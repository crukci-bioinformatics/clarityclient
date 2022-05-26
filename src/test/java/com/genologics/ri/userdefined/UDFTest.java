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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.cruk.clarity.api.unittests.ClarityClientTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.genologics.ri.artifact.Artifact;

@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class UDFTest
{
    @Autowired
    protected Jaxb2Marshaller marshaller;

    protected File exampleDirectory = new File("src/test/jaxb");

    private final Collection<UDF> nullCollection = null;
    private final UDFHolder nullHolder = null;

    public UDFTest() throws Exception
    {
    }

    @Test
    public void testGetUDF() throws Exception
    {
        Artifact a = (Artifact)marshaller.unmarshal(new StreamSource(new File(exampleDirectory, "artifact.xml")));

        try
        {
            UDF.getUDF(a.getUserDefinedFields(), null);
            fail("Allowed null name");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        UDF udf = UDF.getUDF(a.getUserDefinedFields(), "User Comments");
        assertNotNull(udf, "No UDF found");

        udf = UDF.getUDF(nullCollection, "User Comments");
        assertNull(udf, "UDF found in null collection");

        udf = UDF.getUDF(a.getUserDefinedFields(), "No Field");
        assertNull(udf, "UDF found");

        try
        {
            udf = UDF.getUDF(a.getUserDefinedFields(), "User Comments", true);
            assertNotNull(udf,"No UDF found");
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            udf = UDF.getUDF(nullCollection, "User Comments", true);
            fail("No exception with UDF from null collection.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"User Comments\" does not exist.", e.getMessage(), "Exception message wrong");
        }

        try
        {
            udf = UDF.getUDF(a.getUserDefinedFields(), "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"No Field\" does not exist.", e.getMessage(), "Exception message wrong");
        }

        try
        {
            udf = UDF.getUDF(a.getUserDefinedFields(), "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Missing the field No Field", e.getMessage(), "Exception message wrong");
        }
    }

    @Test
    public void testGetUDFValue() throws Exception
    {
        Artifact a = (Artifact)marshaller.unmarshal(new StreamSource(new File(exampleDirectory, "artifact.xml")));

        try
        {
            UDF.getUDFValue(a.getUserDefinedFields(), null);
            fail("Allowed null name");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        String value = UDF.getUDFValue(a.getUserDefinedFields(), "User Comments");
        assertNotNull(value, "No UDF found");

        value = UDF.getUDFValue(nullCollection, "User Comments");
        assertNull(value, "UDF found in null collection");

        value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field");
        assertNull(value, "UDF found");

        value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field", "The default");
        assertEquals("The default", value, "UDF found and no default value");

        try
        {
            value = UDF.getUDFValue(a.getUserDefinedFields(), "User Comments", true);
            assertNotNull(value, "No UDF found");
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue(nullHolder, "User Comments", true);
            fail("No exception with UDF from null collection.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"User Comments\" does not exist because 'thing' is null.", e.getMessage(), "Exception message wrong");
        }

        try
        {
            value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"No Field\" does not exist.", e.getMessage(), "Exception message wrong");
        }

        try
        {
            value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Missing the field No Field", e.getMessage(), "Exception message wrong");
        }
    }

    @Test
    public void testSetUDF()
    {
        List<UDF> udfs = new ArrayList<UDF>();

        try
        {
            UDF.setUDF(nullCollection, null, null);
            fail("Allowed null collection");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            UDF.setUDF(udfs, null, null);
            fail("Allowed null name");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            UDF.setUDF(udfs, "Field", null);
            // Ok.
        }
        catch (IllegalArgumentException e)
        {
            fail("Disallowed null type");
        }

        UDF udf = UDF.setUDF(udfs, "Field", null);
        assertNull(udf, "Set UDF to null should return null");

        udf = UDF.setUDF(udfs, "Field1", "Hello");
        assertNotNull(udf, "Set UDF didn't return a field");
        assertEquals(1, udfs.size(), "Set UDF didn't add to the collection");
        assertEquals("Field1", udf.getName(), "Field returned name wrong");
        assertEquals(null, udf.getType(), "Field returned type wrong");
        assertEquals("Hello", udf.getValue(), "Field returned value wrong");

        udf = UDF.setUDF(udfs, "Field2", "2.45");
        assertNotNull(udf, "Set UDF didn't return a field");
        assertEquals(2, udfs.size(), "Set UDF didn't add to the collection");

        udf = UDF.setUDF(udfs, "Field1", "Moving on");
        assertNotNull(udf, "Set UDF didn't return a field");
        assertEquals(2, udfs.size(), "Set UDF added when it should have just set");

        udf = UDF.setUDF(udfs, "Field1", null);
        assertNotNull(udf, "Set UDF to null didn't return a field");
        assertEquals(2, udfs.size(), "Set UDF to null changed the collection");
        assertEquals("", udf.getValue(), "Set UDF to null should result in an empty value");
    }

    @Test
    public void testGetUDFFromObject() throws Exception
    {
        Artifact a = (Artifact)marshaller.unmarshal(new StreamSource(new File(exampleDirectory, "artifact.xml")));

        try
        {
            UDF.getUDF(a, null);
            fail("Allowed null name");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        UDF udf = UDF.getUDF(a, "User Comments");
        assertNotNull(udf, "No UDF found");

        udf = UDF.getUDF(nullHolder, "User Comments");
        assertNull(udf, "UDF found in null collection");

        udf = UDF.getUDF(a, "No Field");
        assertNull(udf, "UDF found");

        try
        {
            udf = UDF.getUDF(a, "User Comments", true);
            assertNotNull(udf, "No UDF found");
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            udf = UDF.getUDF(nullHolder, "User Comments", true);
            fail("No exception with UDF from null entity.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"User Comments\" does not exist because 'thing' is null.", e.getMessage(), "Exception message wrong");
        }

        try
        {
            udf = UDF.getUDF(a, "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"No Field\" does not exist on Artifact ADM2A8PA1", e.getMessage(), "Exception message wrong");
        }

        try
        {
            udf = UDF.getUDF(a, "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Missing the field No Field", e.getMessage(), "Exception message wrong");
        }
    }

    @Test
    public void testGetUDFValueFromObject() throws Exception
    {
        Artifact a = (Artifact)marshaller.unmarshal(new StreamSource(new File(exampleDirectory, "artifact.xml")));

        try
        {
            UDF.getUDFValue(a, null);
            fail("Allowed null name");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        String value = UDF.getUDFValue(a, "User Comments");
        assertNotNull(value, "No UDF found");

        value = UDF.getUDFValue(nullHolder, "User Comments");
        assertNull(value, "UDF found in null collection");

        value = UDF.getUDFValue(a, "No Field");
        assertNull(value, "UDF found");

        value = UDF.getUDFValue(a, "No Field", "The default");
        assertEquals("The default", value, "UDF found and no default value");

        try
        {
            value = UDF.getUDFValue(a, "User Comments", true);
            assertNotNull(value, "No UDF found");
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue(nullHolder, "User Comments", true);
            fail("No exception with UDF from null entity.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"User Comments\" does not exist because 'thing' is null.", e.getMessage(), "Exception message wrong");
        }

        try
        {
            value = UDF.getUDFValue(a, "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("UDF \"No Field\" does not exist on Artifact ADM2A8PA1", e.getMessage(), "Exception message wrong");
        }

        try
        {
            value = UDF.getUDFValue(a, "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Missing the field No Field", e.getMessage(), "Exception message wrong");
        }
    }
}
