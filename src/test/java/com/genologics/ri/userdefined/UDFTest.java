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

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.cruk.genologics.api.unittests.UnitTestApplicationContextFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.genologics.ri.artifact.Artifact;

public class UDFTest
{
    protected ApplicationContext context;
    protected Jaxb2Marshaller marshaller;

    protected File exampleDirectory = new File("src/test/jaxb");

    public UDFTest() throws Exception
    {
        context = UnitTestApplicationContextFactory.getApplicationContext();
        marshaller = context.getBean("genologicsJaxbMarshaller", Jaxb2Marshaller.class);
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
        assertNotNull("No UDF found", udf);

        udf = UDF.getUDF(null, "User Comments");
        assertNull("UDF found in null collection", udf);

        udf = UDF.getUDF(a.getUserDefinedFields(), "No Field");
        assertNull("UDF found", udf);

        try
        {
            udf = UDF.getUDF(a.getUserDefinedFields(), "User Comments", true);
            assertNotNull("No UDF found", udf);
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            udf = UDF.getUDF(null, "User Comments", true);
            fail("No exception with UDF from null collection.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"User Comments\" does not exist.", e.getMessage());
        }

        try
        {
            udf = UDF.getUDF(a.getUserDefinedFields(), "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"No Field\" does not exist.", e.getMessage());
        }

        try
        {
            udf = UDF.getUDF(a.getUserDefinedFields(), "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "Missing the field No Field", e.getMessage());
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
        assertNotNull("No UDF found", value);

        value = UDF.getUDFValue((Collection<?>)null, "User Comments");
        assertNull("UDF found in null collection", value);

        value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field");
        assertNull("UDF found", value);

        value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field", "The default");
        assertEquals("UDF found and no default value", "The default", value);

        try
        {
            value = UDF.getUDFValue(a.getUserDefinedFields(), "User Comments", true);
            assertNotNull("No UDF found", value);
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue((Collection<?>)null, "User Comments", true);
            fail("No exception with UDF from null collection.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"User Comments\" does not exist because 'thing' is null.", e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"No Field\" does not exist.", e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue(a.getUserDefinedFields(), "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "Missing the field No Field", e.getMessage());
        }
    }

    @Test
    public void testSetUDF()
    {
        List<UDF> udfs = new ArrayList<UDF>();

        try
        {
            UDF.setUDF(null, null, null);
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
        assertNull("Set UDF to null should return null", udf);

        udf = UDF.setUDF(udfs, "Field1", "Hello");
        assertNotNull("Set UDF didn't return a field", udf);
        assertEquals("Set UDF didn't add to the collection", 1, udfs.size());
        assertEquals("Field returned name wrong", "Field1", udf.getName());
        assertEquals("Field returned type wrong", null, udf.getType());
        assertEquals("Field returned value wrong", "Hello", udf.getValue());

        udf = UDF.setUDF(udfs, "Field2", "2.45");
        assertNotNull("Set UDF didn't return a field", udf);
        assertEquals("Set UDF didn't add to the collection", 2, udfs.size());

        udf = UDF.setUDF(udfs, "Field1", "Moving on");
        assertNotNull("Set UDF didn't return a field", udf);
        assertEquals("Set UDF added when it should have just set", 2, udfs.size());

        udf = UDF.setUDF(udfs, "Field1", null);
        assertNull("Set UDF to null returned a field", udf);
        assertEquals("Set UDF to null hasn't removed from the collection", 1, udfs.size());
    }

    @Test
    public void testGetUdfCollection()
    {
        Object o1 = new Artifact();

        Collection<UDF> udfs = UDF.getUDFCollection(o1);
        assertNotNull("No UDFs returned", udfs);

        Object o2 = new Object();

        try
        {
            UDF.getUDFCollection(o2);
            fail("Fetched UDF collection from a class without getUserDefinedFields method.");
        }
        catch (IllegalArgumentException e)
        {
            // Correct.
        }
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
        assertNotNull("No UDF found", udf);

        udf = UDF.getUDF(null, "User Comments");
        assertNull("UDF found in null collection", udf);

        udf = UDF.getUDF(a, "No Field");
        assertNull("UDF found", udf);

        try
        {
            udf = UDF.getUDF(a, "User Comments", true);
            assertNotNull("No UDF found", udf);
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            udf = UDF.getUDF((Artifact)null, "User Comments", true);
            fail("No exception with UDF from null entity.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"User Comments\" does not exist because 'thing' is null.", e.getMessage());
        }

        try
        {
            udf = UDF.getUDF(a, "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"No Field\" does not exist on Artifact ADM2A8PA1", e.getMessage());
        }

        try
        {
            udf = UDF.getUDF(a, "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "Missing the field No Field", e.getMessage());
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
        assertNotNull("No UDF found", value);

        value = UDF.getUDFValue(null, "User Comments");
        assertNull("UDF found in null collection", value);

        value = UDF.getUDFValue(a, "No Field");
        assertNull("UDF found", value);

        value = UDF.getUDFValue(a, "No Field", "The default");
        assertEquals("UDF found and no default value", "The default", value);

        try
        {
            value = UDF.getUDFValue(a, "User Comments", true);
            assertNotNull("No UDF found", value);
        }
        catch (MissingUDFException e)
        {
            fail(e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue((Artifact)null, "User Comments", true);
            fail("No exception with UDF from null entity.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"User Comments\" does not exist because 'thing' is null.", e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue(a, "No Field", true);
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "UDF \"No Field\" does not exist on Artifact ADM2A8PA1", e.getMessage());
        }

        try
        {
            value = UDF.getUDFValue(a, "No Field", true, "Missing the field No Field");
            fail("No exception with missing UDF.");
        }
        catch (MissingUDFException e)
        {
            assertEquals("Exception message wrong", "Missing the field No Field", e.getMessage());
        }
    }
}
