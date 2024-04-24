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

import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.ehcache.CacheManager;
import org.ehcache.StateTransitionException;
import org.ehcache.Status;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Factory for creating a singleton EhCache 3 {@code CacheManager} from
 * XML configuration.
 *
 * @since 2.31
 *
 * @see CacheManager
 */
public class EhCacheManagerFactory implements FactoryBean<CacheManager>
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Nullable
    private Resource configLocation;

    @Nullable
    private CacheManager cacheManager;

    /**
     * Constructor.
     */
    public EhCacheManagerFactory()
    {
    }

    /**
     * Set the location of the EhCache config file.
     *
     * @param configLocation The location of the EhCache XML configuration file.
     */
    public void setConfigLocation(Resource configLocation)
    {
        this.configLocation = configLocation;
    }

    /**
     * Create and initialise the cache manager by reading the configuration
     * XML file.
     *
     * @throws IOException if the location cannot be resolved into a URL.
     *
     * @throws StateTransitionException if the {@code CacheManager} could not be made.
     *
     * @see XmlConfiguration
     * @see CacheManager#init()
     */
    @PostConstruct
    public void createCacheManager() throws IOException
    {
        Assert.notNull(configLocation, "EhCache configuration location not set.");

        logger.debug("Initializing EhCache CacheManager");

        Configuration configuration = new XmlConfiguration(configLocation.getURL());

        cacheManager = CacheManagerBuilder.newCacheManager(configuration);
        cacheManager.init();
    }

    @Override
    @Nullable
    public CacheManager getObject()
    {
        return cacheManager;
    }

    @Override
    public Class<? extends CacheManager> getObjectType()
    {
        return cacheManager != null ? cacheManager.getClass() : CacheManager.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }

    /**
     * Close the cache manager.
     */
    @PreDestroy
    public void closeCacheManager()
    {
        if (cacheManager != null && cacheManager.getStatus() == Status.AVAILABLE)
        {
            logger.debug("Shutting down EhCache CacheManager");
            cacheManager.close();
        }
    }
}
