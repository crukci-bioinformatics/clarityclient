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
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;

/**
 * @since 2.18
 */
@ClarityEntity(uriSection = "steps", uriSubsection = "setup")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setup", propOrder = { "step", "configuration", "files" })
@XmlRootElement(name = "setup")
public class StepSetup implements Linkable<StepSetup>, Serializable
{
    private static final long serialVersionUID = 1161115098255552451L;

    @XmlElement
    protected Link step;

    @XmlElement
    protected StepConfiguration configuration;

    @XmlElementWrapper(name = "files")
    @XmlElement(name = "file")
    protected List<SharedResultFile> files;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public StepSetup()
    {
    }

    public StepSetup(URI uri)
    {
        this.uri = uri;
    }

    public Link getStep()
    {
        return step;
    }

    public void setStep(Link step)
    {
        this.step = step;
    }

    public StepConfiguration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(StepConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public List<SharedResultFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<SharedResultFile>();
        }
        return files;
    }

    public SharedResultFile addFile(SharedResultFile file)
    {
        getFiles().add(file);
        return file;
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

}
