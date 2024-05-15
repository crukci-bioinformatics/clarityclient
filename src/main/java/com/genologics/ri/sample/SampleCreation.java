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

import org.cruk.clarity.api.ClarityAPI;

import com.genologics.ri.Location;

/**
 * Sample creation is the detailed representation required when creating a new
 * Sample in the system.
 *
 * <p>
 * For this API, this class should not be used directly. Create
 * {@code Sample} objects and set their {@code creationLocation} field
 * for the initial location before calling {@code ClarityAPI.create}
 * methods.
 * </p>
 *
 * @see Sample#setCreationLocation(Location)
 * @see ClarityAPI#create(com.genologics.ri.Locatable)
 */
@XmlRootElement(name = "samplecreation")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "samplecreation", propOrder = { "location" })
public class SampleCreation extends SampleBase
{
    private static final long serialVersionUID = 8233679374775813938L;

    protected Location location;

    public SampleCreation()
    {
    }

    /**
     * Construct a new object for sample creation via the API.
     *
     * @param s The original Sample object.
     *
     * @throws IllegalStateException if {@code s} has a URI set (meaning it
     * is already known in the system) or does not have its
     * {@code creationLocation} field set with a location.
     */
    public SampleCreation(Sample s)
    {
        super(s);

        if (s.getUri() != null)
        {
            throw new IllegalStateException("Sample '" + s.getName() + "' already has a URI");
        }
        if (s.getCreationLocation() == null)
        {
            throw new IllegalStateException("Sample '" + s.getName() + "' has no creation location set");
        }

        this.location = s.getCreationLocation();
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location value)
    {
        this.location = value;
    }

}
