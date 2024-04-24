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

package com.genologics.ri.reagentkit;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;

/**
 *
 * The detailed representation of a reagent kit.
 * @since 2.18
 */
@ClarityEntity(uriSection = "reagentkits", creatable = true, updateable = true)
@XmlRootElement(name = "reagent-kit")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagent-kit", propOrder = { "name", "supplier", "catalogueNumber", "website", "archived" })
public class ReagentKit implements Linkable<ReagentKit>, Serializable
{
    private static final long serialVersionUID = 7467244485974104986L;

    @XmlElement
    protected String name;

    @XmlElement
    protected String supplier;

    @XmlElement(name = "catalogue-number")
    protected String catalogueNumber;

    @XmlSchemaType(name = "anyURI")
    protected String website;

    @XmlElement
    protected Boolean archived;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public ReagentKit()
    {
    }

    public ReagentKit(URI uri)
    {
        this.uri = uri;
    }

    public ReagentKit(URI uri, String name)
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

    public String getSupplier()
    {
        return supplier;
    }

    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
    }

    public String getCatalogueNumber()
    {
        return catalogueNumber;
    }

    public void setCatalogueNumber(String catalogueNumber)
    {
        this.catalogueNumber = catalogueNumber;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public Boolean getArchived()
    {
        return archived;
    }

    public void setArchived(Boolean archived)
    {
        this.archived = archived;
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
