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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 *
 * Identifies the resource that represents the program status for the step.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "program-status-link")
public class ProgramStatusLink implements LimsLink<ProgramStatus>, Serializable
{
    private static final long serialVersionUID = 1364271013485882177L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public ProgramStatusLink()
    {
    }

    public ProgramStatusLink(URI uri)
    {
        this.uri = uri;
    }

    public ProgramStatusLink(Linkable<ProgramStatus> link)
    {
        this.uri = link.getUri();
    }

    @Override
    public Class<ProgramStatus> getEntityClass()
    {
        return ProgramStatus.class;
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
