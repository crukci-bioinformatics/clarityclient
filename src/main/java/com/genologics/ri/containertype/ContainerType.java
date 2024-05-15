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

package com.genologics.ri.containertype;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;

/**
 *
 * The detailed representation of a container type.
 * <p>
 * The container type describes the physical characteristics of a class of
 * containers such as the number of wells in the container as well as describing
 * the coordinate system used for identifying those wells.
 * </p>
 * <p>
 * The container type is described using a rectangular coordinate system. The
 * characteristics of the horizontal axis are described by the x-dimension
 * element, and the characteristics of the vertical dimension are described by
 * the y-dimension child elements. The characteristics of each dimension
 * included both the size in that dimension as well as the identification method
 * for values in that dimension. Values can be identified either numerically
 * where 0 is the first item or alphabetically where A is the first element.
 * Additionally an offset can be specified which shifts the value of the first
 * element by a fixed amount. When a well location is represented using the
 * coordinate system, it is shown as Y-Value:X-Value (for example A:1).
 *
 * <p>
 * The following are examples of the coordinate system:
 * </p>
 * <ul>
 * <li>Y-Dimension: alphabetic, size 1, offset 0. X-Dimension: numeric, size 1,
 * offset 0. Size: 1. Valid value: A:0</li>
 * <li>Y-Dimension: alphabetic, size 12, offset 0. X-Dimension: numeric, size 8,
 * offset 0. Size: 96. Valid values: A:0 ... L:7</li>
 * <li>Y-Dimension: alphabetic, size 8, offset 0. X-Dimension: numeric, size 12,
 * offset 1. Size: 96. Valid values: A:1 ... H:12</li>
 * </ul>
 * <p>
 * The container type also identifies wells in the container that are not available for storing
 * samples or reagents, either because the configuration of the container requires those wells to
 * be empty for the instrument configuration or because the well contains specific calibrants that
 * are required by the instrument. The well location of the unavailable wells is specified
 * using the coordinate system that is described by the x-dimension and y-dimension of the container
 * type.
 * </p>
 * <p>
 * <i>Note: calibrant-well is no longer supported and will be ignored if provided.</i>
 * </p>
 */
@ClarityEntity(uriSection = "containertypes", creatable = true, updateable = true)
@XmlRootElement(name = "container-type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "container-type",
         propOrder = { "tube", "calibrantWells", "unavailableWells", "columns", "rows" })
public class ContainerType implements Linkable<ContainerType>, Serializable
{
    private static final long serialVersionUID = -3778472967085446652L;

    @XmlElement(name = "is-tube")
    protected Boolean tube;

    @XmlElement(name = "calibrant-well")
    @Deprecated
    protected List<CalibrantWell> calibrantWells;

    @XmlElement(name = "unavailable-well")
    protected List<String> unavailableWells;

    @XmlElement(name = "x-dimension")
    protected Dimension columns;

    @XmlElement(name = "y-dimension")
    protected Dimension rows;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Boolean isTube()
    {
        return tube;
    }

    public void setTube(Boolean tube)
    {
        this.tube = tube;
    }

    /**
     * Each calibrant well identifies a well location that is reserved for calibrants in containers of the container type.
     *
     * @deprecated This property is no longer supported and will be ignored if provided.
     *
     * @return A list of calibrant wells.
     */
    @Deprecated
    public List<CalibrantWell> getCalibrantWells()
    {
        if (calibrantWells == null)
        {
            calibrantWells = new ArrayList<CalibrantWell>();
        }
        return calibrantWells;
    }

    public List<String> getUnavailableWells()
    {
        if (unavailableWells == null)
        {
            unavailableWells = new ArrayList<String>();
        }
        return unavailableWells;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public Dimension getColumns()
    {
        return columns;
    }

    public void setColumns(Dimension columns)
    {
        this.columns = columns;
    }

    public Dimension getRows()
    {
        return rows;
    }

    public void setRows(Dimension rows)
    {
        this.rows = rows;
    }

    /**
     * Get the capacity of a container of this type, i.e. the columns * rows.
     *
     * @return The capacity (number of lanes/wells). If this cannot be calculated
     * through missing values, returns null.
     *
     * @since 2.31.2
     */
    public Integer getCapacity()
    {
        try
        {
            return columns.getSize() * rows.getSize();
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    @Override
    public String toString()
    {
        return name;
    }
}
