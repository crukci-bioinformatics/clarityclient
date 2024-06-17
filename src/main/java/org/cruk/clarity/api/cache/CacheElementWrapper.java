/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2013 Cancer Research UK Cambridge Institute.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cruk.clarity.api.cache;

import java.io.Serializable;

/**
 * A class emulating the EhCache 2.x {@code CacheElement} class, which
 * had a version attribute. We make use of that for stateful entities
 * (i.e. Artifact) to manage the versions.
 *
 * @since 2.31
 */
class CacheElementWrapper implements Serializable
{
    private static final long serialVersionUID = 5222412342746165964L;

    /**
     * The actual entity in the cache.
     */
    protected Object entity;

    /**
     * The version of this entity.
     */
    protected long version = ClarityAPICache.NO_STATE_VALUE;

    /**
     * Constructor.
     *
     * @param entity The entity to store in the cache.
     */
    public CacheElementWrapper(Object entity)
    {
        assert entity instanceof Serializable : "Entity must be serializable.";
        this.entity = entity;
    }

    /**
     * Constructor.
     *
     * @param entity The entity to store in the cache.
     * @param version The version of this entity.
     */
    public CacheElementWrapper(Object entity, long version)
    {
        this(entity);
        this.version = version;
    }

    /**
     * Get the cached entity.
     *
     * @return The entity.
     */
    public Object getEntity()
    {
        return entity;
    }

    /**
     * Get the version of the entity in the cache.
     *
     * @return The version.
     */
    public long getVersion()
    {
        return version;
    }

    /**
     * Set the version of the entity in the cache.
     *
     * @param state The new version.
     */
    public void setVersion(long state)
    {
        this.version = state;
    }
}
