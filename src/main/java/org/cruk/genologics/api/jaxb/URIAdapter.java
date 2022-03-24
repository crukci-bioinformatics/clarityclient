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

package org.cruk.genologics.api.jaxb;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * Convert to and from URI objects, correctly encoding the query string.
 * Can also remove the "state" parameter from the query part of a URI string
 * before converting to a URI object, or when converting back.
 * Turns out this is probably a bad idea after all, so it has been disabled.
 */
public class URIAdapter extends XmlAdapter<String, URI>
{
    /**
     * UTF-8 character set for URL encoding.
     */
    private static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * Pattern for removing "state=" terms in the query string.
     */
    private static final Pattern STATE_PATTERN = Pattern.compile("([\\?&]?)state=\\d+(&?)");

    /**
     * Flag indicating whether the state removing behaviour should be used.
     */
    private static final boolean REMOVE_STATE = false;


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
        return v == null ? null : new URI(escapeString(v));
    }

    /**
     * Convert the given URI into its string format.
     *
     * @param v The URI to print.
     *
     * @return The URI as a string, or null if {@code v} is null.
     */
    @Override
    public String marshal(URI v)
    {
        String s = null;
        if (v != null)
        {
            s = v.toString();
            if (REMOVE_STATE)
            {
                s = removeStateParameter(s);
            }
        }
        return s;
    }

    /**
     * Escape a string before conversion to a URI and, according to setting,
     * remove the state parameter.
     *
     * @param v The URI in string form.
     *
     * @return The encoded URI.
     */
    private String escapeString(String v)
    {
        if (v != null)
        {
            int queryStart = v.indexOf('?');
            if (queryStart >= 0)
            {
                List<NameValuePair> queryParts = URLEncodedUtils.parse(v.substring(queryStart + 1), UTF8);
                removeStateParameter(queryParts);

                v = v.substring(0, queryStart);
                if (!queryParts.isEmpty())
                {
                    v += '?' + URLEncodedUtils.format(queryParts, UTF8);
                }
            }
        }
        return v;
    }

    /**
     * Look for a parameter called "state" and remove it from the list if
     * it is found.
     *
     * @param terms The name value pair terms.
     */
    private void removeStateParameter(List<NameValuePair> terms)
    {
        if (REMOVE_STATE)
        {
            Iterator<NameValuePair> iter = terms.iterator();
            while (iter.hasNext())
            {
                if ("state".equals(iter.next().getName()))
                {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Remove any "state=" parameter from the given URI.
     *
     * @param uri The URI to modify.
     *
     * @return A new URI which is {@code uri} without the state information.
     */
    public static URI removeStateParameter(URI uri)
    {
        if (uri != null)
        {
            try
            {
                uri = new URI(removeStateParameter(uri.toString()));
            }
            catch (URISyntaxException e)
            {
                throw new AssertionError("Removing state information from URI " + uri + " failed: " + e.getMessage());
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
     */
    public static String removeStateParameter(String uri)
    {
        if (uri != null)
        {
            StringBuffer builder = new StringBuffer();

            Matcher m = STATE_PATTERN.matcher(uri);
            while (m.find())
            {
                String replacement = "";

                if (StringUtils.isNotEmpty(m.group(2)))
                {
                    // There are more options after the state parameter.
                    // Need to put the first question mark or ampersand back.
                    replacement = m.group(1);
                }

                m.appendReplacement(builder, replacement);
            }
            m.appendTail(builder);

            uri = builder.toString();
        }

        return uri;
    }
}
