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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Exception raised when a {@link URI} or {@link URL} object is created
 * from a string but the string isn't a valid URI or URL.
 *
 * <p>
 * This is a RuntimeException that is used in place of the typed
 * {@link URISyntaxException} or {@link MalformedURLException} from within
 * the Clarity API client. We don't expect these to actually happen if
 * the client is configured properly, so handling them as typed exceptions
 * on every call is burdensome. Prior to release 2.31.2 these errors came
 * out as general {@code IllegalArgumentException}s or in one case an
 * {@code AssertionError}. This class provides a specific and
 * consistent type for such errors that wouldn't be expected from a
 * correctly configured program.
 * </p>
 *
 * @since v2.31.2
 */
public class InvalidURIException extends IllegalArgumentException
{
    private static final long serialVersionUID = 6541624754969322769L;

    /**
     * Construct a new InvalidURIException from a URISyntaxException.
     *
     * @param e The URISyntaxException.
     */
    public InvalidURIException(URISyntaxException e)
    {
        super("Could not create a URI object: " + e.getMessage(), e);
    }

    /**
     * Construct a new InvalidURIException from a URISyntaxException.
     *
     * @param message The message indicating why the term is in error.
     * @param e The URISyntaxException.
     */
    public InvalidURIException(String message, URISyntaxException e)
    {
        super(message + e.getMessage(), e);
    }

    /**
     * Construct a new InvalidURIException from a MalformedURLException.
     *
     * @param e The MalformedURLException.
     */
    public InvalidURIException(MalformedURLException e)
    {
        super("Could not create a URL object: " + e.getMessage(), e);
    }

    /**
     * Construct a new InvalidURIException from a MalformedURLException.
     *
     * @param message The message indicating why the term is in error.
     * @param e The MalformedURLException.
     */
    public InvalidURIException(String message, MalformedURLException e)
    {
        super(message + e.getMessage(), e);
    }
}
