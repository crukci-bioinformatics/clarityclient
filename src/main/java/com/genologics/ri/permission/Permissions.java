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

package com.genologics.ri.permission;

import java.io.Serializable;
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
@ClarityQueryResult(entityClass = Permission.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permissions")
@XmlRootElement(name = "permissions")
public class Permissions implements Batch<PermissionLink>, Serializable
{
    private static final long serialVersionUID = -8403111885816697046L;

    @XmlElement(name = "permission")
    protected List<PermissionLink> permissions;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected String uri;


    public Permissions()
    {
    }

    public Permissions(String uri)
    {
        this.uri = uri;
    }

    public List<PermissionLink> getPermissions()
    {
        if (permissions == null)
        {
            permissions = new ArrayList<PermissionLink>();
        }
        return permissions;
    }

    @Override
    public Iterator<PermissionLink> iterator()
    {
        return getPermissions().iterator();
    }

    @Override
    public List<PermissionLink> getList()
    {
        return getPermissions();
    }

    @Override
    public int getSize()
    {
        return permissions == null ? 0 : permissions.size();
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

}
