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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.configuration.FieldLink;

/**
 * The process-type element contains the XML representation of a type of process
 * configured in the system.
 */
@ClarityEntity(uriSection = "processtypes")
@XmlRootElement(name = "process-type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process-type",
         propOrder = { "fieldDefinitions", "parameters", "typeDefinitions", "processInputs",
                       "processOutputs", "processTypeAttributes",
                       "permittedContainers", "permittedReagentCategories", "requiredReagentKits",
                       "permittedControlTypes", "permittedInstrumentTypes",
                       "queueFields", "iceBucketFields", "stepFields", "sampleFields",
                       "stepProperties", "stepSetup", "eppTriggers" })
public class ProcessType implements Linkable<ProcessType>, Serializable
{
    private static final long serialVersionUID = 3103866326222677563L;

    @Deprecated
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

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "permitted-containers")
    @XmlElement(name = "container-type")
    protected List<ContainerTypeLink> permittedContainers;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "permitted-reagent-categories")
    @XmlElement(name = "reagent-category")
    protected List<String> permittedReagentCategories;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "required-reagent-kits")
    @XmlElement(name = "reagent-kit")
    protected List<ReagentKitLink> requiredReagentKits;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "permitted-control-types")
    @XmlElement(name = "control-type")
    protected List<ControlTypeLink> permittedControlTypes;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "permitted-instrument-types")
    @XmlElement(name = "instrument-type")
    protected List<String> permittedInstrumentTypes;

    /**queueFields
     * @since 2.25
     */
    @XmlElementWrapper(name = "queue-fields")
    @XmlElement(name = "queue-field")
    protected List<QueueField> queueFields;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "ice-bucket-fields")
    @XmlElement(name = "ice-bucket-field")
    protected List<IceBucketField> iceBucketFields;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "step-fields")
    @XmlElement(name = "step-field")
    protected List<Field> stepFields;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "sample-fields")
    @XmlElement(name = "sample-field")
    protected List<Field> sampleFields;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "step-properties")
    @XmlElement(name = "step-property")
    protected List<StepProperty> stepProperties;

    /**
     * @since 2.25
     */
    @XmlElement(name = "step-setup")
    protected StepSetup stepSetup;

    /**
     * @since 2.25
     */
    @XmlElementWrapper(name = "epp-triggers")
    @XmlElement(name = "epp-trigger")
    protected List<EppTrigger> eppTriggers;

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

    /**
     * Each field definition provides a URI linking to the configuration of a user-defined field for the output type.
     *
     * @return A list of links to fields.
     *
     * @deprecated These field definitions are ignored as of Clarity LIMS 5.0.
     */
    @Deprecated
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

    public TypeDefinition addTypeDefinition(Linkable<com.genologics.ri.configuration.Field> link)
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

    public List<ContainerTypeLink> getPermittedContainers()
    {
        if (permittedContainers == null)
        {
            permittedContainers = new ArrayList<ContainerTypeLink>();
        }
        return permittedContainers;
    }

    public List<String> getPermittedReagentCategories()
    {
        if (permittedReagentCategories == null)
        {
            permittedReagentCategories = new ArrayList<String>();
        }
        return permittedReagentCategories;
    }

    public List<ReagentKitLink> getRequiredReagentKits()
    {
        if (requiredReagentKits == null)
        {
            requiredReagentKits = new ArrayList<ReagentKitLink>();
        }
        return requiredReagentKits;
    }

    public List<ControlTypeLink> getPermittedControlTypes()
    {
        if (permittedControlTypes == null)
        {
            permittedControlTypes = new ArrayList<ControlTypeLink>();
        }
        return permittedControlTypes;
    }

    public List<String> getPermittedInstrumentTypes()
    {
        if (permittedInstrumentTypes == null)
        {
            permittedInstrumentTypes = new ArrayList<String>();
        }
        return permittedInstrumentTypes;
    }

    public List<QueueField> getQueueFields()
    {
        if (queueFields == null)
        {
            queueFields = new ArrayList<QueueField>();
        }
        return queueFields;
    }

    public List<IceBucketField> getIceBucketFields()
    {
        if (iceBucketFields == null)
        {
            iceBucketFields = new ArrayList<IceBucketField>();
        }
        return iceBucketFields;
    }

    public List<Field> getStepFields()
    {
        if (stepFields == null)
        {
            stepFields = new ArrayList<Field>();
        }
        return stepFields;
    }

    public List<Field> getSampleFields()
    {
        if (sampleFields == null)
        {
            sampleFields = new ArrayList<Field>();
        }
        return sampleFields;
    }

    public List<StepProperty> getStepProperties()
    {
        if (stepProperties == null)
        {
            stepProperties = new ArrayList<StepProperty>();
        }
        return stepProperties;
    }

    public StepSetup getStepSetup()
    {
        return stepSetup;
    }

    public void setStepSetup(StepSetup stepSetup)
    {
        this.stepSetup = stepSetup;
    }

    public List<EppTrigger> getEppTriggers()
    {
        if (eppTriggers == null)
        {
            eppTriggers = new ArrayList<EppTrigger>();
        }
        return eppTriggers;
    }

}
