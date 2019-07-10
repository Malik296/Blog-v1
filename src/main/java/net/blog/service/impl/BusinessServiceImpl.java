package net.blog.service.impl;

import net.blog.dao.SQLDAO;
import net.blog.entity.Account;
import net.blog.entity.Article;
import net.blog.entity.Category;
import net.blog.entity.Comment;
import net.blog.exception.ApplicationException;
import net.blog.exception.RedirectToValidUrlException;
import net.blog.form.CommentForm;
import net.blog.model.Items;
import net.blog.model.SocialAccount;
import net.blog.service.AvatarService;
import net.blog.service.BusinessService;
import net.blog.service.SocialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);

    private final DataSource dataSource;
    private final SQLDAO sql;

    private final SocialService socialService;
    private final AvatarService avatarService;

    BusinessServiceImpl(ServiceManager serviceManager) {
        this.dataSource = serviceManager.dataSource;
        this.socialService = serviceManager.socialService;
        this.avatarService = serviceManager.avatarService;
        this.sql = new SQLDAO();
    }

    @Override
    public Map<Integer, Category> mapCategories() {
        try (Connection c = dataSource.getConnection()) {
            return sql.mapCategories(c);
        } catch (SQLException e) {
            throw new ApplicationException("Can't execute db command: " + e.getMessage(), e);
        }
    }

    @Override
    public Items<Article> listArticles(int offset, int limit) {
        try (Connection c = dataSource.getConnection()) {
            Items<Article> items = new Items<>();
            items.setItems(sql.listArticles(c, offset, limit));
            items.setCount(sql.countArticles(c));
            return items;
        } catch (SQLException e) {
            throw new ApplicationException("Can't execute db command: " + e.getMessage(), e);
        }
    }

    @Override
    public Items<Article> listArticlesByCategory(String urlCategory, int offset, int limit) {
        try (Connection c = dataSource.getConnection()) {
            Items<Article> items = new Items<>();
            items.setItems(sql.listArticlesByCategory(c, urlCategory, offset, limit));
            items.setCount(sql.countArticlesByCategory(c, urlCategory));
            return items;
        } catch (SQLException e) {
            throw new ApplicationException("Can't execute db command: " + e.getMessage(), e);
        }
    }

    @Override
    public Category findCategoryByUrl(String categoryUrl) {
        try (Connection c = dataSource.getConnection()) {
            return sql.findCategoryByUrl(c, categoryUrl);
        } catch (SQLException e) {
            throw new ApplicationException("Can't execute db command: " + e.getMessage(), e);
        }
    }

    @Override
    public Items<Article> listArticlesBySearchQuery(String searchQuery, int offset, int limit) {
        try (Connection c = dataSource.getConnection()) {
            Items<Article> items = new Items<>();
            items.setItems(sql.listArticlesBySearchQuery(c, searchQuery, offset, limit));
            items.setCount(sql.countArticlesBySearchQuery(c, searchQuery));
            return items;
        } catch (SQLException e) {
            throw new ApplicationException("Can't execute db command: " + e.getMessage(), e);
        }
    }

    @Override
    public Article viewArticle(long articleId, String requestUrl) throws RedirectToValidUrlException {
        try (Connection c = dataSource.getConnection()) {
            Article a = sql.findArticleById(c, articleId);
            if (a == null) {
                return null;
            }
            if (!a.getArticleLink().equals(requestUrl)) {
                throw new RedirectToValidUrlException(a.getArticleLink());
            } else {
                a.setViews(a.getViews() + 1);
                sql.updateArticleViews(c, a);
                c.commit();
                return a;
            }
        } catch (SQLException e) {
            throw new ApplicationException("Can't execute db command: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Comment> listComments(long id, int offset, int limit) {
        try (Connection c = dataSource.getConnection()){
            return sql.listComments(c, id, offset, limit);
        } catch (SQLException e) {
            throw new ApplicationException("Can't execute db command: " + e.getMessage(), e);
        }
    }

    @Override
    public Comment createComment(CommentForm form) {
        String newAvatarPath = null;
        try (Connection c = dataSource.getConnection()) {
            SocialAccount socialAccount = socialService.getSocialAccount(form.getAuthToken());
            Account account = sql.findAccountByEmail(c, socialAccount.getEmail());
            if (account == null) {
                newAvatarPath = avatarService.downloadAvatar(socialAccount.getAvatar());
                account = sql.createNewAccount(c, socialAccount.getEmail(), socialAccount.getName(), newAvatarPath);
            }
            Comment comment = sql.createComment(c, form, account.getId());
            comment.setAccount(account);
            Article article = sql.findArticleForNewCommentNotification(c, form.getIdArticle());
            article.setComments(sql.countComments(c, article.getId()));
            sql.updateArticleComments(c, article);
            c.commit();
            // after commit
            //TODO Send new comment notification
            return comment;
        } catch (SQLException | RuntimeException | IOException e) {
            if(avatarService.deleteAvatarIfExists(newAvatarPath)){
                LOGGER.info("Avatar "+newAvatarPath+" deleted");
            }
            throw new ApplicationException("Can't create new comment: " + e.getMessage(), e);
        }
    }
}
