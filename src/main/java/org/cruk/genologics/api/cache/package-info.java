/**
 * Package supporting caching of Genologics entities in memory
 * (or, if so configured, on disk) to reduce the amount of traffic being
 * sent to and from the Genologics REST API.
 *
 * <p>
 * At present, the "stateful" objects (specifically, the {@code Artifact} class)
 * produces some problems. See the documentation for {@link org.cruk.genologics.api.cache.GenologicsAPICache}
 * for further information.
 * </p>
 *
 * <p>
 * The cache is implemented using Ehcache. To make use of it, you need to include
 * the file {@code org/cruk/genologics/api/genologics-cache-context.xml}
 * in your Spring application context path. Its individual caches are configured with
 * the file {@code org/cruk/genologics/api/ehcache.xml}. The default version of
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
 *         &lt;groupId&gt;org.cruk.genologics&lt;/groupId&gt;
 *         &lt;artifactId&gt;clarity-client&lt;/artifactId&gt;
 *         &lt;version&gt;...&lt;/version&gt;
 *         &lt;exclusions&gt;
 *             &lt;exclusion&gt;
 *                 &lt;groupId&gt;net.sf.ehcache&lt;/groupId&gt;
 *                 &lt;artifactId&gt;ehcache-core&lt;/artifactId&gt;
 *             &lt;/exclusion&gt;
 *         &lt;/exclusions&gt;
 *     &lt;/dependency&gt;
 * </pre>
 *
 * @see <a href="http://ehcache.org/">http://ehcache.org/</a>
 */
package org.cruk.genologics.api.cache;
