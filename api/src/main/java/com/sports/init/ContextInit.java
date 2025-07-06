package com.sports.init;

import com.sports.cache.util.CacheUtil;
import jakarta.servlet.ServletContextEvent;

public class ContextInit extends com.sports.web.init.ContextInit {
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
        CacheUtil.close();
    }
}
