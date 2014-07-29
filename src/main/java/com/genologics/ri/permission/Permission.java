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

package com.genologics.ri.permission;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.Linkable;

/**
 * @since 2.19
 */
@GenologicsEntity(uriSection = "permissions")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permission", propOrder = { "name", "action", "description" })
@XmlRootElement(name = "permission")
public class Permission implements Linkable<Permission>, Serializable
{
    private static final long serialVersionUID = 5000872708185118382L;

    protected String name;

    protected String action;

    protected String description;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public Permission()
    {
    }

    public Permission(URI uri)
    {
        this.uri = uri;
    }

    public Permission(URI uri, String name)
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

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    @Override
    public void setUri(URI uri)
    {
        this.uri = uri;
    }

}
