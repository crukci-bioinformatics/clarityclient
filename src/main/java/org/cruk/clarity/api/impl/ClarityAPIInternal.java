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

package org.cruk.clarity.api.impl;

import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.StatefulOverride;

import com.genologics.ri.Batch;
import com.genologics.ri.LimsLink;
import com.genologics.ri.Locatable;


/**
 * Interface to the Clarity API for internal use, mostly by the cache.
 *
 * <p>
 * This interface provides the methods required for internal control of the API from
 * aspects that surround the API.
 * </p>
 *
 * <p>
 * In earlier versions this was a stand alone interface. To aid proxying and to keep
 * only one bean in the context, this has been changed to be an extension of the
 * main public interface.
 * </p>
 *
 * @see ClarityAPI
 *
 * @since 2.24.9
 */
public interface ClarityAPIInternal extends ClarityAPI
{
    /**
     * Helper method for the cache, this method returns whether the next call
     * on the current thread will need to fetch stateful entities in a special
     * way or not.
     *
     * @return The rule for the next call to the API for stateful entities.
     * Will return null if there is no override.
     */
    StatefulOverride getStatefulOverride();

    /**
     * Undoes the effects of {@link ClarityAPI#overrideStateful(StatefulOverride)}
     * after a call to ensure subsequent calls will respect the state parameter for
     * stateful entities. Called by the wrapping aspect after any public method to make
     * sure the behaviour is reset.
     *
     * @param calledMethod The name of the method that caused this to be called.
     * This is provided by the surrounding join point. It is not important and
     * only used for debug logging.
     *
     * @see ClarityAPI#overrideStateful(StatefulOverride)
     */
    void cancelStatefulOverride(String calledMethod);

    /**
     * Get the class that holds a list of links for the given entity when returned
     * from a list or search operation.
     *
     * <p>For example, this is the {@link com.genologics.ri.artifact.Artifacts}
     * class for the {@link com.genologics.ri.artifact.Artifact} entity.
     * </p>
     *
     * @param entityClass The class of the entity.
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the object that holds the list of links to these entities.
     *
     * @return The list of links class for the given class of entity.
     *
     * @throws IllegalArgumentException if {@code entityClass} has no associated
     * list of links class.
     */
    <E extends Locatable, BH extends Batch<? extends LimsLink<E>>>
    Class<BH> getQueryResultsClassForEntity(Class<E> entityClass);
}
