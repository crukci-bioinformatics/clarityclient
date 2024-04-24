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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.AbstractResource;

/**
 * Resource implementation based around a URL. Can be used for any source where
 * the URL protocol is supported by the JVM.
 *
 * @since 2.23
 */
public class URLInputStreamResource extends AbstractResource
{
    /**
     * The URL.
     */
    private final URL url;

    /**
     * The URI.
     */
    private final URI uri;

    /**
     * The connection to the resource.
     */
    private URLConnection connection;

    /**
     * HTTP connection to the resource, if the URL was an HTTP or HTTPS URL.
     */
    private HttpURLConnection httpConnection;

    /**
     * Local file object if the URL was a file URL.
     */
    private File file;

    /**
     * The input stream to read the content of the resource.
     */
    private InputStream inputStream;

    /**
     * Flag indicating that the resource exists.
     */
    private boolean exists = true;


    /**
     * Create a new resource around the given URL.
     *
     * @param url The URL of the resource.
     *
     * @throws IOException if the resource cannot be accessed or the
     * URL does not allow connection.
     */
    public URLInputStreamResource(URL url) throws IOException
    {
        this(url, 8192);
    }

    /**
     * Create a new resource around the given URL.
     *
     * @param url The URL of the resource.
     * @param bufferSize The size of the buffer around the URL's input
     * stream. If this is zero or less, no buffer will be put in place.
     *
     * @throws IOException if the resource cannot be accessed or the
     * URL does not allow connection.
     */
    public URLInputStreamResource(URL url, int bufferSize) throws IOException
    {
        if (url == null)
        {
            throw new IllegalArgumentException("url cannot be null");
        }

        this.url = url;

        try
        {
            uri = url.toURI();
        }
        catch (URISyntaxException e)
        {
            throw new MalformedURLException(e.getMessage());
        }

        connection = url.openConnection();

        inputStream = connection.getInputStream();
        if (bufferSize > 0)
        {
            inputStream = new BufferedInputStream(inputStream, bufferSize);
        }

        try
        {
            httpConnection = (HttpURLConnection)connection;

            exists = httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
        }
        catch (ClassCastException e)
        {
            // Fine, not HTTP.
        }

        if ("file".equals(url.getProtocol()))
        {
            try
            {
                file = new File(url.toURI());
            }
            catch (URISyntaxException e)
            {
                throw new MalformedURLException(e.getMessage());
            }
            catch (IllegalArgumentException e)
            {
                throw new FileNotFoundException(url + " cannot be resolved to a local file path.");
            }
        }
    }

    /**
     * Close the resource's input stream and disconnect the URL connection.
     */
    public void close()
    {
        try
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        catch (IOException e)
        {
            // Ignore.
        }

        if (httpConnection != null)
        {
            httpConnection.disconnect();
        }
    }

    /**
     * Get a file object for the URL.
     *
     * @return The file object for the resource if the URL was a local file
     * resource, null if the URL is of any other type.
     */
    @Override
    public File getFile()
    {
        return file;
    }

    /**
     * Get the file name of the resource.
     *
     * @return The resource's file name.
     */
    @Override
    public String getFilename()
    {
        return FilenameUtils.getName(url.getPath());
    }

    /**
     * Check whether the resource exists. For some protocols this can be
     * done explicitly, but for others this test cannot take place and
     * it is assumed the resource does exist.
     *
     * @return true if the resource exists, false if not.
     */
    @Override
    public boolean exists()
    {
        if (file != null)
        {
            exists = file.exists();
        }

        return exists;
    }

    /**
     * Get the URL of the resource.
     *
     * @return The resource URL.
     */
    @Override
    public URL getURL()
    {
        return url;
    }

    /**
     * Get the URI of the resource.
     *
     * @return The resource URI.
     */
    @Override
    public URI getURI()
    {
        return uri;
    }

    /**
     * Get a description of the resource. For these types, this is the URL
     * in string form.
     *
     * @return The URL as a string.
     */
    @Override
    public String getDescription()
    {
        return url.toExternalForm();
    }

    /**
     * Get the input stream to read the resource content. Client code should
     * call {@link #close()} to close this stream and associated resources,
     * but if the stream is closed explicitly it should be fine as long as this
     * object is not used any further.
     *
     * @return An input stream.
     */
    @Override
    public InputStream getInputStream()
    {
        return inputStream;
    }

    /**
     * Get the size of the resource.
     *
     * @return The size in bytes of the resource. Will return -1 if this cannot
     * be determined.
     */
    @Override
    public long contentLength()
    {
        if (!exists)
        {
            return -1;
        }

        return connection.getContentLength();
    }

    /**
     * Get the last modified time of the resource.
     *
     * @return The last modified time in milliseconds.
     *
     * @see File#lastModified()
     */
    @Override
    public long lastModified()
    {
        return connection.getLastModified();
    }

    /**
     * Test for equality. Two URLInputStreamResource objects are considered
     * equal if they point to the same resource.
     *
     * @param obj The object to test against.
     *
     * @return true if obj is another URLInputStreamResource and both it and
     * this have the same URL.
     */
    @Override
    public boolean equals(Object obj)
    {
        boolean equal = obj == this;
        if (!equal)
        {
            if (obj != null && getClass().equals(obj.getClass()))
            {
                URLInputStreamResource other = (URLInputStreamResource)obj;
                equal = url.equals(other.url);
            }
        }
        return equal;
    }

    /**
     * Get a hash code for this URLInputStreamResource.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode()
    {
        return url.hashCode();
    }

    /**
     * Print this resource in a human readable manner.
     *
     * @return The class name and the URL.
     */
    @Override
    public String toString()
    {
        return "URLInputStreamResource[" + url + "]";
    }
}
