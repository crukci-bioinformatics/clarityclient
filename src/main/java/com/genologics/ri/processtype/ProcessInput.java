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

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 *
 * Process-input is a child element of process type specifying the available
 * input types for the process
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process-input", propOrder = { "artifactType", "displayName", "removeWorkingFlag" })
public class ProcessInput implements Serializable
{
    private static final long serialVersionUID = -2733188258573850043L;

    @XmlElement(name = "artifact-type")
    protected String artifactType;

    @XmlElement(name = "display-name")
    protected String displayName;

    @XmlElement(name = "remove-working-flag")
    @Deprecated
    protected Boolean removeWorkingFlag;

    public String getArtifactType()
    {
        return artifactType;
    }

    public void setArtifactType(String artifactType)
    {
        this.artifactType = artifactType;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Whether the working flag should be removed from the input when the process runs.
     *
     * @return true if it should be removed, false if not.
     * @deprecated This property is no longer supported.
     */
    @Deprecated
    public Boolean isRemoveWorkingFlag()
    {
        return removeWorkingFlag;
    }

    @Deprecated
    public void setRemoveWorkingFlag(Boolean removeWorkingFlag)
    {
        this.removeWorkingFlag = removeWorkingFlag;
    }

}
