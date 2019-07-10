package net.blog.controller.ajax;
import net.blog.controller.AbstractController;
import net.blog.entity.Comment;
import net.blog.form.CommentForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@WebServlet("/ajax/comment")
public class NewCommentController extends AbstractController {
    private static final long serialVersionUID = 4688332757509734157L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommentForm form = createForm(req, CommentForm.class); // TODO
        Comment comment = getBusinessService().createComment(form);
        req.setAttribute("comments", Collections.singleton(comment));
        forwardToFragment("comments.jsp", req, resp);
    }


}
