package com.demo.web.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Adds Caching response headers based on a request URL mapping.
 * Each mapping is configured as FORCE_CHECK or NO_CACHE
 *
 * Cache-Control: no-cache, no-store, must-revalidate
 * Cache-Control: private, max-age=0
 * If-Modified-Since: Tue, 21 Apr 2015 16:03:16 GMT
 *
 */
public class CacheHeaderFilter extends GenericFilterBean {

    // sample bean
    @Bean
    public GenericFilterBean cacheHeaderFilter() {
        final LinkedHashMap<String, CacheMode> cacheMap = new LinkedHashMap<>();
        cacheMap.put("/", CacheHeaderFilter.CacheMode.FORCE_CHECK); // Static
        cacheMap.put("/**/*.*", CacheHeaderFilter.CacheMode.FORCE_CHECK); // Static resources
        cacheMap.put("/**", CacheHeaderFilter.CacheMode.NO_CACHE); // RESTful API call

        return new CacheHeaderFilter(cacheMap);
    }
    private LinkedHashMap<String, CacheMode> cacheMap = new LinkedHashMap<>(); // sorted for pattern priority
    private final AntPathMatcher matcher = new AntPathMatcher();

    public CacheHeaderFilter(LinkedHashMap<String, CacheMode> cacheMap) {
        this.cacheMap = cacheMap;
    }

    // Don't know what headers do what?
    // https://devcenter.heroku.com/articles/increasing-application-performance-with-http-cache-headers
    public static void configCacheHeaders(CacheMode mode, HttpServletResponse res) {
        switch (mode) {
            case FORCE_CHECK:
                res.setHeader("Cache-Control", "private, max-age=0");
                break;
            case NO_CACHE:
                res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                res.setHeader("Pragma", "no-cache");
                res.setDateHeader("Expires", 0);
                break;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        String servletPath = ((HttpServletRequest) servletRequest).getServletPath();
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        for (String path : cacheMap.keySet()) {
            if (matcher.match(path, servletPath)) {
                configCacheHeaders(cacheMap.get(path), res);
                break;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public enum CacheMode {NO_CACHE, FORCE_CHECK}
}