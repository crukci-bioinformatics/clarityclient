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

package com.genologics.ri.container;

import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.configuration.FieldType;
import com.genologics.ri.containertype.ContainerType;
import com.genologics.ri.userdefined.UDF;
import com.genologics.ri.userdefined.UDT;

@ClarityEntity(uriSection = "containers", creatable = true, updateable = true, removable = true)
@XmlRootElement(name = "container")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "container",
         propOrder = { "name", "containerType", "occupiedWells", "placements", "type", "fields", "state" })
public class Container implements LimsEntity<Container>, Serializable
{
    private static final long serialVersionUID = -5274665844559069140L;

    @XmlElement(name = "name")
    protected String name;

    @XmlElement(name = "type")
    protected ContainerTypeLink containerType;

    @XmlElement(name = "occupied-wells")
    protected Long occupiedWells;

    @XmlElement(name = "placement")
    protected List<Placement> placements;

    @XmlElement(name = "type", namespace = UDF_NAMESPACE)
    protected UDT type;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlElement(name = "state")
    protected String state;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public Container()
    {
    }

    public Container(Linkable<ContainerType> containerType)
    {
        setContainerType(containerType);
    }

    public Container(Linkable<ContainerType> containerType, String name)
    {
        setContainerType(containerType);
        setName(name);
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String value)
    {
        this.limsid = value;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ContainerTypeLink getContainerType()
    {
        return containerType;
    }

    public void setContainerType(Linkable<ContainerType> link)
    {
        this.containerType = new ContainerTypeLink(link);
    }

    public Long getOccupiedWells()
    {
        return occupiedWells;
    }

    public void setOccupiedWells(Long occupiedWells)
    {
        this.occupiedWells = occupiedWells;
    }

    public List<Placement> getPlacements()
    {
        if (placements == null)
        {
            placements = new ArrayList<Placement>();
        }
        return placements;
    }

    public void setPlacements(Collection<? extends LimsEntityLinkable<Artifact>> links)
    {
        getPlacements().clear();
        for (LimsEntityLinkable<Artifact> link : links)
        {
            placements.add(new Placement(link));
        }
    }

    public Placement addPlacement(Placement p)
    {
        getPlacements().add(p);
        return p;
    }

    public Placement addPlacement(LimsEntityLinkable<Artifact> artifact, String wellPosition)
    {
        Placement p = new Placement(artifact, wellPosition);
        getPlacements().add(p);
        return p;
    }

    public UDT getUserDefinedType()
    {
        return type;
    }

    public UDT setUserDefinedType(UDT type)
    {
        this.type = type;
        return this.type;
    }

    public UDT setUserDefinedType(String type)
    {
        this.type = new UDT(type);
        return this.type;
    }

    public List<UDF> getUserDefinedFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<UDF>();
        }
        return fields;
    }

    public UDF getUserDefinedField(String name)
    {
        return UDF.getUDF(fields, name);
    }

    public UDF addUserDefinedField(UDF udf)
    {
        getUserDefinedFields().add(udf);
        return udf;
    }

    public UDF addUserDefinedField(String name, FieldType type, String value)
    {
        return addUserDefinedField(new UDF(name, type, value));
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(limsid);
        if (containerType != null)
        {
            sb.append(' ').append(containerType.getName());
        }
        return sb.toString();
    }
}
