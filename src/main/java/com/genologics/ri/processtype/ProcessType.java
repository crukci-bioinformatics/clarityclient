/*
 * CRUK-CI Genologics REST API Java Client.
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.Field;
import com.genologics.ri.configuration.FieldLink;

/**
 * The process-type element contains the XML representation of a type of process
 * configured in the system.
 */
@GenologicsEntity(uriSection = "processtypes")
@XmlRootElement(name = "process-type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process-type",
         propOrder = { "fieldDefinitions", "parameters", "typeDefinitions", "processInputs",
                       "processOutputs", "processTypeAttributes" })
public class ProcessType implements Linkable<ProcessType>, Serializable
{
    private static final long serialVersionUID = 2955957228810223128L;

    @XmlElement(name = "field-definition")
    protected List<FieldLink> fieldDefinitions;

    @XmlElement(name = "parameter")
    protected List<Parameter> parameters;

    @XmlElement(name = "type-definition")
    protected List<TypeDefinition> typeDefinitions;

    @XmlElement(name = "process-input")
    protected List<ProcessInput> processInputs;

    @XmlElement(name = "process-output")
    protected List<ProcessOutput> processOutputs;

    @XmlElement(name = "process-type-attribute")
    protected List<ProcessTypeAttribute> processTypeAttributes;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public ProcessType()
    {
    }

    public ProcessType(URI uri)
    {
        this.uri = uri;
    }

    public ProcessType(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
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

    public List<FieldLink> getFieldDefinitions()
    {
        if (fieldDefinitions == null)
        {
            fieldDefinitions = new ArrayList<FieldLink>();
        }
        return fieldDefinitions;
    }

    public List<Parameter> getParameters()
    {
        if (parameters == null)
        {
            parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }

    public List<TypeDefinition> getTypeDefinitions()
    {
        if (typeDefinitions == null)
        {
            typeDefinitions = new ArrayList<TypeDefinition>();
        }
        return typeDefinitions;
    }

    public TypeDefinition addTypeDefinition(Linkable<Field> link)
    {
        TypeDefinition td = new TypeDefinition(link);
        getTypeDefinitions().add(td);
        return td;
    }

    public List<ProcessInput> getProcessInputs()
    {
        if (processInputs == null)
        {
            processInputs = new ArrayList<ProcessInput>();
        }
        return processInputs;
    }

    public List<ProcessOutput> getProcessOutputs()
    {
        if (processOutputs == null)
        {
            processOutputs = new ArrayList<ProcessOutput>();
        }
        return processOutputs;
    }

    public List<ProcessTypeAttribute> getProcessTypeAttributes()
    {
        if (processTypeAttributes == null)
        {
            processTypeAttributes = new ArrayList<ProcessTypeAttribute>();
        }
        return processTypeAttributes;
    }

}
