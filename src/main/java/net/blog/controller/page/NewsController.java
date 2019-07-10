package net.blog.controller.page;

import net.blog.Constants;
import net.blog.controller.AbstractController;
import net.blog.entity.Article;
import net.blog.entity.Category;
import net.blog.model.Items;
import net.blog.model.Pagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/news", "/news/*"})
public class NewsController extends AbstractController {
    private static final long serialVersionUID = 1509585383250651611L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int offset = getOffset(req, Constants.LIMIT_ARTICLES_PER_PAGE);
        String requestUrl = req.getRequestURI();
        Items<Article> items = null;
        if(requestUrl.endsWith("/news") || requestUrl.endsWith("/news/")){
            items = getBusinessService().listArticles(offset, Constants.LIMIT_ARTICLES_PER_PAGE);
            req.setAttribute("isNewsPage", Boolean.TRUE);
        }
        else{
            String categoryUrl = requestUrl.replace("/news", "");
            Category category = getBusinessService().findCategoryByUrl(categoryUrl);
            if (category == null) {
                resp.sendRedirect("/404?url=" + requestUrl);
                return;
            }
            items = getBusinessService().listArticlesByCategory(categoryUrl, offset, Constants.LIMIT_ARTICLES_PER_PAGE);
            req.setAttribute("selectedCategory", category);
        }
        req.setAttribute("list", items.getItems());
        Pagination pagination = new Pagination.Builder(requestUrl+"?", offset, items.getCount()).withLimit(Constants.LIMIT_ARTICLES_PER_PAGE).build();
        req.setAttribute("pagination", pagination);
        forwardToPage("news.jsp", req, resp);
    }
}
