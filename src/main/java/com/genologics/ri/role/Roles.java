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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.Batch;
import com.genologics.ri.ClarityQueryResult;

/**
 * @since 2.19
 */
@ClarityQueryResult(entityClass = Role.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roles")
@XmlRootElement(name = "roles")
public class Roles implements Batch<RoleLink>, Serializable
{
    private static final long serialVersionUID = -8997233724026640778L;

    @XmlElement(name = "role")
    protected List<RoleLink> roles;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Roles()
    {
    }

    public Roles(URI uri)
    {
        this.uri = uri;
    }

    public List<RoleLink> getRoles()
    {
        if (roles == null)
        {
            roles = new ArrayList<RoleLink>();
        }
        return roles;
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
    public Iterator<RoleLink> iterator()
    {
        return getRoles().iterator();
    }

    @Override
    public List<RoleLink> getList()
    {
        return getRoles();
    }

    @Override
    public int getSize()
    {
        return roles == null ? 0 : roles.size();
    }

}
