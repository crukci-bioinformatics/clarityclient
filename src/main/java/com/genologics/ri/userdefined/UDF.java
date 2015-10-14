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

package com.genologics.ri.userdefined;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.StringUtils;

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

    private static final String UDF_METHOD_NAME = "getUserDefinedFields";

    private static Map<Class<?>, Method> classUdfMethods = Collections.synchronizedMap(new HashMap<Class<?>, Method>());


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
        return value != null ? value : defaultValue;
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
            if (failMessage == null)
            {
                failMessage = "UDF '" + name + "' does not exist.";
            }
            throw new MissingUDFException(name, failMessage);
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
        return udf == null ? null : udf.getValue();
    }

    /**
     * Finds a UDF by name in the UDFs of the given object.
     *
     * @param thing The object that should have the UDFs. Needs to have a method
     * "getUserDefinedFields".
     * @param name The name of the UDF to find.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     */
    public static UDF getUDF(Object thing, String name)
    {
        return getUDF(thing, name, false, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and, if found, returns its value.
     *
     * @param thing The object that should have the UDFs. Needs to have a method
     * "getUserDefinedFields".
     * @param name The name of the UDF to find.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     */
    public static String getUDFValue(Object thing, String name)
    {
        return getUDFValue(thing, name, false, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and, if found, returns its value.
     *
     * @param thing The object that should have the UDFs. Needs to have a method
     * "getUserDefinedFields".
     * @param name The name of the UDF to find.
     * @param defaultValue The value to return if there is no matching UDF.
     *
     * @return The value of the UDF, or {@code defaultValue} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     *
     * @since 2.22
     */
    public static String getUDFValue(Object thing, String name, String defaultValue)
    {
        String value = getUDFValue(thing, name, false, null);
        return value != null ? value : defaultValue;
    }

    /**
     * Finds a UDF by name in the UDFs of the given object.
     *
     * @param thing The object that should have the UDFs. Needs to have a method
     * "getUserDefinedFields".
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static UDF getUDF(Object thing, String name, boolean fail)
    {
        return getUDF(thing, name, fail, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and returns its value.
     *
     * @param thing The object that should have the UDFs. Needs to have a method
     * "getUserDefinedFields".
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static String getUDFValue(Object thing, String name, boolean fail)
    {
        return getUDFValue(thing, name, fail, null);
    }

    /**
     * Finds a UDF by name in the UDFs of the given object.
     *
     * <p>The given object needs to have a method "getUserDefinedFields" returning a {@link Collection}
     * of UDFs.</p>
     *
     * @param thing The object that should have the UDFs.
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     * @param failMessage The message to put in the {@code MissingUDFException} when
     * such an exception is raised.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static UDF getUDF(Object thing, String name, boolean fail, String failMessage)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("name cannot be null");
        }

        UDF udf = null;

        if (thing != null)
        {
            Collection<UDF> udfs = getUDFCollection(thing);
            udf = getUDF(udfs, name, fail, failMessage);
        }

        return udf;
    }

    /**
     * Finds a UDF by name in the UDFs of the given object and returns its value.
     *
     * @param thing The object that should have the UDFs. Needs to have a method
     * "getUserDefinedFields".
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     * @param failMessage The message to put in the {@code MissingUDFException} when
     * such an exception is raised.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     */
    public static String getUDFValue(Object thing, String name, boolean fail, String failMessage)
    {
        UDF udf = getUDF(thing, name, fail, failMessage);
        return udf == null ? null : udf.getValue();
    }

    /**
     * Helper method to get the collection of UDFs from an object.
     *
     * @param thing The object that should have the UDFs.
     *
     * @return The collection of UDFs from {@code thing}.
     *
     * @throws IllegalArgumentException if {@code thing} is null, or if {@code thing}
     * does not have a publicly visible "getUserDefinedFields" method returning a {@code Collection}.
     */
    static Collection<UDF> getUDFCollection(Object thing)
    {
        if (thing == null)
        {
            throw new IllegalArgumentException("thing cannot be null");
        }

        Method getPropsMethod = classUdfMethods.get(thing.getClass());
        if (getPropsMethod == null)
        {
            try
            {
                getPropsMethod = thing.getClass().getMethod(UDF_METHOD_NAME);
                if (!Collection.class.isAssignableFrom(getPropsMethod.getReturnType()))
                {
                    throw new IllegalArgumentException(
                            MessageFormat.format(
                                "The \"{0}\" method on {1} does not return a Collection.",
                                UDF_METHOD_NAME, thing.getClass().getName()));
                }

                classUdfMethods.put(thing.getClass(), getPropsMethod);
            }
            catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException(
                        MessageFormat.format(
                            "There is no method \"{0}\" method on {1}.",
                            UDF_METHOD_NAME, thing.getClass().getName()));
            }
        }

        try
        {
            @SuppressWarnings("unchecked")
            Collection<UDF> udfs = (Collection<UDF>)getPropsMethod.invoke(thing);

            return udfs;
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException(
                    MessageFormat.format(
                        "Cannot call method \"{0}\" method on {1}: {2}",
                        UDF_METHOD_NAME, thing.getClass().getName(), e.getMessage()));
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException("Exception while fetching UDFs from " + thing.getClass().getName(),
                                       e.getTargetException());
        }
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
     * are null, or if {@code thing} does not have a publicly visible
     * "getUserDefinedFields" method returning a {@code Collection}.
     */
    public static UDF setUDF(Object thing, String name, Object value)
    {
        Collection<UDF> udfs = getUDFCollection(thing);

        return setUDF(udfs, name, value);
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

        if (StringUtils.isEmpty(strValue))
        {
            if (udf != null)
            {
                // Remove the field.
                udfs.remove(udf);
                udf = null;
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
