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

package com.genologics.ri.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 *
 * The root element for a routing API call
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "routing", propOrder = { "assignedRoutes", "unassignedRoutes" })
@XmlRootElement(name = "routing")
public class Routing implements Serializable
{
    private static final long serialVersionUID = 4810146731548918326L;

    @XmlElement(name = "assign")
    protected List<ExtArtifactAssignments> assignedRoutes;

    @XmlElement(name = "unassign")
    protected List<ExtArtifactAssignments> unassignedRoutes;

    public List<ExtArtifactAssignments> getAssign()
    {
        if (assignedRoutes == null)
        {
            assignedRoutes = new ArrayList<ExtArtifactAssignments>();
        }
        return assignedRoutes;
    }

    public List<ExtArtifactAssignments> getUnassign()
    {
        if (unassignedRoutes == null)
        {
            unassignedRoutes = new ArrayList<ExtArtifactAssignments>();
        }
        return unassignedRoutes;
    }

}
