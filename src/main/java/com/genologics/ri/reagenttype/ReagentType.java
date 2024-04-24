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

package com.genologics.ri.reagenttype;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Link;

/**
 *
 * The detailed representation of a reagent type.
 */
@ClarityEntity(uriSection = "reagenttypes")
@XmlRootElement(name = "reagent-type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagent-type", propOrder = { "specialType", "reagentCategory" })
public class ReagentType implements LimsEntity<ReagentType>, Serializable
{
    private static final long serialVersionUID = -7390347729777321516L;

    @XmlElement(name = "special-type")
    protected SpecialType specialType;

    @XmlElement(name = "reagent-category")
    protected String reagentCategory;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public ReagentType()
    {
    }

    public ReagentType(URI uri)
    {
        this.uri = uri;
    }

    public ReagentType(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public SpecialType getSpecialType()
    {
        return specialType;
    }

    public void setSpecialType(SpecialType specialType)
    {
        this.specialType = specialType;
    }

    public String getReagentCategory()
    {
        return reagentCategory;
    }

    public void setReagentCategory(String reagentCategory)
    {
        this.reagentCategory = reagentCategory;
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
    public String getLimsid()
    {
        return Link.limsIdFromUri(uri);
    }

    @Override
    public void setLimsid(String id)
    {
        // Does nothing.
    }

    @Override
    public String toString()
    {
        ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        b.append("id", getLimsid());
        b.append("category", reagentCategory);
        b.append("name", name);
        return b.toString();
    }
}
