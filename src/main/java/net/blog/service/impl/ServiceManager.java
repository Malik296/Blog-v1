package net.blog.service.impl;

import net.blog.service.AvatarService;
import net.blog.service.BusinessService;
import net.blog.service.SocialService;
import net.blog.util.AppUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.sql.SQLException;
import java.util.Properties;

public class ServiceManager {
    private static final String SERVICE_MANAGER = "SERVICE_MANAGER";
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class);

    public static ServiceManager getInstance(ServletContext context) {
        ServiceManager instance = (ServiceManager) context.getAttribute(SERVICE_MANAGER);
        if (instance == null) {
            instance = new ServiceManager(context);
            context.setAttribute(SERVICE_MANAGER, instance);
        }
        return instance;
    }

    public void destroy() {
        try {
            dataSource.close();
        } catch (SQLException e) {
            LOGGER.error("Close dataSource failed: " + e.getMessage(), e);
        }
        LOGGER.info("ServiceManage instance destroy");

    }

    final BusinessService businessService;
    final BasicDataSource dataSource;
    final Properties applicationProperties = new Properties();
    final SocialService socialService;
    final AvatarService avatarService;
    final ServletContext applicationContext;

    public String getApplicationProperty(String property) {
        return applicationProperties.getProperty(property);
    }

    public BusinessService getBusinessService() {
        return businessService;
    }

    private ServiceManager(ServletContext context) {
        applicationContext = context;
        AppUtil.loadProperties(applicationProperties, "application.properties");
        dataSource = createBasicDataSource();
        socialService = new GooglePlusSocialServiceImpl(this);
        avatarService = new FileStorageAvatarService(this);
        businessService = new BusinessServiceImpl(this);
        LOGGER.info("ServiceManage instance created");
    }

    private BasicDataSource createBasicDataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setDefaultAutoCommit(false);
        ds.setRollbackOnReturn(true);
        ds.setDriverClassName(getApplicationProperty("db.driver"));
        ds.setUrl(getApplicationProperty("db.url"));
        ds.setUsername(getApplicationProperty("db.username"));
        ds.setPassword(getApplicationProperty("db.password"));
        ds.setInitialSize(Integer.parseInt(getApplicationProperty("db.pool.initSize")));
        ds.setMaxTotal(Integer.parseInt(getApplicationProperty("db.pool.maxSize")));
        return ds;
    }
}
