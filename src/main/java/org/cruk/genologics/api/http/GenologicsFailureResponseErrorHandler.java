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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.cruk.genologics.api.GenologicsException;
import org.cruk.genologics.api.jaxb.JaxbUnmarshallingAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * REST client error handler that looks at the response body when there is a failure
 * code (an HTTP 400 or 500 code) and sees if the body contains a Genologics
 * {@code exception} message. If it does, that is thrown instead.
 * <p>This handler relies on the exception conversion aspect around the JAXB2
 * unmarshaller.</p>
 *
 * @see JaxbUnmarshallingAspect
 */
public class GenologicsFailureResponseErrorHandler extends DefaultResponseErrorHandler
{
    /**
     * The marshaller.
     */
    private Jaxb2Marshaller marshaller;

    /**
     * Empty constructor.
     */
    public GenologicsFailureResponseErrorHandler()
    {
    }

    /**
     * Set the marshaller used to create the XML.
     *
     * @param marshaller The Jaxb2Marshaller.
     */
    @Autowired
    @Qualifier("genologicsJaxbMarshaller")
    public void setMarshaller(Jaxb2Marshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    /**
     * Refinement of the default implementation, as we want anything that's not a
     * successful error code (in the 200 series) to be classed as a failure.
     *
     * @param statusCode The HTTP status code
     * @return {@code true} if the response is not in the 200 series of HTTP codes.
     */
    @Override
    protected boolean hasError(HttpStatus statusCode)
    {
        return statusCode.series() != HttpStatus.Series.SUCCESSFUL;
    }

    /**
     * See if the response indicates an error and, if the body of the response is
     * a Genologics error reply, create a throwable {@code GenologicsException} from
     * it and throw that.
     * <p>Normally this test should be redundant, as the {@code GenologicsExceptionAspect}
     * should automatically take care of creating throwable {@code GenologicsException}s from
     * the response.</p>
     *
     * @param response The HTTP response.
     *
     * @throws IOException if there is a problem reading from the response.
     * @throws GenologicsException if the response is found to be a Genologics exception.
     *
     * @see JaxbUnmarshallingAspect
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException
    {
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.is4xxClientError() || statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
        {
            // Try and decode the message body. If it forms a Genologics exception,
            // throw that. Otherwise fall through.

            byte[] body = getResponseBody(response);

            try
            {
                Object content = marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(body)));

                // Should be handled by the GenologicsExceptionAspect, but in case
                // that isn't in place...

                if (content instanceof com.genologics.ri.exception.Exception)
                {
                    throw new GenologicsException((com.genologics.ri.exception.Exception)content, statusCode);
                }
            }
            catch (GenologicsException e)
            {
                e.setHttpStatus(statusCode);
                throw e;
            }
            catch (Exception e)
            {
                // Ignore, and behave as before.
            }
        }

        // If still here, it's not a GenologicsException that has been received, and so
        // continue as before.
        super.handleError(response);
    }
}
