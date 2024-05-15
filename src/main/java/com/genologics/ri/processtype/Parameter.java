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

package com.genologics.ri.processtype;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.file.ClarityFile;

/**
 *
 * The parameter element integrates the process with the External Program
 * Integration plug-in (EPP). When a user runs the process, the system
 * automatically issue a command, or submits files and scripts to third-party
 * programs for further processing. The parameter element is a child element of
 * process-type.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parameter", propOrder = { "file", "script", "runProgramPerEvent", "channel", "invocationType", "files" })
public class Parameter implements Serializable
{
    private static final long serialVersionUID = -803288179230938758L;

    @XmlElement(name = "file")
    @Deprecated
    protected String file;

    /**
     * The EPP script text that will actually run.
     */
    @XmlElement(name = "string")
    protected String script;

    @XmlElement(name = "run-program-per-event")
    @Deprecated
    protected Boolean runProgramPerEvent;

    @XmlElement(name = "channel")
    protected String channel;

    @XmlElement(name = "invocation-type")
    @Deprecated
    protected InvocationType invocationType;

    /**
     * @since 2.26
     */
    @XmlElement(name = "file", namespace = FILE_NAMESPACE)
    protected List<ClarityFile> files;

    @XmlAttribute(name = "name")
    protected String name;


    /**
     * The file of the parameter.
     * @return The file name (presumably).
     * @deprecated This property is no longer supported.
     */
    @Deprecated
    public String getFile()
    {
        return file;
    }

    @Deprecated
    public void setFile(String file)
    {
        this.file = file;
    }

    public String getScript()
    {
        return script;
    }

    public void setScript(String script)
    {
        this.script = script;
    }

    /**
     * Should the EPP run once for each process related event?
     * @return true if it should run once per event, false if not.
     * @deprecated This property is no longer supported.
     */
    @Deprecated
    public Boolean isRunProgramPerEvent()
    {
        return runProgramPerEvent;
    }

    @Deprecated
    public void setRunProgramPerEvent(Boolean runProgramPerEvent)
    {
        this.runProgramPerEvent = runProgramPerEvent;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * The epp invocation type of this script.
     * @return The invocation type.
     * @deprecated This property is no longer supported.
     */
    @Deprecated
    public InvocationType getInvocationType()
    {
        return invocationType;
    }

    @Deprecated
    public void setInvocationType(InvocationType invocationType)
    {
        this.invocationType = invocationType;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Each File provides a URI linking to the detailed representation of a File associated with the Process Parameter.
     *
     * @return A list of files.
     *
     * @since 2.26
     */
    public List<ClarityFile> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<>();
        }
        return files;
    }
}
