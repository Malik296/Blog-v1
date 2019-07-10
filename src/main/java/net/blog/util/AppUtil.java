package net.blog.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppUtil {
    public static void loadProperties(Properties properties, String classPathUrl) {
        try (InputStream in = AppUtil.class.getClassLoader().getResourceAsStream(classPathUrl)){
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't load properties from classpath: " + classPathUrl, e);
        }
    }
}
