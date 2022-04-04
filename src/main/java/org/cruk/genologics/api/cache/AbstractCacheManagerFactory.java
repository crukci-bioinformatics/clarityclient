/*
 * CRUK-CI Genologics REST API Java Client.
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

package org.cruk.genologics.api.cache;

import static org.ehcache.config.ResourceType.Core.HEAP;
import static org.ehcache.config.units.EntryUnit.ENTRIES;

import java.time.Duration;

import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;
import org.springframework.beans.factory.FactoryBean;

import com.genologics.ri.LimsEntity;


/**
 * Base class for a factory creating EhCache {@code CacheManager} for the
 * Clarity API. A subclass should extend this to call {@code buildCache}
 * for each entity that wants its own cache configuration.
 * <p>
 * This has become necessary as moving to the EE 9.1 implementations breaks
 * EhCache 3's XML configuration used previously. That depends on the EE8 and
 * earlier JAXB implementation using the <i>javax.xml.bind</i> namespace.
 * Hopefully a newer version of EhCache will switch to EE9 and we can return
 * to the easlier XML cache configuration.
 * </p>
 *
 * @since 2.31
 */
public abstract class AbstractCacheManagerFactory implements FactoryBean<CacheManager>
{
    /**
     * Constructor.
     */
    protected AbstractCacheManagerFactory()
    {
    }

    /**
     * Add a cache to the given CacheManagerBuilder for entities of the given type.
     * <p>
     * This method should be called for each entity a separate cache is desired for,
     * and one for {@link LimsEntity} as a general catch all. The CacheManagerBuilder
     * returned should be used to either build the CacheManager or be passed back in
     * to the next call to this method, as each instance of it is immutable and one
     * needs to move on with the instance returned by the call to this method.
     * </p>
     *
     * @param b The current CacheManagerBuilder. Can be null to start things off, in
     * which case a new one is created.
     *
     * @param type The type of LimsEntity to create the cache for.
     *
     * @param maxInMemory The maximum number of these entities that can be in the memory cache.
     *
     * @param timeToLive How long an entity can stay in the cache before it must be fetched again.
     *
     * @return A new instance of CacheManagerBuilder that has the configuration as
     * passed in as {@code b} plus the new cache requested.
     */
    protected CacheManagerBuilder<CacheManager> buildCache(CacheManagerBuilder<CacheManager> b, Class<?> type, int maxInMemory, int timeToLive)
    {
        ResourcePools pools = ResourcePoolsBuilder.newResourcePoolsBuilder()
            .with(HEAP, maxInMemory, ENTRIES, false)
            .build();

        ExpiryPolicy<Object, Object> expiry =
                ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(timeToLive));

        CacheConfiguration<String, CacheElementWrapper> config =
            CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, CacheElementWrapper.class, pools)
                .withExpiry(expiry)
                .build();

        if (b == null)
        {
            b = CacheManagerBuilder.newCacheManagerBuilder();
        }

        return b.withCache(type.getName(), config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<CacheManager> getObjectType()
    {
        return CacheManager.class;
    }
}
