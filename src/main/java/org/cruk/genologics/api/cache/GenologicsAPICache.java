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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.cruk.genologics.api.GenologicsAPI;
import org.cruk.genologics.api.StatefulOverride;
import org.cruk.genologics.api.impl.GenologicsAPIImpl;
import org.cruk.genologics.api.impl.GenologicsAPIInternal;
import org.cruk.genologics.api.impl.LatestVersionsResetAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.Locatable;
import com.genologics.ri.file.GenologicsFile;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * A cache optionally deployed around a {@code GenologicsAPI} bean as an aspect.
 * Looks to see what objects have already been fetched or created and, if they
 * are in the cache, doesn't go back to the server for them. Creates, updates and
 * deletes are passed through immediately.
 *
 * <p>
 * The "stateful" {@code Artifact} class produces some problems. There is more than
 * one way these could be handled, where an Artifact's state (specifically its QC flag)
 * will be different for different number of its "state" URI parameter.
 * This implementation uses a "latest available" strategy, where if the state
 * requested for an Artifact is earlier than the one in the cache, the cached version
 * is returned. If a later state is requested, it is fetched and replaces the one in
 * the cache.
 * This strategy may not be suitable for all cases and future refinements will allow
 * different strategies to be selected.
 * </p>
 */
@Aspect
public class GenologicsAPICache
{
    /**
     * The version to use for objects and requests that give no state.
     */
    static final long NO_STATE_VALUE = 0L;

    /**
     * The part of the URI that specifies the state number.
     */
    private static final String STATE_TERM = "state=";

    /**
     * The length of the STATE_TERM string.
     */
    private static final int STATE_TERM_LENGTH = STATE_TERM.length();

    /**
     * Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(GenologicsAPICache.class);

    /**
     * The API this aspect will call through to.
     */
    protected GenologicsAPI api;

    /**
     * The API again, but via its internal interface.
     */
    protected GenologicsAPIInternal apiCacheControl;

    /**
     * The Ehcache cache manager.
     */
    protected CacheManager cacheManager;

    /**
     * The aspect that resets API version behaviour.
     */
    protected LatestVersionsResetAspect latestVersionsResetAspect;

    /**
     * The behaviour for dealing with stateful entities.
     */
    protected CacheStatefulBehaviour behaviour = CacheStatefulBehaviour.LATEST;

    /**
     * Lock to prevent the cache behaviour changing during an operation.
     */
    private Lock behaviourLock = new ReentrantLock();


    /**
     * Empty constructor.
     */
    public GenologicsAPICache()
    {
    }

    /**
     * Sets the API being used.
     *
     * @param api The GenologicsAPI bean.
     */
    @Required
    public void setGenologicsAPI(GenologicsAPI api)
    {
        this.api = api;
    }

    /**
     * Set the internal interface access to the API.
     *
     * @param internalApi The API bean, but through its internal interface.
     */
    @Required
    public void setInternalGenologicsAPI(GenologicsAPIInternal internalApi)
    {
        this.apiCacheControl = internalApi;
    }

    /**
     * Set the Ehcache cache manager.
     *
     * @param cacheManager The cache manager.
     */
    @Required
    public void setCacheManager(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    /**
     * Set a reference to the aspect that resets the API's behaviour around stateful
     * entities.
     *
     * @param latestVersionsResetAspect The version resetting aspect.
     * @see #cancelStatefulOverride(JoinPoint)
     */
    @Required
    public void setLatestVersionsResetAspect(LatestVersionsResetAspect latestVersionsResetAspect)
    {
        this.latestVersionsResetAspect = latestVersionsResetAspect;
    }

    /**
     * Set the behaviour for dealing with stateful objects. Note that changing
     * this behaviour during operation clears the cache.
     *
     * @param behaviour The desired behaviour.
     *
     * @since 2.22
     */
    public void setStatefulBehaviour(CacheStatefulBehaviour behaviour)
    {
        if (behaviour != null && this.behaviour != behaviour)
        {
            behaviourLock.lock();
            try
            {
                this.behaviour = behaviour;

                // Guard for null so the order of Spring property setting
                // doesn't matter.

                if (cacheManager != null)
                {
                    cacheManager.clearAll();
                }
            }
            finally
            {
                behaviourLock.unlock();
            }
        }
    }

    /**
     * Clears the cache of all cached entities.
     */
    public void clear()
    {
        cacheManager.clearAll();
    }

    /**
     * Get an Ehcache for the given class.
     *
     * @param type The class for the cache required.
     *
     * @return The Ehcache for the class given. If there is no specific
     * cache defined for the class, use the general purpose "LimsEntity"
     * cache.
     *
     * @see #loadOrRetrieve(ProceedingJoinPoint, String, Class)
     */
    public Ehcache getCache(Class<?> type)
    {
        if (type == null)
        {
            throw new IllegalArgumentException("type cannot be null");
        }

        Ehcache cache = cacheManager.getEhcache(type.getName());
        if (cache == null)
        {
            cache = cacheManager.getEhcache(LimsEntity.class.getName());
        }

        return cache;
    }

    /**
     * Makes sure the effects of {@code GenologicsAPI.overrideStateful()}
     * is reset after a call. The API itself has its own wrapper to clear
     * this after a call, but if the cache intercepts the call and doesn't
     * call through to the API (because the objects are already in the cache)
     * we need to make sure the behaviour reverts to respecting the state
     * version anyway.
     *
     * <p>
     * If one considers this, it might not be necessary because when the API
     * is instructed to fetch the most recent, the call will always pass through
     * the cache. This is a belt and braces method.
     * </p>
     *
     * @param jp The join point.
     *
     * @see GenologicsAPIInternal#cancelStatefulOverride(String)
     * @see LatestVersionsResetAspect#cancelStatefulOverride(JoinPoint)
     */
    public void cancelStatefulOverride(JoinPoint jp)
    {
        latestVersionsResetAspect.cancelStatefulOverride(jp);
    }

    /**
     * Join point for the {@code GenologicsAPI.retrieve} methods. Fetches the
     * object requested, either from the cache or from the API.
     *
     * @param pjp The join point object.
     *
     * @return The object retrieved.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#retrieve(String, Class)
     * @see GenologicsAPI#retrieve(URI, Class)
     * @see #loadOrRetrieve(ProceedingJoinPoint, String, Class)
     */
    public Object retrieve(ProceedingJoinPoint pjp) throws Throwable
    {
        assert pjp.getArgs().length == 2 : "Wrong number of arguments.";

        Object thing = pjp.getArgs()[0];
        if (thing == null)
        {
            throw new IllegalArgumentException("uri cannot be null");
        }
        Class<?> entityClass = (Class<?>)pjp.getArgs()[1];
        String uri = thing.toString();

        return loadOrRetrieve(pjp, uri, entityClass);
    }

    /**
     * Join point for the {@code GenologicsAPI.load} methods taking an id and a class.
     * Fetches the object requested, either from the cache or from the API.
     *
     * @param pjp The join point object.
     *
     * @return The object retrieved.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#load(String, Class)
     * @see #loadOrRetrieve(ProceedingJoinPoint, String, Class)
     */
    public Object loadById(ProceedingJoinPoint pjp) throws Throwable
    {
        Object[] args = pjp.getArgs();

        Object id1, id2;
        Class<?> entityClass;
        String uri;

        switch (args.length)
        {
            case 2:
                id1 = args[0];
                if (id1 == null)
                {
                    throw new IllegalArgumentException("limsid cannot be null");
                }
                entityClass = (Class<?>)args[1];
                uri = toUriString(entityClass, id1.toString());
                return loadOrRetrieve(pjp, uri, entityClass);

            case 3:
                id1 = args[0];
                if (id1 == null)
                {
                    throw new IllegalArgumentException("outerLimsid cannot be null");
                }
                id2 = args[0];
                if (id2 == null)
                {
                    throw new IllegalArgumentException("innerLimsid cannot be null");
                }
                entityClass = (Class<?>)args[2];
                uri = toUriString(entityClass, id1.toString(), id2.toString());
                return loadOrRetrieve(pjp, uri, entityClass);

            default:
                throw new AssertionError("Wrong number of arguments to a load by id method");
        }
    }

    /**
     * Join point for the {@code GenologicsAPI.load} methods taking a {@code LimsLink}.
     * Fetches the object requested, either from the cache or from the API.
     *
     * @param pjp The join point object.
     *
     * @return The object retrieved.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#load(LimsLink)
     * @see #loadOrRetrieve(ProceedingJoinPoint, String, Class)
     */
    public Object loadByLink(ProceedingJoinPoint pjp) throws Throwable
    {
        assert pjp.getArgs().length == 1 : "Wrong number of arguments.";

        LimsLink<?> link = (LimsLink<?>)pjp.getArgs()[0];
        if (link == null)
        {
            throw new IllegalArgumentException("link cannot be null");
        }
        if (link.getUri() == null)
        {
            throw new IllegalArgumentException("link uri cannot be null");
        }

        String uri = link.getUri().toString();

        return loadOrRetrieve(pjp, uri, link.getEntityClass());
    }

    /**
     * Get the object from its cache wrapper. In its own method to suppress
     * unchecked warnings.
     *
     * @param <E> The type of object held in the cache element.
     * @param wrapper The cache Element.
     *
     * @return The object in the cache element.
     */
    @SuppressWarnings("unchecked")
    protected <E extends Locatable> E getFromWrapper(Element wrapper)
    {
        return wrapper == null ? null : (E)wrapper.getObjectValue();
    }

    /**
     * Fetch an object from the cache or, if it's not yet been seen, from the
     * API and store the result in the cache for future use.
     *
     * <p>
     * Special consideration has to be made for objects that have a "state"
     * parameter to their URIs. See the class description for more details.
     * </p>
     *
     * @param pjp The join point object.
     * @param uri The URI of the object to fetch.
     * @param entityClass The type of object to fetch.
     *
     * @return The object retrieved.
     *
     * @throws Throwable if there is an error.
     */
    protected Object loadOrRetrieve(ProceedingJoinPoint pjp, String uri, Class<?> entityClass) throws Throwable
    {
        if (!isCacheable(entityClass))
        {
            return pjp.proceed();
        }

        final boolean statefulEntity = isStateful(entityClass);

        final String className = ClassUtils.getShortClassName(entityClass);

        Ehcache cache = getCache(entityClass);

        Locatable genologicsObject = null;
        String key = keyFromUri(uri);
        long version = NO_STATE_VALUE;

        behaviourLock.lock();
        try
        {
            Element wrapper = null;
            if (key != null)
            {
                wrapper = cache.get(key);
                if (wrapper != null)
                {
                    if (!statefulEntity)
                    {
                        genologicsObject = getFromWrapper(wrapper);
                    }
                    else if (!isFetchLatestVersions())
                    {
                        version = versionFromUri(uri);

                        switch (getBehaviourForCall())
                        {
                            case ANY:
                                genologicsObject = getFromWrapper(wrapper);
                                break;

                            case LATEST:
                                if (version == NO_STATE_VALUE || version <= wrapper.getVersion())
                                {
                                    genologicsObject = getFromWrapper(wrapper);
                                }
                                break;

                            case EXACT:
                                if (version == NO_STATE_VALUE || version == wrapper.getVersion())
                                {
                                    genologicsObject = getFromWrapper(wrapper);
                                }
                                break;
                        }
                    }
                }
            }

            if (genologicsObject == null)
            {
                if (logger.isDebugEnabled())
                {
                    if (version == NO_STATE_VALUE)
                    {
                        logger.debug("Don't have {} {} - calling through to API {}", className, key, pjp.getSignature().getName());
                    }
                    else
                    {
                        logger.debug("Have a different version of {} {} - calling through to API {}", className, key, pjp.getSignature().getName());
                    }
                }

                genologicsObject = (Locatable)pjp.proceed();

                if (wrapper == null)
                {
                    // Not already in the cache, so it needs to be stored.
                    cache.put(createCacheElement(genologicsObject));
                }
                else
                {
                    // Most entities already in the cache will just stay there.
                    // If though we have a stateful entity, there may be cause
                    // to replace the object in the cache depending on how the
                    // cache normally behaves. Typically this will be replacing the
                    // existing with a newer version or replacing for a difference.
                    // When we don't care about versions, the one already in the cache
                    // can remain.

                    if (statefulEntity)
                    {
                        switch (behaviour)
                        {
                            case ANY:
                                break;

                            case LATEST:
                                if (version > wrapper.getVersion())
                                {
                                    cache.put(createCacheElement(genologicsObject));
                                }
                                break;

                            case EXACT:
                                if (version != wrapper.getVersion())
                                {
                                    cache.put(createCacheElement(genologicsObject));
                                }
                                break;
                        }
                    }
                }
            }
            else
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Already have {} {} in the cache.", className, key);
                }
            }
        }
        finally
        {
            behaviourLock.unlock();
        }

        return genologicsObject;
    }

    /**
     * Join point for the {@code GenologicsAPI.loadAll} method.
     * Examines the cache for objects already loaded and only fetches those
     * that are not already seen (or, for stateful objects, those whose requested
     * state is later than that in the cache).
     *
     * @param <E> The type of LIMS entity referred to.
     * @param pjp The join point object.
     *
     * @return The list of entities retrieved.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#loadAll(Collection)
     */
    public <E extends Locatable> List<E> loadAll(ProceedingJoinPoint pjp) throws Throwable
    {
        @SuppressWarnings("unchecked")
        Collection<LimsLink<E>> links = (Collection<LimsLink<E>>)pjp.getArgs()[0];

        List<E> results = new ArrayList<E>(links == null ? 0 : links.size());

        if (links != null && !links.isEmpty())
        {
            Ehcache cache = null;

            List<LimsLink<E>> toFetch = new ArrayList<LimsLink<E>>(links.size());
            List<E> alreadyCached = new ArrayList<E>(links.size());

            Boolean cacheable = null;
            String className = null;
            Boolean stateful = null;

            CacheStatefulBehaviour callBehaviour = getBehaviourForCall();

            behaviourLock.lock();
            try
            {
                Iterator<LimsLink<E>> linkIterator = links.iterator();

                // Loop through the links requested and accumulate two lists of links:
                // those that are not in the cache and need to be fetched and those that
                // have already been fetched. While doing this, assemble in "results" those
                // entities already in the cache that don't need to be fetch. This list will
                // have nulls inserted where the entity needs to be fetched.

                while (linkIterator.hasNext())
                {
                    LimsLink<E> link = linkIterator.next();
                    if (link == null)
                    {
                        throw new IllegalArgumentException("link contains a null");
                    }
                    if (link.getUri() == null)
                    {
                        throw new IllegalArgumentException("A link in the collection has no URI set.");
                    }

                    if (className == null)
                    {
                        className = ClassUtils.getShortClassName(link.getEntityClass());
                        cacheable = isCacheable(link.getEntityClass());
                        stateful = isStateful(link.getEntityClass());
                    }

                    E entity = null;
                    if (!cacheable)
                    {
                        // Fetch always.
                        toFetch.add(link);
                    }
                    else
                    {
                        if (cache == null)
                        {
                            cache = getCache(link.getEntityClass());
                        }

                        String key = keyFromLocatable(link);

                        Element wrapper = cache.get(key);
                        if (wrapper == null)
                        {
                            toFetch.add(link);
                        }
                        else
                        {
                            if (isFetchLatestVersions())
                            {
                                toFetch.add(link);
                            }
                            else
                            {
                                long version = versionFromLocatable(link);

                                switch (callBehaviour)
                                {
                                    case ANY:
                                        entity = getFromWrapper(wrapper);
                                        alreadyCached.add(entity);
                                        break;

                                    case LATEST:
                                        if (version != NO_STATE_VALUE && version > wrapper.getVersion())
                                        {
                                            toFetch.add(link);
                                        }
                                        else
                                        {
                                            entity = getFromWrapper(wrapper);
                                            alreadyCached.add(entity);
                                        }
                                        break;

                                    case EXACT:
                                        if (version != NO_STATE_VALUE && version != wrapper.getVersion())
                                        {
                                            toFetch.add(link);
                                        }
                                        else
                                        {
                                            entity = getFromWrapper(wrapper);
                                            alreadyCached.add(entity);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                    results.add(entity);
                }

                if (logger.isWarnEnabled())
                {
                    if (cache.getCacheConfiguration().getMaxEntriesLocalHeap() < links.size())
                    {
                        logger.warn("{} {}s are requested, but the cache will only hold {}. Repeated fetches of this collection will always call through to the API.",
                                links.size(), className, cache.getCacheConfiguration().getMaxEntriesLocalHeap());
                    }
                }

                if (logger.isDebugEnabled())
                {
                    if (alreadyCached.size() == links.size())
                    {
                        logger.debug("All {} {}s requested are already in the cache.", links.size(), className);
                    }
                    else
                    {
                        logger.debug("Have {} {}s in the cache; {} to retrieve.", alreadyCached.size(), className, toFetch.size());
                    }
                }

                // If there is anything to fetch, perform the call to the API then
                // fill in the nulls in the "results" list from the entities returned
                // from the API.
                // The end result is that newly fetched items are put into the cache
                // and "results" is a fully populated list.

                if (!toFetch.isEmpty())
                {
                    assert cacheable != null : "No cacheable flag found";
                    assert stateful != null : "No stateful flag found";

                    Object[] args = { toFetch };
                    @SuppressWarnings("unchecked")
                    List<E> fetched = (List<E>)pjp.proceed(args);

                    ListIterator<E> resultIterator = results.listIterator();
                    ListIterator<E> fetchIterator = fetched.listIterator();

                    while (resultIterator.hasNext())
                    {
                        E entity = resultIterator.next();
                        if (entity == null)
                        {
                            assert fetchIterator.hasNext() : "Run out of items in the fetched list.";
                            entity = fetchIterator.next();
                            resultIterator.set(entity);

                            if (cacheable)
                            {
                                if (!stateful)
                                {
                                    // Entities without state will only have been fetched because they
                                    // were not in the cache. These should just be added.

                                    cache.put(createCacheElement(entity));
                                }
                                else
                                {
                                    // Stateful entities may already be in the cache but may have been
                                    // fetched because the requested version is newer or of a different
                                    // state. Some care needs to be taken to update its cached version
                                    // depending on how the cache normally behaves.

                                    String key = keyFromLocatable(entity);
                                    Element wrapper = cache.get(key);

                                    if (wrapper == null)
                                    {
                                        // Not already cached, so simply add this entity whatever
                                        // its state.

                                        cache.put(createCacheElement(entity));
                                    }
                                    else
                                    {
                                        // As we have a stateful entity, there may be cause
                                        // to replace the object in the cache depending on how the
                                        // cache normally behaves. Typically this will be replacing the
                                        // existing with a newer version or replacing for a difference.
                                        // When we don't care about versions, the one already in the cache
                                        // can remain.

                                        long version = versionFromLocatable(entity);

                                        switch (behaviour)
                                        {
                                            case ANY:
                                                break;

                                            case LATEST:
                                                if (version > wrapper.getVersion())
                                                {
                                                    cache.put(createCacheElement(entity));
                                                }
                                                break;

                                            case EXACT:
                                                if (version != wrapper.getVersion())
                                                {
                                                    cache.put(createCacheElement(entity));
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    assert !fetchIterator.hasNext() : "Have further items fetched after populating results list.";
                }
            }
            finally
            {
                behaviourLock.unlock();
            }
        }

        return results;
    }

    /**
     * Join point for the {@code GenologicsAPI.reload} method.
     * Force a reload from the API of an object by fetching again and updating
     * its cache entry.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#reload(LimsEntity)
     */
    public void reload(ProceedingJoinPoint pjp) throws Throwable
    {
        Locatable entity = (Locatable)pjp.getArgs()[0];

        pjp.proceed();

        if (isCacheable(entity))
        {
            Ehcache cache = getCache(entity.getClass());
            cache.put(createCacheElement(entity));
        }
    }

    /**
     * Join point for the {@code GenologicsAPI.create} method.
     * Create the object through the API and, if it can be cached, record it
     * in the cache.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#create(Locatable)
     */
    public void create(ProceedingJoinPoint pjp) throws Throwable
    {
        Locatable entity = (Locatable)pjp.getArgs()[0];

        pjp.proceed();

        if (isCacheable(entity))
        {
            Ehcache cache = getCache(entity.getClass());

            cache.put(createCacheElement(entity));
        }
    }

    /**
     * Join point for the {@code GenologicsAPI.createAll} method.
     * Create the objects through the API and, if they can be cached, record them
     * in the cache.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#createAll(Collection)
     */
    public void createAll(ProceedingJoinPoint pjp) throws Throwable
    {
        @SuppressWarnings("unchecked")
        Collection<Locatable> entities = (Collection<Locatable>)pjp.getArgs()[0];

        pjp.proceed();

        if (isCacheable(entities))
        {
            Ehcache cache = null;

            for (Locatable entity : entities)
            {
                if (cache == null)
                {
                    cache = getCache(entity.getClass());
                }
                cache.put(createCacheElement(entity));
            }
        }
    }

    /**
     * Join point for the {@code GenologicsAPI.update} method.
     * Update the object through the API and, if it can be cached, update the record
     * in the cache.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#update(Locatable)
     */
    public void update(ProceedingJoinPoint pjp) throws Throwable
    {
        Locatable entity = (Locatable)pjp.getArgs()[0];

        pjp.proceed();

        if (isCacheable(entity))
        {
            Ehcache cache = getCache(entity.getClass());
            cache.put(createCacheElement(entity));
        }
    }

    /**
     * Join point for the {@code GenologicsAPI.updateAll} method.
     * Update the objects through the API and, if they can be cached, update their records
     * in the cache.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#updateAll(Collection)
     */
    public void updateAll(ProceedingJoinPoint pjp) throws Throwable
    {
        @SuppressWarnings("unchecked")
        Collection<Locatable> entities = (Collection<Locatable>)pjp.getArgs()[0];

        pjp.proceed();

        if (isCacheable(entities))
        {
            Ehcache cache = null;

            for (Locatable entity : entities)
            {
                if (cache == null)
                {
                    cache = getCache(entity.getClass());
                }
                cache.put(createCacheElement(entity));
            }
        }
    }

    /**
     * Join point for the {@code GenologicsAPI.delete} method.
     * Delete the object through the API and, if it can be cached, remove it from the cache.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#delete(Locatable)
     */
    public void delete(ProceedingJoinPoint pjp) throws Throwable
    {
        Locatable entity = (Locatable)pjp.getArgs()[0];

        String key = keyFromLocatable(entity);

        pjp.proceed();

        if (isCacheable(entity))
        {
            Ehcache cache = getCache(entity.getClass());
            cache.remove(key);
        }
    }

    /**
     * Join point for the {@code GenologicsAPI.deleteAll} method.
     * Delete the objects through the API and, if they can be cached, remove their records
     * from the cache.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#deleteAll(Collection)
     */
    public void deleteAll(ProceedingJoinPoint pjp) throws Throwable
    {
        @SuppressWarnings("unchecked")
        Collection<Locatable> entities = (Collection<Locatable>)pjp.getArgs()[0];

        Ehcache cache = null;

        List<String> keys = null;
        if (isCacheable(entities))
        {
            keys = new ArrayList<String>(entities.size());
            for (Locatable entity : entities)
            {
                if (entity != null && entity.getUri() != null)
                {
                    keys.add(keyFromLocatable(entity));

                    if (cache == null)
                    {
                        cache = getCache(entity.getClass());
                    }
                }
            }
        }

        pjp.proceed();

        if (keys != null)
        {
            assert cache != null : "No cache set";
            cache.removeAll(keys);
        }
    }

    /**
     * Join point for the methods that take an object in to perform an operation and
     * return an object as a result (not necessarily object passed in). For example,
     * {@code GenologicsAPI.executeProcess} and {@code GenologicsAPI.beginProcessStep}.
     *
     * <p>Run the operation and store the resulting object in the cache (if cacheable).</p>
     *
     * @param pjp The join point object.
     *
     * @return The object created by the operation.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#executeProcess(com.genologics.ri.processexecution.ExecutableProcess)
     * @see GenologicsAPI#beginProcessStep(com.genologics.ri.step.StepCreation)
     */
    public Locatable runSomething(ProceedingJoinPoint pjp) throws Throwable
    {
        Locatable result = (Locatable)pjp.proceed();

        if (isCacheable(result))
        {
            Ehcache cache = getCache(result.getClass());
            cache.put(createCacheElement(result));
        }

        return result;
    }

    /**
     * Join point for the {@code GenologicsAPI.uploadFile} method.
     * Call through to the API to do the work and store the resulting {@code GenologicsFile}
     * object in the cache.
     *
     * @param pjp The join point object.
     *
     * @return The file record in the Genologics LIMS.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#uploadFile(com.genologics.ri.LimsEntityLinkable, java.net.URL, boolean)
     */
    public GenologicsFile uploadFile(ProceedingJoinPoint pjp) throws Throwable
    {
        GenologicsFile file = (GenologicsFile)pjp.proceed();

        if (isCacheable(file))
        {
            Ehcache cache = getCache(file.getClass());
            cache.put(createCacheElement(file));
        }

        return file;
    }

    /**
     * Join point for the {@code GenologicsAPI.deleteAndRemoveFile} method.
     * Call through to the API to do the work and remove the {@code GenologicsFile}
     * object from the cache.
     *
     * @param pjp The join point object.
     *
     * @throws Throwable if there is an error.
     *
     * @see GenologicsAPI#deleteAndRemoveFile(Linkable)
     */
    public void deleteAndRemoveFile(ProceedingJoinPoint pjp) throws Throwable
    {
        Linkable<?> file = (Linkable<?>)pjp.getArgs()[0];

        pjp.proceed();

        if (isCacheable(file))
        {
            Ehcache cache = getCache(file.getClass());
            cache.remove(keyFromLocatable(file));
        }
    }

    /**
     * Assemble a URI for an object's id and class.
     *
     * <p>
     * Uses the reference to the GenologicsAPI to obtain the current
     * server address.
     * </p>
     *
     * <p>
     * This method essentially replicates {@link GenologicsAPIImpl#limsIdToUri(String, Class)}
     * but is looser on the class object it accepts for entityClass and doesn't create the
     * URI object for the identifier.
     * </p>
     *
     * @param entityClass The class of the entity.
     * @param ids The LIMS id(s) of the entity.
     *
     * @return A URI in string form for the entity.
     */
    protected String toUriString(Class<?> entityClass, String... ids)
    {
        GenologicsEntity entityAnno = entityClass.getAnnotation(GenologicsEntity.class);
        if (entityAnno == null)
        {
            throw new IllegalArgumentException(
                    "The class " + entityClass.getName() + " has not been annotated with the GenologicsEntity annotation.");
        }

        if (api.getServerApiAddress() == null)
        {
            throw new IllegalStateException("Genologics API has not been configured yet.");
        }
        StringBuilder uri = new StringBuilder(api.getServerApiAddress());

        if (entityAnno.primaryEntity() != void.class)
        {
            if (ids.length != 2)
            {
                throw new IllegalArgumentException(
                        entityClass.getName() + " has a single section endpoint in the API. " +
                        "Use load(String, Class) for this type.");
            }

            GenologicsEntity primaryAnno = entityAnno.primaryEntity().getAnnotation(GenologicsEntity.class);
            if (primaryAnno == null)
            {
                throw new IllegalArgumentException(
                        "The class " + entityAnno.primaryEntity().getName() + " has not been annotated with the GenologicsEntity annotation.");
            }

            uri.append(primaryAnno.uriSection()).append('/').append(ids[0]);
            uri.append(entityAnno.uriSection()).append('/').append(ids[1]);
        }
        else
        {
            if (ids.length != 1)
            {
                throw new IllegalArgumentException(
                        entityClass.getName() + " has a double section endpoint in the API. " +
                        "Use load(String, String, Class) for this type.");
            }

            uri.append(entityAnno.uriSection()).append('/').append(ids[0]);
            if (isNotEmpty(entityAnno.uriSubsection()))
            {
                uri.append('/').append(entityAnno.uriSubsection());
            }
        }

        return uri.toString();
    }

    /**
     * Test whether a collection of entities is cacheable. Finds the first
     * non-null item in the list an tests its {@code GenologicsEntity} annotation.
     * Assumes the collection is homogeneous in its content.
     *
     * @param entities The collection to test.
     *
     * @return {@code true} if the first non-null item is the list is cacheable,
     * {@code false} otherwise (including for a null or empty list).
     *
     * @see #isCacheable(Object)
     */
    public boolean isCacheable(Collection<?> entities)
    {
        if (entities != null)
        {
            for (Object thing : entities)
            {
                if (thing != null)
                {
                    return isCacheable(thing);
                }
            }
        }
        return false;
    }

    /**
     * Test whether an entity is cacheable. If the object given is not null,
     * examine its {@code GenologicsEntity} annotation for its "cacheable"
     * attribute and return that value.
     *
     * @param thing The object to test.
     *
     * @return {@code true} if {@code thing} is not null, is annotated with
     * the {@code GenologicsEntity} annotation, and that annotation has its
     * "cacheable" attribute set to true; {@code false} otherwise.
     *
     * @see GenologicsEntity#cacheable()
     */
    public boolean isCacheable(Object thing)
    {
        boolean cacheable = false;
        if (thing != null)
        {
            Class<?> entityClass = thing.getClass();
            if (LimsLink.class.isAssignableFrom(entityClass))
            {
                entityClass = ((LimsLink<?>)thing).getEntityClass();
            }
            cacheable = isCacheable(entityClass);
        }
        return cacheable;
    }

    /**
     * Test whether entities of a class are cacheable. Tests whether the given
     * class is annotated with the {@code GenologicsEntity} annotation and, if
     * so, its "cacheable" attribute is set.
     *
     * @param entityClass The class to test.
     *
     * @return {@code true} if {@code entityClass} is annotated with
     * the {@code GenologicsEntity} annotation, and that annotation has its
     * "cacheable" attribute set to true; {@code false} otherwise.
     *
     * @see GenologicsEntity#cacheable()
     */
    public boolean isCacheable(Class<?> entityClass)
    {
        GenologicsEntity entityAnno = entityClass.getAnnotation(GenologicsEntity.class);
        return entityAnno != null && entityAnno.cacheable();
    }

    /**
     * Test whether a collection of entities is stateful. Finds the first
     * non-null item in the list an tests its {@code GenologicsEntity} annotation.
     * Assumes the collection is homogeneous in its content.
     *
     * @param entities The collection to test.
     *
     * @return {@code true} if the first non-null item is the list is stateful,
     * {@code false} otherwise (including for a null or empty list).
     *
     * @see #isStateful(Object)
     */
    public boolean isStateful(Collection<?> entities)
    {
        if (entities != null)
        {
            for (Object thing : entities)
            {
                if (thing != null)
                {
                    return isStateful(thing);
                }
            }
        }
        return false;
    }

    /**
     * Test whether an entity is stateful. If the object given is not null,
     * examine its {@code GenologicsEntity} annotation for its "stateful"
     * attribute and return that value.
     *
     * @param thing The object to test.
     *
     * @return {@code true} if {@code thing} is not null, is annotated with
     * the {@code GenologicsEntity} annotation, and that annotation has its
     * "stateful" attribute set to true; {@code false} otherwise.
     *
     * @see GenologicsEntity#stateful()
     */
    public boolean isStateful(Object thing)
    {
        boolean stateful = false;
        if (thing != null)
        {
            Class<?> entityClass = thing.getClass();
            if (LimsLink.class.isAssignableFrom(entityClass))
            {
                entityClass = ((LimsLink<?>)thing).getEntityClass();
            }
            stateful = isStateful(entityClass);
        }
        return stateful;
    }

    /**
     * Test whether entities of a class are stateful. Tests whether the given
     * class is annotated with the {@code GenologicsEntity} annotation and, if
     * so, its "stateful" attribute is set.
     *
     * @param entityClass The class to test.
     *
     * @return {@code true} if {@code entityClass} is annotated with
     * the {@code GenologicsEntity} annotation, and that annotation has its
     * "stateful" attribute set to true; {@code false} otherwise.
     *
     * @see GenologicsEntity#stateful()
     */
    public boolean isStateful(Class<?> entityClass)
    {
        GenologicsEntity entityAnno = entityClass.getAnnotation(GenologicsEntity.class);
        return entityAnno != null && entityAnno.stateful();
    }

    /**
     * Create a cache element wrapper for the given entity. Extracts from the entity's URI
     * the cache key as the full path of the entity and, if relevant, the state
     * from the "state" part of the URI's query string.
     *
     * @param entity The entity to create a cache wrapper around.
     *
     * @return A Ehcache Element with the key, state (version) and entity set.
     */
    public Element createCacheElement(Locatable entity)
    {
        String key = keyFromLocatable(entity);
        long version = versionFromLocatable(entity);

        return new Element(key, entity, version);
    }

    /**
     * Extract the state value from the given object's URI, if such an entity can
     * have a state value.
     *
     * @param thing The entity to extract the state for.
     *
     * @return The state number, or {@code NO_STATE_VALUE} if either the object
     * is not a stateful object or there is no state information in the object's URI.
     */
    public long versionFromLocatable(Locatable thing)
    {
        return isStateful(thing) ? versionFromUri(thing.getUri()) : NO_STATE_VALUE;
    }

    /**
     * Extract the state value from a URI.
     *
     * @param uri The URI to dissect for a state value.
     *
     * @return The state number, or {@code NO_STATE_VALUE} if there is no state
     * information in the URI.
     */
    public long versionFromUri(URI uri)
    {
        return uri == null ? NO_STATE_VALUE : versionFromUri(uri.toString());
    }

    /**
     * Extract the state value from a string version of a URI.
     *
     * @param uri The URI string to dissect for a state value.
     *
     * @return The state number, or {@code NO_STATE_VALUE} if there is no state
     * information in the URI.
     */
    public long versionFromUri(String uri)
    {
        long version = NO_STATE_VALUE;
        int query = uri.indexOf('?');
        if (query >= 0)
        {
            int statePosition = uri.indexOf(STATE_TERM, query + 1);
            if (statePosition >= 0)
            {
                version = 0L;
                int length = uri.length();
                for (int i = statePosition + STATE_TERM_LENGTH; i < length; i++)
                {
                    char c = uri.charAt(i);
                    if (Character.isDigit(c))
                    {
                        version = version * 10 + (c - '0');
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        return version;
    }

    /**
     * Extract the cache key value from the given object's URI. This is the
     * full path of the entity, less any query string.
     *
     * @param thing The entity to extract the key for.
     *
     * @return The cache key value.
     */
    public String keyFromLocatable(Locatable thing)
    {
        return thing == null ? null : keyFromUri(thing.getUri());
    }

    /**
     * Extract the cache key value from the given URI. This is the
     * full path of the entity, less any query string.
     *
     * @param uri The URI to extract the key from.
     *
     * @return The cache key value.
     */
    public String keyFromUri(URI uri)
    {
        return uri == null ? null : keyFromUri(uri.toString());
    }

    /**
     * Extract the cache key value from the given URI string. This is the
     * full path of the entity, less any query string.
     *
     * @param uri The URI string to extract the key from.
     *
     * @return The cache key value.
     */
    public String keyFromUri(String uri)
    {
        String key = uri;
        int query = key.indexOf('?');
        if (query > 0)
        {
            key = key.substring(0, query);
        }
        return key;
    }

    /**
     * Get the type of stateful behaviour to use for the current call.
     * If there is an override that requires the exact version, behave
     * on this call as if the cache were in EXACT operation (for the
     * fetch, not the store). Otherwise return the configured behaviour.
     *
     * @return The cache behaviour to use this time.
     */
    protected CacheStatefulBehaviour getBehaviourForCall()
    {
        StatefulOverride override = apiCacheControl.getStatefulOverride();
        if (override == StatefulOverride.EXACT)
        {
            // Behave as if the whole cache was running in EXACT mode.
            return CacheStatefulBehaviour.EXACT;
        }
        return behaviour;
    }

    /**
     * Convenience method to test whether the latest version of stateful
     * entities is required.
     *
     * @return True if there is an override wanting the latest version
     * set on the API, false if not.
     */
    protected boolean isFetchLatestVersions()
    {
        return apiCacheControl.getStatefulOverride() == StatefulOverride.LATEST;
    }

}
