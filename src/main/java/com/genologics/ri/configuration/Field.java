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

package com.genologics.ri.configuration;

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
 * The detailed representation of the configuration of a user-defined field.
 */
@ClarityEntity(uriSection = "configuration/udfs", updateable = true)
@XmlRootElement(name = "field")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "field",
         propOrder = { "name", "attachToName", "precision", "unitLabel", "typeDefinition", "showInLablink",
                       "allowNonPresetValues", "firstPresetIsDefaultValue", "showInTables", "editable",
                       "deviation", "controlledVocabulary", "parentUri", "childUris", "presets",
                       "minValue", "maxValue", "required", "attachToCategory" })
public class Field implements Linkable<Field>, Serializable
{
    private static final long serialVersionUID = -60457662591218753L;

    @XmlElement(name = "name")
    protected String name;

    @XmlElement(name = "attach-to-name")
    protected String attachToName;

    @XmlElement(name = "precision")
    protected Integer precision;

    @XmlElement(name = "unit")
    protected String unitLabel;

    @XmlElement(name = "type-definition")
    protected TypeDefinition typeDefinition;

    @XmlElement(name = "show-in-lablink")
    protected Boolean showInLablink;

    @XmlElement(name = "allow-non-preset-values")
    protected Boolean allowNonPresetValues;

    @XmlElement(name = "first-preset-is-default-value")
    protected Boolean firstPresetIsDefaultValue;

    @XmlElement(name = "show-in-tables")
    protected Boolean showInTables;

    @XmlElement(name = "is-editable")
    protected Boolean editable;

    @XmlElement(name = "is-deviation")
    protected Boolean deviation;

    @XmlElement(name = "is-controlled-vocabulary")
    protected Boolean controlledVocabulary;

    @XmlElement(name = "parent-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI parentUri;

    @XmlElement(name = "child-uri")
    protected List<String> childUris;

    @XmlElement(name = "preset")
    protected List<String> presets;

    @XmlElement(name = "min-value")
    protected Double minValue;

    @XmlElement(name = "max-value")
    protected Double maxValue;

    @XmlElement(name = "is-required")
    protected Boolean required;

    @XmlElement(name = "attach-to-category")
    protected String attachToCategory;

    @XmlAttribute(name = "type")
    protected FieldType type;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public List<String> getChildUris()
    {
        if (childUris == null)
        {
            childUris = new ArrayList<String>();
        }
        return childUris;
    }

    public List<String> getPresets()
    {
        if (presets == null)
        {
            presets = new ArrayList<String>();
        }
        return presets;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAttachToName()
    {
        return attachToName;
    }

    public void setAttachToName(String attachToName)
    {
        this.attachToName = attachToName;
    }

    public Integer getDisplayPrecision()
    {
        return precision;
    }

    public void setDisplayPrecision(Integer precision)
    {
        this.precision = precision;
    }

    public String getUnitLabel()
    {
        return unitLabel;
    }

    public void setUnitLabel(String unit)
    {
        this.unitLabel = unit;
    }

    public TypeDefinition getTypeDefinition()
    {
        return typeDefinition;
    }

    public void setTypeDefinition(TypeDefinition typeDefinition)
    {
        this.typeDefinition = typeDefinition;
    }

    public boolean isShowInLablink()
    {
        return showInLablink == null ? false : showInLablink.booleanValue();
    }

    public void setShowInLablink(Boolean showInLablink)
    {
        this.showInLablink = showInLablink;
    }

    public boolean isAllowNonPresetValues()
    {
        return allowNonPresetValues == null ? false : allowNonPresetValues.booleanValue();
    }

    public void setAllowNonPresetValues(Boolean allowNonPresetValues)
    {
        this.allowNonPresetValues = allowNonPresetValues;
    }

    public boolean isFirstPresetDefaultValue()
    {
        return firstPresetIsDefaultValue == null ? false : firstPresetIsDefaultValue.booleanValue();
    }

    public void setFirstPresetDefaultValue(Boolean firstPresetIsDefaultValue)
    {
        this.firstPresetIsDefaultValue = firstPresetIsDefaultValue;
    }

    public boolean isShowInTables()
    {
        return showInTables == null ? false : showInTables.booleanValue();
    }

    public void setShowInTables(Boolean showInTables)
    {
        this.showInTables = showInTables;
    }

    public boolean isEditable()
    {
        return editable == null ? false : editable.booleanValue();
    }

    public void setEditable(Boolean editable)
    {
        this.editable = editable;
    }

    public boolean isDeviation()
    {
        return deviation == null ? false : deviation.booleanValue();
    }

    public void setDeviation(Boolean deviation)
    {
        this.deviation = deviation;
    }

    public boolean isControlledVocabulary()
    {
        return controlledVocabulary == null ? false : controlledVocabulary.booleanValue();
    }

    public void setControlledVocabulary(Boolean controlledVocabulary)
    {
        this.controlledVocabulary = controlledVocabulary;
    }

    public URI getParentUri()
    {
        return parentUri;
    }

    public void setParentUri(URI parentUri)
    {
        this.parentUri = parentUri;
    }

    public Double getMinValue()
    {
        return minValue;
    }

    public void setMinValue(Double minValue)
    {
        this.minValue = minValue;
    }

    public Double getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(Double maxValue)
    {
        this.maxValue = maxValue;
    }

    public boolean isRequired()
    {
        return required == null ? false : required.booleanValue();
    }

    public void setRequired(Boolean required)
    {
        this.required = required;
    }

    public String getAttachToCategory()
    {
        return attachToCategory;
    }

    public void setAttachToCategory(String attachToCategory)
    {
        this.attachToCategory = attachToCategory;
    }

    public FieldType getType()
    {
        return type;
    }

    public void setType(FieldType type)
    {
        this.type = type;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

}
