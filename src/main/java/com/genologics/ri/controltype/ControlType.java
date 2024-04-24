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

package com.genologics.ri.controltype;

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

@ClarityEntity(uriSection = "controltypes", creatable = true, updateable = true)
@XmlRootElement(name = "control-type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "control-type",
         propOrder = { "supplier", "catalogueNumber", "website", "concentration", "archived", "singleStep" })
public class ControlType implements Linkable<ControlType>, Serializable
{
    private static final long serialVersionUID = -3152986669595427342L;

    @XmlElement
    protected String supplier;

    @XmlElement(name = "catalogue-number")
    protected String catalogueNumber;

    @XmlSchemaType(name = "anyURI")
    protected String website;

    /**
     * @since 2.20
     */
    @XmlElement
    protected String concentration;

    @XmlElement
    protected Boolean archived;

    @XmlElement(name = "single-step")
    protected Boolean singleStep;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;


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

    public String getConcentration()
    {
        return concentration;
    }

    public void setConcentration(String concentration)
    {
        this.concentration = concentration;
    }

    public Boolean getArchived()
    {
        return archived;
    }

    public void setArchived(Boolean archived)
    {
        this.archived = archived;
    }

    public Boolean getSingleStep()
    {
        return singleStep;
    }

    public void setSingleStep(Boolean singleStep)
    {
        this.singleStep = singleStep;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
