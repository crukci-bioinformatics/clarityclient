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

package org.cruk.clarity.api.http;

import java.net.URL;

import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.http.client.ClientHttpRequestFactory;

/**
 * Extension of the ClientHttpRequestFactory interface to allow
 * authentication to be used with the REST client.
 *
 * @since 2.23.1
 */
@SuppressWarnings("exports")
public interface AuthenticatingClientHttpRequestFactory extends ClientHttpRequestFactory
{
    /**
     * Get the host to which the credentials will be applied.
     *
     * @return The HttpHost specification to which the authentication applies.
     */
    HttpHost getHost();

    /**
     * Get the credentials currently in use.
     *
     * @return The credentials.
     */
    Credentials getCredentials();

    /**
     * Set the credentials based on the protocol, host and port in the URL.
     *
     * @param url The URL of the HTTP server.
     * @param credentials The credentials to use, or null to reset.
     *
     * @throws IllegalArgumentException if {@code url} is null.
     */
    void setCredentials(URL url, Credentials credentials);

    /**
     * Set the credentials based on the HTTP host specification.
     *
     * @param host The host specification of the HTTP server.
     * @param credentials The credentials to use, or null to reset.
     *
     * @throws IllegalArgumentException if {@code host} is null.
     */
    void setCredentials(HttpHost host, Credentials credentials);
}
