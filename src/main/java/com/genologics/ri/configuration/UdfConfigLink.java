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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;

/**
 *
 * Udfconfig-link is a child element type of udfs and provides a URI linking to
 * the detailed representation of the configuration for a user defined field.
 */
@XmlRootElement(name = "udfconfig")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "udfconfig-link")
public class UdfConfigLink implements LimsLink<Field>, Serializable
{
    private static final long serialVersionUID = -164190204904044681L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "attach-to-name")
    protected String attachToName;

    @XmlAttribute(name = "attach-to-category")
    protected String attachToCategory;

    public UdfConfigLink()
    {
    }

    public UdfConfigLink(URI uri)
    {
        this.uri = uri;
    }

    public UdfConfigLink(Field realField)
    {
        uri = realField.getUri();
        name = realField.getName();
        attachToName = realField.getAttachToName();
        attachToCategory = realField.getAttachToCategory();
    }

    @Override
    public Class<Field> getEntityClass()
    {
        return Field.class;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public String getAttachToName()
    {
        return attachToName;
    }

    public void setAttachToName(String attachToName)
    {
        this.attachToName = attachToName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAttachToCategory()
    {
        return attachToCategory;
    }

    public void setAttachToCategory(String attachToCategory)
    {
        this.attachToCategory = attachToCategory;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
