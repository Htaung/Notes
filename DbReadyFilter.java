package com.example.startup;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns HTTP 503 until StartupListener has set "dbReady" to TRUE.
 * Optional: skip this filter if parts of the app must work without the database.
 */
public class DbReadyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        Object ready = req.getServletContext().getAttribute(StartupListener.DB_READY_ATTR);
        if (Boolean.TRUE.equals(ready)) {
            chain.doFilter(req, res);
        } else {
            HttpServletResponse r = (HttpServletResponse) res;
            r.setHeader("Retry-After", "30");
            r.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                        "Service starting, database not ready");
        }
    }
}
