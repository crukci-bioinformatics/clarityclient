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

package org.cruk.genologics.api.http;

import java.net.URI;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * Client HTTP request factory adding basic authentication to the request.
 *
 * @see <a href="http://www.baeldung.com/2012/04/16/how-to-use-resttemplate-with-basic-authentication-in-spring-3-1/#automatic_auth">This
 * post about adding basic authentication to REST template.</a>
 *
 * @since 2.23.1
 */
public class HttpComponentsClientHttpRequestFactoryBasicAuth extends HttpComponentsClientHttpRequestFactory
implements AuthenticatingClientHttpRequestFactory
{
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Authentication scope object for the credentials provider.
     * Contains the HttpHost specification.
     */
    private AuthScope authenticationScope;

    /**
     * The authentication credentials.
     */
    private Credentials credentials;

    /**
     * Basic authentication scheme object.
     */
    private AuthScheme basicAuthenticationScheme = new BasicScheme();

    /**
     * Authentication cache.
     */
    private AuthCache authenticationCache = new BasicAuthCache();

    /**
     * Http client context. This provides the combination of the
     * authentication objects and the credentials provider.
     */
    private HttpClientContext context = HttpClientContext.create();
    {
        context.setAuthCache(authenticationCache);
    }


    /**
     * Constructor.
     */
    public HttpComponentsClientHttpRequestFactoryBasicAuth()
    {
        super();
    }

    /**
     * Constructor with an HTTP client.
     *
     * @param client The underlying HTTP client.
     */
    public HttpComponentsClientHttpRequestFactoryBasicAuth(HttpClient client)
    {
        super(client);
    }

    /**
     * Constructor with an HTTP client and credentials provider.
     *
     * @param client The underlying HTTP client.
     * @param credentialsProvider The authentication credentials provider.
     */
    public HttpComponentsClientHttpRequestFactoryBasicAuth(HttpClient client, CredentialsProvider credentialsProvider)
    {
        super(client);
        setCredentialsProvider(credentialsProvider);
    }

    /**
     * Get the credentials provider used by this factory.
     *
     * @return The authentication credentials provider.
     */
    public CredentialsProvider getCredentialsProvider()
    {
        return context.getCredentialsProvider();
    }

    /**
     * Set the credentials provider to use.
     *
     * @param credentialsProvider The authentication credentials provider.
     */
    @Required
    public void setCredentialsProvider(CredentialsProvider credentialsProvider)
    {
        context.setCredentialsProvider(credentialsProvider);
    }

    /**
     * Provide the HTTP context set up for basic authentication by this
     * factory. Despite the method being called "create", the same
     * {@code HttpContext} can be returned each time.
     *
     * @param httpMethod The HTTP method.
     * @param uri The request URI.
     *
     * @return The HttpContext.
     */
    @Override
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri)
    {
        return context;
    }

    @Override
    public HttpHost getHost()
    {
        return authenticationScope == null ? null : authenticationScope.getOrigin();
    }

    @Override
    public Credentials getCredentials()
    {
        return credentials;
    }

    @Override
    public void setCredentials(URL url, Credentials credentials)
    {
        if (url == null)
        {
            throw new IllegalArgumentException("url cannot be set to null");
        }

        setCredentials(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()), credentials);
    }

    @Override
    public void setCredentials(HttpHost host, Credentials credentials)
    {
        if (host == null)
        {
            throw new IllegalArgumentException("host cannot be set to null");
        }

        authenticationScope = new AuthScope(host);

        authenticationCache.put(host, basicAuthenticationScheme);

        this.credentials = credentials;

        if (credentials != null)
        {
            getCredentialsProvider().setCredentials(authenticationScope, credentials);
        }
    }
}
