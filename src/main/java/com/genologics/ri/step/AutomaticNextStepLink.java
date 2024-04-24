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


package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;


/**
 * Identifies the resource that represents the step that was automatically begun when the
 * "Automatically start Next Step" option is enabled in a step's configuration.
 *
 * @since 2.22
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "automatic-next-step-link")
public class AutomaticNextStepLink implements LimsLink<ProcessStep>, Serializable
{
    private static final long serialVersionUID = -7710114187126292590L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public AutomaticNextStepLink()
    {
    }

    public AutomaticNextStepLink(URI uri)
    {
        setUri(uri);
    }

    public AutomaticNextStepLink(Linkable<ProcessStep> link)
    {
        setUri(link.getUri());
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
    public Class<ProcessStep> getEntityClass()
    {
        return ProcessStep.class;
    }
}
