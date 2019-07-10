package net.blog.controller.page;

import net.blog.Constants;
import net.blog.controller.AbstractController;
import net.blog.entity.Article;
import net.blog.model.Items;
import net.blog.model.Pagination;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/search")
public class SearchController extends AbstractController {
    private static final long serialVersionUID = -1195468551493957291L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        if (StringUtils.isNotBlank(query)) {
            int offset = getOffset(request, Constants.LIMIT_ARTICLES_PER_PAGE);
            Items<Article> items = getBusinessService().listArticlesBySearchQuery(query, offset, Constants.LIMIT_ARTICLES_PER_PAGE);
            request.setAttribute("list", items.getItems());
            request.setAttribute("count", items.getCount());
            request.setAttribute("searchQuery", query);
            Pagination pagination = new Pagination.Builder("/search?query=" + URLEncoder.encode(query, "utf-8") + "&",
                    offset, items.getCount()).withLimit(Constants.LIMIT_ARTICLES_PER_PAGE).build();
            request.setAttribute("pagination", pagination);
            forwardToPage("search.jsp", request, response);
        } else {
            response.sendRedirect("/news");
        }
    }
}
