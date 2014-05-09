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

import java.net.URI;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;

import com.genologics.ri.GenologicsBatchRetrieveResult;
import com.genologics.ri.Links;
import com.genologics.ri.Locatable;

@Aspect
public class RestCallTrackingAspect
{
    private static final String[] EMPTY_ARRAY = new String[0];

    private static final Pattern SEARCH_TERM_SPLIT = Pattern.compile("&");

    private boolean enabled = false;
    private String[] allowedUris = EMPTY_ARRAY;

    protected CacheManager cacheManager;

    public RestCallTrackingAspect()
    {
    }

    @Required
    public void setCacheManager(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String[] getAllowedUris()
    {
        return allowedUris;
    }

    public void setAllowedUris(String... uris)
    {
        allowedUris = uris;
    }

    public void setAllowedUris(URI... uris)
    {
        allowedUris = new String[uris.length];
        for (int i = 0; i < uris.length; i++)
        {
            allowedUris[i] = uris[i].toString();
        }
    }

    public void setAllowedUris(Locatable... objects)
    {
        allowedUris = new String[objects.length];
        for (int i = 0; i < objects.length; i++)
        {
            allowedUris[i] = objects[i].getUri().toString();
        }
    }

    public void clear()
    {
        allowedUris = EMPTY_ARRAY;
    }

    public Object getForObject(ProceedingJoinPoint pjp) throws Throwable
    {
        if (!enabled)
        {
            return pjp.proceed();
        }

        Object uri = pjp.getArgs()[0];

        final String key = uri.toString();

        Object returned = pjp.proceed();

        boolean isBatch = returned.getClass().getAnnotation(GenologicsBatchRetrieveResult.class) != null;
        boolean links = Links.class.isAssignableFrom(returned.getClass());
        listCache();

        if (!isBatch && !links && !isSearch(key) && !ArrayUtils.contains(allowedUris, key))
        {
            listCache();
            Assert.fail("Not allowed to fetch " + key + ". Should already be in the cache.");
        }

        return returned;
    }

    public ResponseEntity<?> getForEntity(ProceedingJoinPoint pjp) throws Throwable
    {
        if (!enabled)
        {
            return (ResponseEntity<?>)pjp.proceed();
        }

        Object uri = pjp.getArgs()[0];

        final String key = uri.toString();

        ResponseEntity<?> response = (ResponseEntity<?>)pjp.proceed();
        Object returned = response.getBody();

        boolean isBatch = returned.getClass().getAnnotation(GenologicsBatchRetrieveResult.class) != null;
        boolean links = Links.class.isAssignableFrom(returned.getClass());

        if (!isBatch && !links && !isSearch(key) && !ArrayUtils.contains(allowedUris, key))
        {
            listCache();
            Assert.fail("Not allowed to fetch " + key + ". Should already be in the cache.");
        }

        return response;
    }

    void listCache()
    {
        Log logger = LogFactory.getLog(GenologicsAPICacheTest.class);

        if (logger.isDebugEnabled())
        {
            String[] cacheNames = cacheManager.getCacheNames();
            Arrays.sort(cacheNames, Collator.getInstance());

            logger.debug("Cache dump");

            for (String cacheName : cacheNames)
            {
                Ehcache cache = cacheManager.getEhcache(cacheName);
                List<?> keys = cache.getKeys();

                logger.debug("Have " + keys.size() + " things in the " + cacheName + " cache.");

                for (Object key : keys)
                {
                    logger.debug(cache.get(key));
                }
            }
        }
    }

    boolean isSearch(String uri)
    {
        boolean search = false;

        int queryIndex = uri.indexOf('?');
        if (queryIndex >= 0)
        {
            String[] terms = SEARCH_TERM_SPLIT.split(uri.substring(queryIndex + 1));

            for (int i = 0; i < terms.length && !search; i++)
            {
                int equal = terms[i].indexOf('=');
                String key = equal <= 0 ? terms[i] : terms[i].substring(0, equal);
                search = !"state".equals(key);
            }
        }

        return search;
    }
}
