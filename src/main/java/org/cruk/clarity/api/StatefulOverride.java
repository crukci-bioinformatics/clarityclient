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

package org.cruk.clarity.api;

/**
 * Enumeration of the ways to override the normal behaviour of the API and
 * the cache to get specific versions of stateful entities.
 *
 * @since 2.24.8
 */
public enum StatefulOverride
{
    /**
     * Fetch the latest version of the stateful entity. This retrieves the
     * entity with any <i>state</i> parameter removed from the URI. It does
     * not look in the cache for the entity, always calling through to the
     * Clarity API. The entity returned is cached.
     */
    LATEST,

    /**
     * Fetch the exact version of a stateful entity, the version as specified
     * by its state parameter. Will be fetched from the cache if the cache
     * happens to contain this exact version, otherwise it will go to the
     * Clarity API. The returned entity may be stored in the cache according
     * to the usual cache rules. If there is a newer version of the entity
     * in the cache already, that will be ignored.
     */
    EXACT;
}
