package org.cruk.genologics.api.cache;

import java.io.Serializable;

class CacheElementWrapper implements Serializable
{
    private static final long serialVersionUID = 5222412342746165964L;

    protected Object entity;
    protected long version = GenologicsAPICache.NO_STATE_VALUE;

    public CacheElementWrapper(Object entity)
    {
        assert entity instanceof Serializable : "Entity must be serializable.";
        this.entity = entity;
    }

    public CacheElementWrapper(Object entity, Long version)
    {
        this(entity);
        this.version = version;
    }

    public long getVersion()
    {
        return version;
    }

    public void setVersion(long state)
    {
        this.version = state;
    }

    public Object getEntity()
    {
        return entity;
    }
}
