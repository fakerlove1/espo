package com.stackfarm.esports.cache.sql.tables;


import com.stackfarm.esports.cache.sql.SqlCache;

/**
 * @author 十三月之夜
 */
public class SystemUserRolesCache extends SqlCache {

    private final String id;

    private static final String SQL_CACHE_PREFIX = SqlCache.SQL_CACHE_PREFIX + "SYSTEM_USER_ROLES_";

    public SystemUserRolesCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        super.putObject(SQL_CACHE_PREFIX + key, value);
    }

    @Override
    public Object getObject(Object key) {
        return super.getObject(SQL_CACHE_PREFIX + key);
    }

    @Override
    public Object removeObject(Object key) {
        return super.removeObject(SQL_CACHE_PREFIX + key);
    }

    @Override
    public void clear() {
        super.clear(SQL_CACHE_PREFIX + "*");
    }
}
