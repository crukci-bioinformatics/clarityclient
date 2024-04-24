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
import com.genologics.ri.Locatable;
import com.genologics.ri.Namespaces;
import com.genologics.ri.file.ClarityFile;

/**
 * The automation element integrates the process with the Automation Worker
 * (previously called External Program Plugin or EPP). When a user runs the
 * process, the system automatically issue a command, or submits files and
 * scripts to third-party programs for further processing.
 *
 * @since 2.26
 */
@ClarityEntity(uriSection = "configuration/automations")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "automation",
         propOrder = { "context", "script", "runProgramPerEvent", "channel", "files", "processTypes" })
@XmlRootElement(name = "automation")
public class Automation implements Locatable, Serializable
{
    private static final long serialVersionUID = 8888968017373728807L;

    @XmlElement
    protected Context context;

    @XmlElement(name = "string")
    protected String script;

    @XmlElement(name = "run-program-per-event")
    protected Boolean runProgramPerEvent;

    @XmlElement
    protected String channel;

    @XmlElement(namespace = Namespaces.FILE_NAMESPACE, name = "file")
    protected List<ClarityFile> files;

    @XmlElementWrapper(name = "process-types")
    @XmlElement(name = "process-type")
    protected List<ProcessTypeLink> processTypes;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;


    public Automation()
    {
    }

    public Context getContext()
    {
        return context;
    }

    public void setContext(Context value)
    {
        this.context = value;
    }

    public String getScript()
    {
        return script;
    }

    public void setScript(String script)
    {
        this.script = script;
    }

    public Boolean isRunProgramPerEvent()
    {
        return runProgramPerEvent;
    }

    public void setRunProgramPerEvent(Boolean value)
    {
        this.runProgramPerEvent = value;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String value)
    {
        this.channel = value;
    }

    public List<ClarityFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<>();
        }
        return files;
    }

    public List<ProcessTypeLink> getProcessTypes()
    {
        if (processTypes == null)
        {
            processTypes = new ArrayList<>();
        }
        return processTypes;
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

    public void setName(String value)
    {
        this.name = value;
    }
}
