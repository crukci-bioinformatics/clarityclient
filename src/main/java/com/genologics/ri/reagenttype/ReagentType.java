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

package com.genologics.ri.reagenttype;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.Locatable;

/**
 *
 * The detailed representation of a reagent type.
 */
@GenologicsEntity(uriSection = "reagenttypes")
@XmlRootElement(name = "reagent-type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagent-type", propOrder = { "specialType", "reagentCategory" })
public class ReagentType implements Locatable, Serializable
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

}
