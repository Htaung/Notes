# Liferay DXP 7.4: Recovering a Service Builder (SB) Jar After a Failed Hot Deploy

## Problem

1. Hot-deployed a new Service Builder jar.
2. Script errors occurred and the SB module failed to start.
3. Restarted Liferay and hot-deployed the rollback (old) jar.
4. Restarted again, but the SB module still would not start.

## Cause

- A failed upgrade leaves `Release_.state_ = 1` for the module (the last upgrade failed). While it is 1, the module's service does not start, even with a working rollback jar.
- `Release_.schemaVersion` is only updated after a successful upgrade, so it is unchanged by the failure.
- The `ServiceComponent` build number can also be recorded early by the failed deploy. If the rollback jar's `build.number` is lower than the recorded one, it can be ignored.

## Fix That Worked

1. Shut down Liferay.
2. Update the module's state to 0:
   ```sql
   UPDATE Release_ SET state_ = 0
   WHERE servletContextName = 'your.module.name.service';
   ```
3. Clear the cache (delete `LIFERAY_HOME/osgi/state`).
4. Copy the SB jar into the `deploy` folder (no hot deploy while the server is running).
5. Start Liferay. The module started normally.

## Diagnostic Queries

```sql
-- Upgrade state of the module (0 = OK, 1 = upgrade failed)
SELECT schemaVersion, buildNumber, state_, verified
FROM Release_
WHERE servletContextName = 'your.module.name.service';

-- Highest recorded service build number
SELECT buildNamespace, buildNumber
FROM ServiceComponent
WHERE buildNamespace = 'YourNamespace'
ORDER BY buildNumber DESC;
```

## Alternative Used Earlier (7.3)

- Set a higher `build.number` in `service.properties` than the highest value in `ServiceComponent`, rebuild, and redeploy.

## Gogo Shell Checks (if it still won't start)

```
lb | grep your.module
diag <bundle-id>
```

## Quick Recovery Checklist

1. Stop Liferay and back up the database.
2. Check `Release_.state_`; set it to 0 if it is 1.
3. Make sure the jar's `build.number` is higher than the highest `ServiceComponent.buildNumber` (or delete the stale higher rows).
4. Delete `osgi/state`, copy the jar to `deploy`, and start the server.

## Notes and Prevention

- Resetting `state_` only clears the flag. It does not undo partial changes made by a failed upgrade, so verify tables and columns match what the rollback jar expects, or restore from backup.
- Test SB changes against a copy of the database first.
- Back up the database before deploying SB changes.
- In production, deploy during a controlled restart instead of hot deploy.
