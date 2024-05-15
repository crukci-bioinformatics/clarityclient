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

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

/**
 * A protocol step setting is considered locked if the Master Step has defined
 * that setting.
 *
 * @since 2.25
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lockable-setting")
@XmlSeeAlso({ StepSetup.class, EppTrigger.class, Field.class,
              ControlTypeLink.class, ReagentKitLink.class, StepProperty.class })
public class LockableSetting implements Serializable
{
    private static final long serialVersionUID = 1977151383272159438L;

    @XmlAttribute(name = "locked")
    protected Boolean locked;


    public LockableSetting()
    {
    }

    public Boolean isLocked()
    {
        return locked;
    }

    public void setLocked(Boolean value)
    {
        this.locked = value;
    }

}
