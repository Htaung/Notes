package com.example.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

/**
 * Starts the database-dependent startup work in a background thread so that
 * a database that is down at deploy time does NOT fail the web context.
 * The thread retries with backoff until the work succeeds, then sets the
 * servlet context attribute "dbReady" to TRUE.
 *
 * Target: Tomcat 9 (javax.servlet). For Tomcat 10+ change imports to jakarta.servlet.
 */
public class StartupListener implements ServletContextListener {

    public static final String DB_READY_ATTR = "dbReady";

    private static final long INITIAL_DELAY_MS = 5_000;
    private static final long MAX_DELAY_MS = 60_000;

    private volatile Thread retryThread;

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        sce.getServletContext().setAttribute(DB_READY_ATTR, Boolean.FALSE);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                long delayMs = INITIAL_DELAY_MS;
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        doDatabaseStartupWork();
                        sce.getServletContext().setAttribute(DB_READY_ATTR, Boolean.TRUE);
                        sce.getServletContext().log("DB startup work completed");
                        return;
                    } catch (Exception e) {
                        if (isFatalLoginError(e)) {
                            sce.getServletContext().log(
                                "Fatal DB login error, not retrying (check credentials / account status)", e);
                            return;
                        }
                        sce.getServletContext().log(
                            "DB not ready, retrying in " + delayMs + " ms: " + e.getMessage());
                        try {
                            Thread.sleep(delayMs);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                        delayMs = Math.min(delayMs * 2, MAX_DELAY_MS);
                    }
                }
            }
        }, "db-startup-retry");

        t.setDaemon(true);
        retryThread = t;
        t.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Thread t = retryThread;
        if (t != null) {
            t.interrupt();
        }
        // If the app does not already close its c3p0 datasource on shutdown,
        // close it here (for example dataSource.close()).
    }

    /**
     * Errors that retrying will never fix and that can lock the Oracle account:
     * ORA-01017 invalid username/password, ORA-28000 account locked,
     * ORA-28001 password expired.
     */
    private boolean isFatalLoginError(Throwable e) {
        while (e != null) {
            if (e instanceof SQLException) {
                int code = ((SQLException) e).getErrorCode();
                if (code == 1017 || code == 28000 || code == 28001) {
                    return true;
                }
            }
            e = e.getCause();
        }
        return false;
    }

    /**
     * TODO: move the code your current startup listener runs here
     * (load config, warm cache, init SessionFactory, etc.).
     * Throw any exception on failure so the loop retries.
     */
    private void doDatabaseStartupWork() throws Exception {
        // Example only: replace with your real startup work.
        // try (java.sql.Connection c = dataSource.getConnection()) { ... }
    }
}
