package org.cruk.genologics.api.http;

import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.Credentials;
import org.springframework.http.client.ClientHttpRequestFactory;

/**
 * Extension of the ClientHttpRequestFactory interface to allow
 * authentication to be used with the REST client.
 *
 * @since 2.23.1
 */
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
