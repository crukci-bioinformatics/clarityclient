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

package com.genologics.ri.protocolconfiguration;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.stepconfiguration.ProtocolStep;

/**
 *
 * <p>
 * Detailed representation of a protocol.
 * </p>
 * <p>
 * A protocol represents a collection of different steps. A protocol can be a QC
 * protocol meaning samples go through the process mostly in parallel or a
 * normal protocol, where samples go through in series.
 * </p>
 */
@ClarityEntity(uriSection = "configuration/protocols")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "protocol", propOrder = { "steps", "protocolProperties" })
@XmlRootElement(name = "protocol")
public class Protocol implements Linkable<Protocol>, Serializable
{
    /**
     * Regular expression to extract protocol id from a protocol URI.
     *
     * @since 2.22
     */
    public static final Pattern ID_EXTRACTOR_PATTERN;

    private static final long serialVersionUID = -8872729950204682764L;

    @XmlElementWrapper(name = "steps")
    @XmlElement(name = "step")
    protected List<ProtocolStep> steps;

    @XmlElementWrapper(name = "protocol-properties")
    @XmlElement(name = "protocol-property")
    protected List<ProtocolProperty> protocolProperties;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "index")
    protected Integer index;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    static
    {
        ClarityEntity anno = Protocol.class.getAnnotation(ClarityEntity.class);

        StringBuilder b = new StringBuilder();
        b.append("^.*/").append(anno.uriSection()).append("/(\\d+)$");

        ID_EXTRACTOR_PATTERN = Pattern.compile(b.toString());
    }

    public Protocol()
    {
    }

    public Protocol(URI uri)
    {
        this.uri = uri;
    }

    public Protocol(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public Protocol(URI uri, String name, Integer index)
    {
        this.uri = uri;
        this.name = name;
        this.index = index;
    }

    /**
     * Get the numeric identifier for this protocol from its URI.
     *
     * @return The protocol id, or null if either the URI is not set
     * or it doesn't match the form expected for a protocol URI.
     *
     * @since 2.22
     */
    public Integer getId()
    {
        Integer id = null;
        if (uri != null)
        {
            Matcher m = ID_EXTRACTOR_PATTERN.matcher(uri.toString());
            if (m.matches())
            {
                id = Integer.valueOf(m.group(1));
            }
        }
        return id;
    }

    public List<ProtocolStep> getSteps()
    {
        if (steps == null)
        {
            steps = new ArrayList<ProtocolStep>();
        }
        return steps;
    }

    public List<ProtocolProperty> getProtocolProperties()
    {
        if (protocolProperties == null)
        {
            protocolProperties = new ArrayList<ProtocolProperty>();
        }
        return protocolProperties;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getIndex()
    {
        return index;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
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
