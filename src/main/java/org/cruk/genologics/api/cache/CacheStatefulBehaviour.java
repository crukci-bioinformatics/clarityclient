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

/**
 * Options for how the API cache handles entities that are stateful.
 *
 * @since 2.22
 */
public enum CacheStatefulBehaviour
{
    /**
     * Fetch the latest version of the entity, using no state version
     * at all. This will always cause the entity to be fetched again,
     * so should only be used in very specific cases. The entity
     * returned replaces the version in the cache, if there is one.
     *
     * @since 2.24.8
     */
    UP_TO_DATE,

    /**
     * The version of the entity in the cache must match exactly,
     * otherwise it is fetched from the server.
     */
    EXACT,

    /**
     * The entity is fetched if the version in the cache is older
     * than the version in the URI. If the same, newer, or the URI
     * has no state version, the cached version is returned.
     *
     * @since 2.24.8
     */
    NEWER,

    /**
     * The entity is fetched if the version in the cache is older
     * than the version in the URI. If the same, newer, or the URI
     * has no state version, the cached version is returned.
     *
     * @deprecated This is the original name for {@code NEWER}.
     * {@code NEWER} should be used in preference, as it's more accurate.
     */
    LATEST,

    /**
     * Only fetch entities if they are not in the cache. The state
     * version is ignored.
     */
    ANY;
}
