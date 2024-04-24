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

import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.userdefined.UDF;

/**
 * The biosource of the Sample.
 * <p>
 * A Biosource can either be preset or custom. To match a preset biosource, the
 * name should be provided that matches the preset biosources description. If
 * name is not provided, it is considered a custom biosource
 * </p>
 *
 * @deprecated Biosource is not supported in Clarity.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "biosource", propOrder = { "description", "fields" })
@Deprecated
public class BioSource implements Serializable
{
    private static final long serialVersionUID = -4781585129823907022L;

    @XmlElement(name = "description")
    protected String description;

    @XmlElement(name = "field", namespace = UDF_NAMESPACE)
    protected List<UDF> fields;

    @XmlAttribute(name = "name")
    protected String name;


    public BioSource()
    {
    }

    public BioSource(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String value)
    {
        this.description = value;
    }

    public List<UDF> getFields()
    {
        if (fields == null)
        {
            fields = new ArrayList<UDF>();
        }
        return this.fields;
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
