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

package com.genologics.ri.role;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;

/**
 *
 * Role-link is a child element type of roles and provides a URI linking to the
 * detailed representation of a Role.
 *
 * @since 2.19
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role-link")
public class RoleLink implements LimsLink<Role>, Serializable
{
    private static final long serialVersionUID = -6515590246139208584L;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public RoleLink()
    {
    }

    public RoleLink(URI uri)
    {
        this.uri = uri;
    }

    public RoleLink(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    @Override
    public Class<Role> getEntityClass()
    {
        return Role.class;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
