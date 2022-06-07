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

package com.genologics.ri.instrument;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntity;

/**
 * The detailed representation of an instrument.
 */
@ClarityEntity(uriSection = "instruments")
@XmlRootElement(name = "instrument")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrument", propOrder = { "name", "type", "serialNumber", "expiryDate", "archived" })
public class Instrument implements LimsEntity<Instrument>, Serializable
{
    private static final long serialVersionUID = 1517557403395348841L;

    protected String name;

    protected String type;

    /**
     * @since 2.25
     */
    @XmlElement(name = "serial-number")
    protected String serialNumber;

    /**
     * @since 2.25
     */
    @XmlElement(name = "expiry-date")
    protected String expiryDate;

    /**
     * @since 2.25
     */
    protected Boolean archived;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    /**
     * @since 2.25
     */
    @XmlAttribute(name = "limsid")
    protected String limsid;

    public Instrument()
    {
    }

    public Instrument(String name)
    {
        this.name = name;
    }

    public Instrument(String name, String type)
    {
        this.name = name;
        this.type = type;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public Boolean getArchived()
    {
        return archived;
    }

    public void setArchived(Boolean archived)
    {
        this.archived = archived;
    }
}
