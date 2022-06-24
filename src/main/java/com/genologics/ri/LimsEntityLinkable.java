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

package com.genologics.ri;


/**
 * Interface for entities and links to entities that are identifiable in the
 * system (i.e. have a LIMS id).
 *
 * @param <E> The type of entity that is at the end of the link.
 */
public interface LimsEntityLinkable<E extends LimsEntity<E>> extends Linkable<E>
{
    /**
     * Get the LIMS id for this object.
     *
     * @return The LIMS id.
     */
    String getLimsid();

    /**
     * Set the LIMS id for this object.
     *
     * @param id The new LIMS id.
     */
    void setLimsid(String id);

    /**
     * Same as {@code getLimsid()} but written in a more pleasing form for camel case.
     *
     * @return The LIMS id.
     *
     * @since 2.31.1
     */
    default String getLimsId()
    {
        return getLimsid();
    }

    /**
     * Same as {@code setLimsid(String)} but written in a more pleasing form for camel case.
     *
     * @param id The new LIMS id.
     *
     * @since 2.31.1
     */
    default void setLimsId(String id)
    {
        setLimsid(id);
    }
}
