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

package com.genologics.ri.exception;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import org.cruk.clarity.api.ClarityException;
import org.cruk.clarity.api.jaxb.JaxbUnmarshallingAspect;

/**
 *
 * Exception is a descriptive error message that is returned instead of the
 * standard response for any request that the system was unable to process.
 * <p>
 * Exception will include a code corresponding to the HTTP response code of the
 * error. Typical response codes are 400, indicating that the submitted request
 * was not valid and cannot be reattempted without corrective action being
 * taken, or 500 indicating that there was an internal error in the server when
 * attempting to process the request. Refer to the section on HTTP response
 * codes in the API reference documentation for further details about the
 * categories and meanings of HTTP response codes.
 * </p>
 * <p>
 * Exception will include a message element with a textual summary of the error,
 * and may also provide a suggested-actions element with details on how to
 * correct the problem.
 * </p>
 *
 * <p>
 * Note that within the client, these exceptions are converted to true Java
 * {@code ClarityException} automatically.
 * </p>
 *
 * @see ClarityException
 * @see JaxbUnmarshallingAspect
 */
@XmlRootElement(name = "exception")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exception", propOrder = { "message", "suggestedActions" })
public class Exception implements Serializable
{
    private static final long serialVersionUID = 8552752622433159544L;

    @XmlElement(name = "message")
    protected String message;

    @XmlElement(name = "suggested-actions")
    protected String suggestedActions;

    @XmlAttribute(name = "category")
    protected String category;

    @XmlAttribute(name = "code")
    protected String code;

    public Exception()
    {
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getSuggestedActions()
    {
        return suggestedActions;
    }

    public void setSuggestedActions(String suggestedActions)
    {
        this.suggestedActions = suggestedActions;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

}
