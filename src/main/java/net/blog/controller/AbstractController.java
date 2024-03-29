package net.blog.controller;

import net.blog.form.CommentForm;
import net.blog.service.BusinessService;
import net.blog.service.impl.ServiceManager;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@WebServlet(name = "AbstractController")
public class AbstractController extends HttpServlet {
    private static final long serialVersionUID = 5600600419551581382L;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private BusinessService businessService;
    public final BusinessService getBusinessService() {
        return businessService;
    }
    public final int getOffset(HttpServletRequest req, int limit) {
        String val = req.getParameter("page");
        if (val != null) {
            int page = Integer.parseInt(val);
            return (page - 1) * limit;
        } else {
            return 0;
        }
    }
    @Override
    public void init() throws ServletException {
        businessService = ServiceManager.getInstance(getServletContext()).getBusinessService();
    }
    public final void forwardToPage (String jspPage, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("currentPage", "page/"+jspPage);
        req.getRequestDispatcher("/WEB-INF/JSP/page-template.jsp").forward(req, resp);
    }

    public final void forwardToFragment (String jspPage, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/JSP/fragment/"+jspPage).forward(req, resp);
    }

    public final <T> T createForm(HttpServletRequest req, Class<T> formClass) throws ServletException {
        try {
            T form = formClass.newInstance();
            BeanUtils.populate(form, req.getParameterMap());
            return form;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ServletException(e);
        }
    }


}
