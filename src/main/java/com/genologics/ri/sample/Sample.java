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

package com.genologics.ri.sample;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Location;
import com.genologics.ri.container.Container;

/**
 * The detailed representation of a sample.
 */
@ClarityEntity(uriSection = "samples", creationClass = SampleCreation.class, creatable = true, updateable = true)
@XmlRootElement(name = "sample")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sample")
public class Sample extends SampleBase implements LimsEntity<Sample>
{
    private static final long serialVersionUID = 5852545940907097947L;

    /**
     * Location for a new sample to be created in a container.
     * This field is not serialized or marshalled to XML or sent
     * (in this form) to the Clarity server.
     */
    //@XmlTransient
    private transient Location creationLocation;

    /**
     * Get the initial creation location for this sample.
     *
     * @return The initial location.
     */
    public Location getCreationLocation()
    {
        return creationLocation;
    }

    /**
     * Set the location of this sample for creation. This is required when
     * creating samples via the API only.
     *
     * @param creationLocation The initial location of the sample.
     */
    public void setCreationLocation(Location creationLocation)
    {
        this.creationLocation = creationLocation;
    }

    /**
     * Set the location of this sample for creation. This is required when
     * creating samples via the API only.
     *
     * @param container The container the sample will be created in.
     * @param position The position within the container for the sample.
     */
    public void setCreationLocation(LimsEntityLinkable<Container> container, String position)
    {
        this.creationLocation = new Location(container, position);
    }


}
