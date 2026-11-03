# Oracle-tolerant startup for Tomcat 9 web apps

Stops one web app from failing (and blocking other deployments) when the Oracle
database is unreachable at startup. The context starts immediately; the
DB-dependent startup work retries in a background thread with backoff until it
succeeds.

## Files

| File | Purpose |
|---|---|
| `StartupListener.java` | Runs your DB startup work in a retry thread; sets `dbReady` when done |
| `DbReadyFilter.java` | Optional. Returns HTTP 503 until `dbReady` is true |
| `web-xml-snippet.xml` | `web.xml` entries to register the listener and filter |
| `c3p0-settings.xml` | Fast-fail c3p0 pool settings (Spring bean example) |

## Setup

1. Put the two Java files under `src/main/java/com/example/startup/` (change the package if you like).
2. Move your existing startup code into `doDatabaseStartupWork()` in `StartupListener`.
3. Replace the old startup listener entry in `web.xml` with the snippet.
4. Apply the c3p0 settings to your datasource. Keep `CONNECT_TIMEOUT=5` in the JDBC URL.
5. Build the war and deploy.

Tomcat 10+ uses `jakarta.servlet` instead of `javax.servlet`; change the imports.

## Behaviour

- Retry delay starts at 5 s and doubles up to 60 s.
- Stops retrying on ORA-01017, ORA-28000 and ORA-28001 so a bad password cannot lock the account.
- The retry thread is interrupted in `contextDestroyed`, so undeploy leaves no leaked thread.

## Test

1. Block the DB (or use a wrong port) and start the app: the context starts, the log shows "DB not ready, retrying".
2. Restore the DB: within a minute the log shows "DB startup work completed" and requests are served.
3. Undeploy during retry: no "failed to stop thread" warning.

## Notes

- If the failing startup listener is Spring's `ContextLoaderListener` or Hibernate, make DB-dependent beans
  `lazy-init="true"`, or for Hibernate set `hibernate.temp.use_jdbc_metadata_defaults=false`,
  set `hibernate.dialect` explicitly and remove `hbm2ddl.auto`.
- Never commit real passwords. Keep credentials in environment variables or a secrets store.
