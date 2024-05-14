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

import static com.genologics.ri.Namespaces.STEP_CONFIGURATION_NAMESPACE;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * EPP trigger configuration for the Protocol Step.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = STEP_CONFIGURATION_NAMESPACE, name = "epp-trigger")
public class EppTrigger extends LockableSetting
{
    private static final long serialVersionUID = 8850458905996420686L;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "type")
    protected TriggerType type;

    @XmlAttribute(name = "point")
    protected TriggerPoint point;

    @XmlAttribute(name = "status")
    protected TriggerStatus status;


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public TriggerType getType()
    {
        return type;
    }

    public void setType(TriggerType type)
    {
        this.type = type;
    }

    public TriggerPoint getPoint()
    {
        return point;
    }

    public void setPoint(TriggerPoint point)
    {
        this.point = point;
    }

    public TriggerStatus getStatus()
    {
        return status;
    }

    public void setStatus(TriggerStatus status)
    {
        this.status = status;
    }


}
