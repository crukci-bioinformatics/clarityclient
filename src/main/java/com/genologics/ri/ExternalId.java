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

package com.genologics.ri;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

/**
 *
 * An external id is a reference to an identifier in an external system that
 * contains additional information about a representation within the API.
 * <p>
 * External id is supported on representations that contain links back to
 * external systems.
 * </p>
 * <p>
 * External id consists of two different URI type elements: id and URI. ID is
 * the URI referencing the external system, and provides context and
 * identification of the representation within that system. URI is the URI
 * within the system, and provides a means of looking up the representation that
 * the external id is associated with.
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "externalid")
public class ExternalId implements Locatable, Serializable
{
    private static final long serialVersionUID = -6268759708863731052L;

    @XmlAttribute(name = "id")
    @XmlSchemaType(name = "anyURI")
    protected URI id;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public ExternalId()
    {
    }

    public ExternalId(URI id)
    {
        this.id = id;
    }

    public ExternalId(URI id, URI uri)
    {
        this.id = id;
        this.uri = uri;
    }

    public URI getId()
    {
        return id;
    }

    public void setId(URI id)
    {
        this.id = id;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

}
