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

package com.genologics.ri.reagentlot;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Linkable;
import com.genologics.ri.reagentkit.ReagentKit;
import com.genologics.ri.researcher.Researcher;

/**
 *
 * The detailed representation of a Reagent Lot
 *
 * @since 2.18
 */
@GenologicsEntity(uriSection = "reagentlots", creatable = true, updateable = true)
@XmlRootElement(name = "reagent-lot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagent-lot",
         propOrder = { "reagentKit", "name", "lotNumber", "createdDate", "lastModifiedDate",
                       "expiryDate", "createdBy", "lastModifiedBy", "storageLocation", "notes",
                       "status", "usageCount" })
public class ReagentLot implements LimsEntity<ReagentLot>, Serializable
{
    private static final long serialVersionUID = 8414977581247533239L;

    @XmlElement(name = "reagent-kit")
    protected ReagentKitLink reagentKit;

    @XmlElement
    protected String name;

    @XmlElement(name = "lot-number")
    protected String lotNumber;

    @XmlElement(name = "created-date")
    @XmlSchemaType(name = "date")
    protected Date createdDate;

    @XmlElement(name = "last-modified-date")
    @XmlSchemaType(name = "date")
    protected Date lastModifiedDate;

    @XmlElement(name = "expiry-date")
    @XmlSchemaType(name = "date")
    protected Date expiryDate;

    @XmlElement(name = "created-by")
    protected ResearcherLink createdBy;

    @XmlElement(name = "last-modified-by")
    protected ResearcherLink lastModifiedBy;

    @XmlElement(name = "storage-location")
    protected String storageLocation;

    @XmlElement
    protected String notes;

    @XmlElement
    protected Status status;

    /**
     * @since 2.20
     */
    @XmlElement(name = "usage-count")
    protected Long usageCount;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;


    public ReagentLot()
    {
    }

    public ReagentLot(URI uri)
    {
        this.uri = uri;
    }

    public ReagentLot(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public ReagentLot(LimsEntityLinkable<ReagentLot> link)
    {
        uri = link.getUri();
        limsid = link.getLimsid();
    }

    public ReagentKitLink getReagentKit()
    {
        return reagentKit;
    }

    public void setReagentKit(Linkable<ReagentKit> reagentKit)
    {
        this.reagentKit = new ReagentKitLink(reagentKit);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLotNumber()
    {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber)
    {
        this.lotNumber = lotNumber;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public ResearcherLink getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Linkable<Researcher> createdBy)
    {
        this.createdBy = new ResearcherLink(createdBy);
    }

    public ResearcherLink getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Linkable<Researcher> lastModifiedBy)
    {
        this.lastModifiedBy = new ResearcherLink(lastModifiedBy);
    }

    public String getStorageLocation()
    {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation)
    {
        this.storageLocation = storageLocation;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public Long getUsageCount()
    {
        return usageCount;
    }

    public void setUsageCount(Long usageCount)
    {
        this.usageCount = usageCount;
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

    @Override
    public String getLimsid()
    {
        return limsid;
    }

    @Override
    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(limsid).append(' ').append(name);
        return sb.toString();
    }
}
