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

package com.genologics.ri.userdefined;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

import org.apache.commons.lang3.ClassUtils;

import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Locatable;
import com.genologics.ri.configuration.FieldType;

/**
 *
 * Field is the value and data type of a user-defined field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "field")
public class UDF implements Serializable
{
    private static final long serialVersionUID = -3019516615900521203L;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "type")
    protected FieldType type;

    @XmlAttribute(name = "unit")
    protected String unit;

    @XmlValue
    protected String value;

    public UDF()
    {
    }

    public UDF(String name, FieldType type)
    {
        this(name, type, null);
    }

    public UDF(String name, Object value)
    {
        this(name, null, value);
    }

    public UDF(String name, FieldType type, Object value)
    {
        setName(name);
        setType(type);
        setValue(value);
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value == null ? null : value.toString();
    }

    public String getUnit()
    {
        return unit;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public FieldType getType()
    {
        return type;
    }

    public void setType(FieldType type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder(200);
        b.append("UDF[name=").append(name);
        b.append(",type=").append(type);
        b.append(",value=").append(value);
        b.append(']');
        return b.toString();
    }

    /**
     * Finds a UDF by name in a collection of UDF objects.
     *
     * @param udfs The collection to search.
     * @param name The name of the UDF to find.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     */
    public static UDF getUDF(Collection<UDF> udfs, String name)
    {
        return getUDF(udfs, name, false, null);
    }

    /**
     * Finds a UDF by name in a collection of UDF objects and, if found, returns its value.
     *
     * @param udfs The collection to search.
     * @param name The name of the UDF to find.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     */
    public static String getUDFValue(Collection<UDF> udfs, String name)
    {
        return getUDFValue(udfs, name, false, null);
    }

    /**
     * Finds a UDF by name in a collection of UDF objects and, if found, returns its value.
     *
     * @param udfs The collection to search.
     * @param name The name of the UDF to find.
     * @param defaultValue The value to return if there is no matching UDF.
     *
     * @return The value of the UDF, or {@code defaultValue} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     *
     * @since 2.22
     */
    public static String getUDFValue(Collection<UDF> udfs, String name, String defaultValue)
    {
        String value = getUDFValue(udfs, name, false, null);
        return isNotEmpty(value) ? value : defaultValue;
    }

    /**
     * Finds a UDF by name in a collection of UDF objects.
     *
     * @param udfs The collection to search.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static UDF getUDF(Collection<UDF> udfs, String name, boolean fail)
    {
        return getUDF(udfs, name, fail, null);
    }

    /**
     * Finds a UDF by name in a collection of UDF objects and returns its value.
     *
     * @param udfs The collection to search.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static String getUDFValue(Collection<UDF> udfs, String name, boolean fail)
    {
        return getUDFValue(udfs, name, fail, null);
    }

    /**
     * Finds a UDF by name in a collection of UDF objects.
     *
     * @param udfs The collection to search.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     * @param failMessage The message to put in the {@code MissingUDFException} when
     * such an exception is raised.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static UDF getUDF(Collection<UDF> udfs, String name, boolean fail, String failMessage)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("name cannot be null");
        }

        if (udfs != null)
        {
            for (UDF u : udfs)
            {
                if (u != null && name.equals(u.getName()))
                {
                    return u;
                }
            }
        }

        if (fail)
        {
            throw new MissingUDFException(name, getMissingUDFMessage(name, failMessage));
        }

        return null;
    }

    /**
     * Finds a UDF by name in a collection of UDF objects and returns its value.
     *
     * @param udfs The collection to search.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     * @param failMessage The message to put in the {@code MissingUDFException} when
     * such an exception is raised.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static String getUDFValue(Collection<UDF> udfs, String name, boolean fail, String failMessage)
    {
        UDF udf = getUDF(udfs, name, fail, failMessage);
        if (udf != null && fail && isEmpty(udf.getValue()))
        {
            throw new MissingUDFException(name, getMissingUDFMessage(name, failMessage));
        }
        return udf == null || isEmpty(udf.getValue()) ? null : udf.getValue();
    }

    /**
     * Finds a UDF by name in the UDFs of the given object.
     *
     * @param thing The object that holds UDFs.
     * @param name The name of the UDF to find.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     */
    public static UDF getUDF(UDFHolder thing, String name)
    {
        return getUDF(thing, name, false, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and, if found, returns its value.
     *
     * @param thing The object that holds UDFs.
     * @param name The name of the UDF to find.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     */
    public static String getUDFValue(UDFHolder thing, String name)
    {
        return getUDFValue(thing, name, false, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and, if found, returns its value.
     *
     * @param thing The object that holds UDFs.
     * @param name The name of the UDF to find.
     * @param defaultValue The value to return if there is no matching UDF.
     *
     * @return The value of the UDF, or {@code defaultValue} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     *
     * @since 2.22
     */
    public static String getUDFValue(UDFHolder thing, String name, String defaultValue)
    {
        String value = getUDFValue(thing, name, false, null);
        return isNotEmpty(value) ? value : defaultValue;
    }

    /**
     * Finds a UDF by name in the UDFs of the given object.
     *
     * @param thing The object that holds UDFs.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static UDF getUDF(UDFHolder thing, String name, boolean fail)
    {
        return getUDF(thing, name, fail, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and returns its value.
     *
     * @param thing The object that holds UDFs.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static String getUDFValue(UDFHolder thing, String name, boolean fail)
    {
        return getUDFValue(thing, name, fail, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object.
     *
     * @param thing The object that holds UDFs.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     * @param failMessage The message to put in the {@code MissingUDFException} when
     * such an exception is raised.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static UDF getUDF(UDFHolder thing, String name, boolean fail, String failMessage)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("name cannot be null");
        }

        if (thing == null && fail)
        {
            throw new MissingUDFException(name, getMissingUDFMessage(thing, name, failMessage));
        }

        UDF udf = null;

        if (thing != null)
        {
            assert thing.getUserDefinedFields() != null : thing + " getUserDefinedFields returned null: breaks contract.";

            try
            {
                udf = getUDF(thing.getUserDefinedFields(), name, fail, failMessage);
            }
            catch (MissingUDFException e)
            {
                // If there is no explicit fail message, we might be able to provide a better one
                // than that given by getUDF(...).
                throw new MissingUDFException(name, getMissingUDFMessage(thing, name, failMessage));
            }
        }

        return udf;
    }

    /**
     * Create a message for the MissingUDFException when {@code failMessage} is not set.
     *
     * @param name The name of the UDF.
     * @param failMessage The fail message supplied by the caller.
     *
     * @return A suitable failure message for the exception.
     */
    private static String getMissingUDFMessage(String name, String failMessage)
    {
        if (failMessage == null)
        {
            failMessage = "UDF \"" + name + "\" does not exist.";
        }
        return failMessage;
    }

    /**
     * Create a message for the MissingUDFException when {@code failMessage} is not set.
     *
     * @param thing The object the UDF is not set on.
     * @param name The name of the UDF.
     * @param failMessage The fail message supplied by the caller.
     *
     * @return A suitable failure message for the exception.
     */
    private static String getMissingUDFMessage(UDFHolder thing, String name, String failMessage)
    {
        if (failMessage == null)
        {
            if (thing == null)
            {
                failMessage = "UDF \"" + name + "\" does not exist because 'thing' is null.";
            }
            else
            {
                String better = "UDF \"{0}\" does not exist on {1} {2}";
                if (thing instanceof LimsEntityLinkable<?> linkable)
                {
                    failMessage = MessageFormat.format(better, name, ClassUtils.getShortClassName(thing.getClass()), linkable.getLimsid());
                }
                else if (thing instanceof Locatable locatable)
                {
                    failMessage = MessageFormat.format(better, name, ClassUtils.getShortClassName(thing.getClass()), locatable.getUri());
                }
                else
                {
                    failMessage = MessageFormat.format(better, name, ClassUtils.getShortClassName(thing.getClass()),
                                                       Integer.toHexString(System.identityHashCode(thing)));
                }
            }
        }
        return failMessage;
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and returns its value.
     *
     * @param thing The object that holds UDFs.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     * @param failMessage The message to put in the {@code MissingUDFException} when
     * such an exception is raised.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static String getUDFValue(UDFHolder thing, String name, boolean fail, String failMessage)
    {
        UDF udf = getUDF(thing, name, fail, failMessage);
        if (udf != null && fail && isEmpty(udf.getValue()))
        {
            throw new MissingUDFException(name, getMissingUDFMessage(thing, name, failMessage));
        }
        return udf == null ? null : udf.getValue();
    }

    /**
     * Adds, sets or removes a UDF from the collection of fields on the given object. If the named
     * UDF exists in the collection, it either has its value set (if "value" is not null)
     * or is removed (if "value" is null). If it doesn't exist, a new {@code UDF}
     * object is created for the field and is added to the collection.
     *
     * @param thing The object containing UDFs.
     * @param name The name of the field to change.
     * @param value The value to set the field to.
     *
     * @return The UDF object found or created, or null if it is removed or not found.
     *
     * @throws IllegalArgumentException if either of {@code thing} or {@code name}
     * are null.
     */
    public static UDF setUDF(UDFHolder thing, String name, Object value)
    {
        if (thing == null)
        {
            throw new IllegalArgumentException("UDF holding object cannot be null");
        }

        assert thing.getUserDefinedFields() != null : thing + " getUserDefinedFields returned null: breaks contract.";

        return setUDF(thing.getUserDefinedFields(), name, value);
    }

    /**
     * Adds, sets or removes a UDF from the given collection of fields. If the named
     * UDF exists in the collection, it either has its value set (if "value" is not null)
     * or is removed (if "value" is null). If it doesn't exist, a new {@code UDF}
     * object is created for the field and is added to the collection.
     *
     * @param udfs The collection of UDF objects.
     * @param name The name of the field to change.
     * @param value The value to set the field to.
     *
     * @return The UDF object found or created, or null if it is removed or not found.
     *
     * @throws IllegalArgumentException if either of {@code udfs} or {@code name}
     * are null.
     */
    public static UDF setUDF(Collection<UDF> udfs, String name, Object value)
    {
        if (udfs == null)
        {
            throw new IllegalArgumentException("udfs cannot be null");
        }
        if (name == null)
        {
            throw new IllegalArgumentException("name cannot be null");
        }

        UDF udf = getUDF(udfs, name, false);

        String strValue = value == null ? null : value.toString();

        if (isEmpty(strValue))
        {
            if (udf != null)
            {
                // Need to set the value to empty so when the call is made the
                // API will actually set the value (or rather clear it). It seems
                // that the assumption that it can just be removed from the list
                // is wrong.
                udf.setValue(EMPTY);
            }
        }
        else
        {
            if (udf != null)
            {
                // Just change the value.
                udf.setValue(strValue);
            }
            else
            {
                udf = new UDF(name, strValue);
                udfs.add(udf);
            }
        }

        return udf;
    }
}
