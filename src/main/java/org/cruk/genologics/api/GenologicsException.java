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

package org.cruk.genologics.api;

import org.apache.commons.lang3.StringUtils;
import org.cruk.genologics.api.jaxb.JaxbUnmarshallingAspect;
import org.springframework.http.HttpStatus;


/**
 * Exception decoded when a response from an API call gives an exception
 * object. The fields from the unmarshalled object are transferred into
 * one of these, which is then thrown.
 *
 * @see com.genologics.ri.exception.Exception
 * @see JaxbUnmarshallingAspect
 */
public class GenologicsException extends RuntimeException
{
    private static final long serialVersionUID = 404952616991787238L;

    protected String suggestedActions;

    protected String category;

    protected String code;

    protected HttpStatus httpStatus;

    /**
     * Initialise from the given API exception.
     *
     * @param ge The Exception unmarshalled from the XML response.
     */
    public GenologicsException(com.genologics.ri.exception.Exception ge)
    {
        super(ge.getMessage());
        suggestedActions = ge.getSuggestedActions();
        category = ge.getCategory();
        code = ge.getCode();
    }

    /**
     * Initialise from the given API exception with an HTTP status code.
     *
     * @param ge The Exception unmarshalled from the XML response.
     * @param httpStatus The return code from the HTTP call.
     */
    public GenologicsException(com.genologics.ri.exception.Exception ge, HttpStatus httpStatus)
    {
        this(ge);
        this.httpStatus = httpStatus;
    }

    /**
     * Get the "suggested actions" text.
     *
     * @return The suggested actions text.
     */
    public String getSuggestedActions()
    {
        return suggestedActions;
    }

    /**
     * Get the error category.
     *
     * @return The error category.
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * Get the error code.
     *
     * @return The error code.
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Get the HTTP status code.
     *
     * @return The HTTP status code returned from the server.
     */
    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    /**
     * Set the HTTP status code.
     *
     * @param httpStatus The HTTP status code.
     */
    public void setHttpStatus(HttpStatus httpStatus)
    {
        this.httpStatus = httpStatus;
    }

    /**
     * Convenience method to test whether this exception is because something
     * is not found.
     *
     * @return true if the status is not found (HTTP 404), false otherwise.
     *
     * @since 2.24.14
     */
    public boolean isNotFound()
    {
        return httpStatus == HttpStatus.NOT_FOUND;
    }

    /**
     * Convenience method absorb this exception if it is because something
     * is not found, otherwise rethrow itself. This is designed to be used
     * inside catch blocks where it is possible to ask for an entity by id
     * and handle it not being there (so a speculative GET).
     *
     * @throws GenologicsException (this) if the reason for this exception
     * is for any reason other than something not being found.
     *
     * @since 2.24.14
     */
    public void throwUnlessNotFound()
    {
        if (!isNotFound())
        {
            throw this;
        }
    }

    /**
     * Provide a printable representation of this exception. Prints the
     * main error message and, if present, the HTTP status code and the
     * suggested actions.
     *
     * @return A printable string for this exception.
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder(super.toString());
        if (httpStatus != null)
        {
            str.append(" [HTTP ").append(httpStatus.value()).append(']');
        }
        if (StringUtils.isNotBlank(suggestedActions))
        {
            str.append(' ').append(suggestedActions);
        }
        return str.toString();
    }
}
