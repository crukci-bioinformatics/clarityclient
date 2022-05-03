/**
 * Package supporting caching of Clarity entities in memory
 * (or, if so configured, on disk) to reduce the amount of traffic being
 * sent to and from the REST API.
 *
 * <p>
 * At present, the "stateful" objects (specifically, the {@code Artifact} class)
 * produces some problems. See the documentation for {@link org.cruk.clarity.api.cache.ClarityAPICache}
 * for further information.
 * </p>
 *
 * <p>
 * The cache is implemented using EhCache. To make use of it, you need to include
 * the file {@code org/cruk/clarity/api/clarity-cache-context.xml}
 * in your Spring application context path. Its individual caches are configured with
 * the file {@code org/cruk/clarity/api/ehcache.xml}. The default version of
 * this file is packaged with the JAR, so if you need to change it create a replacement
 * that is before this project's JAR file on the class path.
 * </p>
 *
 * <p>
 * If you do not use the cache, you can
 * exclude it from this project's dependencies in Maven:
 * </p>
 *
 * <pre>
 *     &lt;dependency&gt;
 *         &lt;groupId&gt;org.cruk.clarity&lt;/groupId&gt;
 *         &lt;artifactId&gt;clarity-client&lt;/artifactId&gt;
 *         &lt;version&gt;...&lt;/version&gt;
 *         &lt;exclusions&gt;
 *             &lt;exclusion&gt;
 *                 &lt;groupId&gt;org.ehcache&lt;/groupId&gt;
 *                 &lt;artifactId&gt;ehcache&lt;/artifactId&gt;
 *             &lt;/exclusion&gt;
 *         &lt;/exclusions&gt;
 *     &lt;/dependency&gt;
 * </pre>
 *
 * @see <a href="https://ehcache.org/">https://ehcache.org/</a>
 */
package org.cruk.clarity.api.cache;
