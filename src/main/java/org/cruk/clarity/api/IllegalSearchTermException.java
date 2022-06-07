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
 * Exception raised when a search for entities is provided with search
 * terms that are illegal. Terms can be illegal for a number of reasons:
 *
 * <ol>
 * <li>The term's value is null;</li>
 * <li>The term's value is an empty collection or a zero length array;</li>
 * <li>The term's value is a collection or an array containing a null.</li>
 * </ol>
 *
 * <p>
 * It is important to not ignore missing values as the search will probably
 * not run as intended. Typically it will return more results than one would
 * expect, often drastically so. An example is searching for processes by
 * input artifact ids: if the ids have been collected into a set but on a
 * particular call that set is empty, then all processes ever run will be
 * returned (if that term is just left out of the query, which is how the
 * code worked before release 2.23). This is clearly not a good idea when
 * the intention is to search for a small set.
 * </p>
 *
 * <p>
 * It is still possible to use the {@code find} method with no search terms.
 * In this case, the call is the same as {@link ClarityAPI#listAll(Class)}.
 * </p>
 *
 * @see ClarityAPI#find(java.util.Map, Class)
 * @see ClarityAPI#listQueue(com.genologics.ri.Linkable, java.util.Map)
 *
 * @since 2.23
 */
public class IllegalSearchTermException extends IllegalArgumentException
{
    private static final long serialVersionUID = -1008444394159250460L;

    /**
     * Search term.
     */
    private final String searchTerm;

    /**
     * Construct a new IllegalSearchTermException.
     *
     * @param term The search term that is in error.
     * @param message The message indicating why the term is in error.
     */
    public IllegalSearchTermException(String term, String message)
    {
        super(message);
        searchTerm = term;
    }

    /**
     * Get the search term that caused the error.
     *
     * @return The search term.
     */
    public String getSearchTerm()
    {
        return searchTerm;
    }
}
