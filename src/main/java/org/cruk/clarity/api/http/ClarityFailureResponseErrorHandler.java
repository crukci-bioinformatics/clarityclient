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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBElement;

import org.cruk.clarity.api.ClarityException;
import org.cruk.clarity.api.jaxb.JaxbUnmarshallingAspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * REST client error handler that looks at the response body when there is a failure
 * code (an HTTP 400 or 500 code) and sees if the body contains a Clarity
 * {@code exception} message. If it does, that is thrown instead.
 *
 * <p>This handler relies on the exception conversion aspect around the JAXB
 * unmarshaller.</p>
 *
 * @see JaxbUnmarshallingAspect
 */
@SuppressWarnings("exports")
// Can't declare this as a component as it causes a circular bean dependency.
public class ClarityFailureResponseErrorHandler extends DefaultResponseErrorHandler
{
    /**
     * The unmarshaller.
     */
    private Unmarshaller unmarshaller;

    /**
     * Empty constructor.
     */
    public ClarityFailureResponseErrorHandler()
    {
    }

    /**
     * Constructor with the unmarshaller.
     *
     * @param unmarshaller The Jaxb Unmarshaller.
     */
    public ClarityFailureResponseErrorHandler(Unmarshaller unmarshaller)
    {
        setUnmarshaller(unmarshaller);
    }

    /**
     * Set the unmarshaller used to create the XML.
     *
     * @param unmarshaller The Jaxb Unmarshaller.
     */
    public void setUnmarshaller(Unmarshaller unmarshaller)
    {
        this.unmarshaller = unmarshaller;
    }

    /**
     * Refinement of the default implementation, as we want anything that's not a
     * successful error code (in the 200 series) to be classed as a failure.
     *
     * @param statusCode The HTTP status code
     * @return {@code true} if the response is not in the 200 series of HTTP codes.
     */
    @Override
    protected boolean hasError(HttpStatusCode statusCode)
    {
        return !statusCode.is2xxSuccessful();
    }

    /**
     * See if the response indicates an error and, if the body of the response is
     * a Clarity error reply, create a throwable {@code ClarityException} from
     * it and throw that.
     * <p>Normally this test should be redundant, as the {@code ClarityExceptionAspect}
     * should automatically take care of creating throwable {@code ClarityException}s from
     * the response.</p>
     *
     * @param response The HTTP response.
     *
     * @throws IOException if there is a problem reading from the response.
     * @throws ClarityException if the response is found to be a Clarity exception.
     *
     * @see JaxbUnmarshallingAspect
     */
    @Override
    @SuppressWarnings("exports")
    public void handleError(ClientHttpResponse response) throws IOException
    {
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.is4xxClientError() || statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
        {
            // Try and decode the message body. If it forms a Clarity exception,
            // throw that. Otherwise fall through.

            byte[] body = getResponseBody(response);

            try
            {
                Object content = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(body)));

                // Should be handled by the ClarityExceptionAspect, but in case
                // that isn't in place...

                if (content instanceof JAXBElement<?> element)
                {
                    content = element.getValue();
                }
                if (content instanceof com.genologics.ri.exception.Exception ge)
                {
                    throw new ClarityException(ge, statusCode);
                }
            }
            catch (ClarityException e)
            {
                e.setHttpStatus(statusCode);
                throw e;
            }
            catch (Exception e)
            {
                // Ignore, and behave as before.
            }
        }

        // If still here, it's not a ClarityException that has been received, and so
        // continue as before.
        super.handleError(response);
    }
}
