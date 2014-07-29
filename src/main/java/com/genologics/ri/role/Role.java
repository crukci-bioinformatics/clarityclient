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

package com.genologics.ri.role;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.Linkable;

/**
 *
 * The detailed representation of a Role
 *
 * @since 2.19
 */
@GenologicsEntity(uriSection = "roles", updateable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role", propOrder = { "name", "researchers", "permissions" })
@XmlRootElement(name = "role")
public class Role implements Linkable<Role>, Serializable
{
    private static final long serialVersionUID = 5417064490465819346L;

    @XmlElement
    protected String name;

    @XmlElementWrapper(name = "researchers")
    @XmlElement(name = "researcher")
    protected List<ResearcherLink> researchers;

    @XmlElementWrapper(name = "permissions")
    @XmlElement(name = "permission")
    protected List<PermissionLink> permissions;

    @XmlAttribute(name = "built-in")
    protected Boolean builtIn;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Role()
    {
    }

    public Role(URI uri)
    {
        this.uri = uri;
    }

    public Role(URI uri, String name)
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

    public List<ResearcherLink> getResearchers()
    {
        if (researchers == null)
        {
            researchers = new ArrayList<ResearcherLink>();
        }
        return researchers;
    }

    public List<PermissionLink> getPermissions()
    {
        if (permissions == null)
        {
            permissions = new ArrayList<PermissionLink>();
        }
        return permissions;
    }

    public Boolean getBuiltIn()
    {
        return builtIn;
    }

    public void setBuiltIn(Boolean builtIn)
    {
        this.builtIn = builtIn;
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
