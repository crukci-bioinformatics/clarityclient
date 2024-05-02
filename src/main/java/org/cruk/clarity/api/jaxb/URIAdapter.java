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

package org.cruk.clarity.api.jaxb;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.cruk.clarity.api.InvalidURIException;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Convert to and from URI objects, correctly encoding the query string.
 * Can also remove the "state" parameter from the query part of a URI string
 * before converting to a URI object, or when converting back.
 * Turns out this is probably a bad idea after all, so it has been disabled.
 */
public class URIAdapter extends XmlAdapter<String, URI>
{
    /**
     * Flag indicating whether the state removing behaviour should be used.
     */
    private final static boolean REMOVE_STATE = false;


    public URIAdapter()
    {
    }

    /**
     * Convert a string into a URI.
     *
     * @param v The string to convert.
     *
     * @return A {@code URI} object for the string, or null if {@code v} is null.
     *
     * @throws URISyntaxException if the string cannot be parsed.
     */
    @Override
    public URI unmarshal(String v) throws URISyntaxException
    {
        if (REMOVE_STATE)
        {
            return v == null ? null : removeState(new URIBuilder(v)).build();
        }
        else
        {
            return v == null ? null : new URI(v);
        }
    }

    /**
     * Convert the given URI into its string format.
     *
     * @param v The URI to print.
     *
     * @return The URI as a string, or null if {@code v} is null.
     *
     * @throws URISyntaxException if the URI cannot be parsed.
     */
    @Override
    public String marshal(URI v) throws URISyntaxException
    {
        if (REMOVE_STATE)
        {
            return v == null ? null : removeState(new URIBuilder(v)).toString();
        }
        else
        {
            return v == null ? null : v.toString();
        }
    }

    /**
     * Remove the state parameter from a URI builder.
     *
     * @param builder The builder to manipulate.
     *
     * @return A reference to {@code builder}.
     */
    private static URIBuilder removeState(URIBuilder builder)
    {
        if (!builder.isQueryEmpty())
        {
            List<NameValuePair> queryParts = builder.getQueryParams();

            Iterator<NameValuePair> iter = queryParts.iterator();
            while (iter.hasNext())
            {
                if ("state".equals(iter.next().getName()))
                {
                    iter.remove();
                }
            }

            builder.setParameters(queryParts);
        }
        return builder;
    }

    /**
     * Remove any "state" parameter from the given URI.
     *
     * @param uri The URI to modify.
     *
     * @return A new URI which is {@code uri} without the state information.
     *
     * @throws InvalidURIException if the URI cannot be manipulated.
     */
    @SuppressWarnings("unused")
    public static URI removeStateParameter(URI uri)
    {
        if (uri != null)
        {
            try
            {
                uri = removeState(new URIBuilder(uri)).build();
            }
            catch (URISyntaxException e)
            {
                throw new InvalidURIException("Removing state information from URI " + uri + " failed.", e);
            }
        }
        return uri;
    }

    /**
     * Remove any "state=" parameter from the given URI.
     *
     * @param uri The URI to modify.
     *
     * @return A string which is {@code uri} without the state information.
     *
     * @throws InvalidURIException if the URI cannot be manipulated.
     */
    @SuppressWarnings("unused")
    public static String removeStateParameter(String uri)
    {
        if (uri != null)
        {
            try
            {
                uri = removeState(new URIBuilder(uri)).toString();
            }
            catch (URISyntaxException e)
            {
                throw new InvalidURIException("Removing state information from URI " + uri + " failed.", e);
            }
        }

        return uri;
    }
}
