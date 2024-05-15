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

package com.genologics.ri.configuration;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.beanutils.PropertyUtils;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 * Type-definition provides a URI linking to the user defined type associated
 * with the user-defined field, if applicable.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-definition")
public class TypeDefinition implements LimsLink<Type>, Serializable
{
    private static final long serialVersionUID = 7368802583367288257L;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public TypeDefinition()
    {
    }

    public TypeDefinition(URI uri)
    {
        this.uri = uri;
    }

    public TypeDefinition(URI uri, String name)
    {
        this.name = name;
        this.uri = uri;
    }

    public TypeDefinition(Linkable<Type> link)
    {
        uri = link.getUri();
        try
        {
            this.name = (String)PropertyUtils.getProperty(link, "name");
        }
        catch (Exception e)
        {
            // Ignore.
        }
    }

    @Override
    public Class<Type> getEntityClass()
    {
        return Type.class;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
