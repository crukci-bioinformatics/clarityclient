package com.genologics.ri.userdefined;

import java.util.List;

/**
 * Interface for any entity or other object that holds user defined
 * fields (UDFs).
 *
 * @since 2.31
 */
public interface UDFHolder
{
    /**
     * Get a list of user defined fields held by the entity.
     *
     * @return A list of UDF objects. This should never return null.
     */
    List<UDF> getUserDefinedFields();

    /**
     * Alternative method used as a shorthand for {@code getUserDefinedFields}.
     *
     * @return A list of UDF objects.
     */
    default List<UDF> getUDFs()
    {
        return getUserDefinedFields();
    }

    /**
     * Another alternative for {@code getUserDefinedFields} that is more pleasant
     * on the eye when used in Groovy or other scripting systems where the property
     * is accessed as if it were a field.
     *
     * @return A list of UDF objects.
     */
    default List<UDF> getUdfs()
    {
        return getUserDefinedFields();
    }

    /**
     * Finds a UDF by name.
     *
     * @param name The name of the UDF to find.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     *
     * @since 2.31.5
     */
    default UDF getUDF(String name)
    {
        return UDF.getUDF(this, name);
    }

    /**
     * Finds a UDF by name and, if found, returns its value.
     *
     * @param name The name of the UDF to find.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     *
     * @since 2.31.5
     */
    default String getUDFValue(String name)
    {
        return UDF.getUDFValue(this, name);
    }

    /**
     * Finds a UDF by name and, if found, returns its value.
     *
     * @param name The name of the UDF to find.
     * @param defaultValue The value to return if there is no matching UDF.
     *
     * @return The value of the UDF, or {@code defaultValue} if there
     * is no matching UDF.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     *
     * @since 2.31.5
     */
    default String getUDFValue(String name, String defaultValue)
    {
        return UDF.getUDFValue(this, name, defaultValue);
    }

    /**
     * Finds a UDF by name.
     *
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The UDF object with the same name, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     *
     * @since 2.31.5
     */
    default UDF getUDF(String name, boolean fail)
    {
        return UDF.getUDF(this, name, fail);
    }

    /**
     * Finds a UDF by name and returns its value.
     *
     * @param name The name of the UDF to find.
     * @param fail Whether to fail with a {@code MissingUDFException} if the field
     * is not found.
     *
     * @return The value of the UDF, or {@code null} if there
     * is no matching UDF and {@code fail} is false.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     * @throws MissingUDFException if {@code fail} is true and there is no matching UDF.
     *
     * @since 2.31.5
     */
    default String getUDFValue(String name, boolean fail)
    {
        return UDF.getUDFValue(this, name, fail);
    }

    /**
     * Finds a UDF by name.
     *
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
    default UDF getUDF(String name, boolean fail, String failMessage)
    {
        return UDF.getUDF(this, name, fail, failMessage);
    }

    /**
     * Finds a UDF by name and return its value.
     *
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
     *
     * @since 2.31.5
     */
    default String getUDFValue(String name, boolean fail, String failMessage)
    {
        return UDF.getUDFValue(this, name, fail, failMessage);
    }

    /**
     * Adds, sets or removes a UDF. If the named
     * UDF exists, it either has its value set (if "value" is not null)
     * or is removed (if "value" is null). If it doesn't exist, a new {@code UDF}
     * object is created for the field and is added.
     *
     * @param name The name of the field to change.
     * @param value The value to set the field to.
     *
     * @return The UDF object found or created, or null if it is removed or not found.
     *
     * @throws IllegalArgumentException if {@code name} is null.
     *
     * @since 2.31.5
     */
    default UDF setUDF(String name, Object value)
    {
        return UDF.setUDF(this, name, value);
    }
}
