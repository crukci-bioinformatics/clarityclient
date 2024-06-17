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

/**
 * Options for how the API cache handles entities that are stateful.
 *
 * @since 2.22
 */
public enum CacheStatefulBehaviour
{
    /**
     * The version of the entity in the cache must match exactly,
     * otherwise it is fetched from the server.
     */
    EXACT,

    /**
     * The entity is fetched if the version in the cache is older
     * than the version in the URI. If the same, newer, or the URI
     * has no state version, the cached version is returned.
     */
    LATEST,

    /**
     * Only fetch entities if they are not in the cache. The state
     * version is ignored.
     */
    ANY;
}
