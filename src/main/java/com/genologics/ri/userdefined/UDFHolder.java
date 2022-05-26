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
}
