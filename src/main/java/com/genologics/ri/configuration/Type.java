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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.Batch;
import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;

/**
 * The detailed representation of the configuration of a user defined type.
 */
@ClarityEntity(uriSection = "configuration/udts")
@XmlRootElement(name = "type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type", propOrder = { "fieldDefinitions", "attachToName", "attachToCategory" })
public class Type implements Linkable<Type>, Batch<FieldLink>, Serializable
{
    private static final long serialVersionUID = 6975337629090720371L;

    @XmlElement(name = "field-definition")
    protected List<FieldLink> fieldDefinitions;

    @XmlElement(name = "attach-to-name")
    protected String attachToName;

    @XmlElement(name = "attach-to-category")
    protected String attachToCategory;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    protected URI uri;

    public List<FieldLink> getFieldDefinitions()
    {
        if (fieldDefinitions == null)
        {
            fieldDefinitions = new ArrayList<FieldLink>();
        }
        return this.fieldDefinitions;
    }

    @Override
    public Iterator<FieldLink> iterator()
    {
        return getFieldDefinitions().iterator();
    }

    @Override
    public List<FieldLink> getList()
    {
        return getFieldDefinitions();
    }

    @Override
    public int getSize()
    {
        return getFieldDefinitions().size();
    }

    public String getAttachToName()
    {
        return attachToName;
    }

    public void setAttachToName(String value)
    {
        this.attachToName = value;
    }

    public String getAttachToCategory()
    {
        return attachToCategory;
    }

    public void setAttachToCategory(String value)
    {
        this.attachToCategory = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

}
