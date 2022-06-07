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

import com.genologics.ri.ClarityEntity;

/**
 * Exception thrown when an attempt is made to change an object that
 * cannot be altered in such a manner through the Clarity API.
 * This covers any of create, update or delete.
 *
 * @see ClarityEntity
 */
public class ClarityUpdateException extends RuntimeException
{
    private static final long serialVersionUID = -2679585761524242033L;

    /**
     * Initialise with no details.
     */
    public ClarityUpdateException()
    {
    }

    /**
     * Initialise with an error message.
     *
     * @param message The error message.
     */
    public ClarityUpdateException(String message)
    {
        super(message);
    }

}
