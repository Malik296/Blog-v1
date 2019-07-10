package net.blog.listener;

import net.blog.Constants;
import net.blog.entity.Category;
import net.blog.service.impl.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Map;

@WebListener()
public class ApplicationListener implements ServletContextListener {
    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServiceManager serviceManager = ServiceManager.getInstance(sce.getServletContext());
        Map<Integer, Category> map = serviceManager.getBusinessService().mapCategories();
        sce.getServletContext().setAttribute("social_googleplus_clientId", serviceManager.getApplicationProperty("social.googleplus.clientId"));
        sce.getServletContext().setAttribute(Constants.CATEGORY_MAP, map);

        LOGGER.info("Application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServiceManager.getInstance(sce.getServletContext()).destroy();
        LOGGER.info("Application destroyed");
    }
}
