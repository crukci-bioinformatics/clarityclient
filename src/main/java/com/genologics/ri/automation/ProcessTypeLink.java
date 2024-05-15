/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2018 Cancer Research UK Cambridge Institute.
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

package com.genologics.ri.automation;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.beanutils.PropertyUtils;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.processtype.ProcessType;

/**
 * Provides links to process types the automation is enabled on.
 *
 * @since 2.26
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process-type")
public class ProcessTypeLink implements LimsLink<ProcessType>, Serializable
{
    private static final long serialVersionUID = 1189833674343873526L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;


    public ProcessTypeLink()
    {
    }

    public ProcessTypeLink(URI uri)
    {
        this.uri = uri;
    }

    public ProcessTypeLink(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

    public void setProcessType(Linkable<ProcessType> processType)
    {
        uri = processType == null ? null : processType.getUri();
        name = null;
        try
        {
            name = (String)PropertyUtils.getProperty(processType, "name");
        }
        catch (Exception e)
        {
            // Ignore everything - it's only an attempt.
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    @Override
    public Class<ProcessType> getEntityClass()
    {
        return ProcessType.class;
    }

    @Override
    public String toString()
    {
        return name == null ? "null" : name;
    }
}
