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

package com.genologics.ri.stepconfiguration;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;

/**
 *
 * Step-setup is a child element that describes the shared result file placeholder information for the step-setup view.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "step-setup")
public class StepSetup extends LockableSetting
{
    private static final long serialVersionUID = 3706069288148584102L;

    @XmlElementWrapper(name = "files")
    @XmlElement(name = "file")
    protected List<SharedResultFile> files;

    @XmlAttribute(name = "enabled", required = true)
    protected boolean enabled;

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

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
