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

package org.cruk.clarity.api.jaxb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.cruk.clarity.api.ClarityException;
import org.cruk.clarity.api.unittests.ClarityClientTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class JaxbUnmarshallingAspectTest
{
    @Autowired
    protected Unmarshaller unmarshaller;

    public JaxbUnmarshallingAspectTest()
    {
    }

    @Test
    public void testExceptionAspect() throws IOException
    {
        Source source = new StreamSource(new File("src/test/xml/exceptionaspecttest-exception.xml"));

        try
        {
            unmarshaller.unmarshal(source);
            fail("Didn't get a ClarityException when unmarshalling an exception.");
        }
        catch (ClarityException e)
        {
            assertEquals("Could not find resource for relative : /v2/process-executions of full path: http://limsdev.cri.camres.org:8080/api/v2/process-executions",
                         e.getMessage(),
                         "Exception message wrong");
            assertEquals("Highest", e.getCategory(), "Exception category wrong");
            assertEquals("Check URL", e.getSuggestedActions(), "Exception suggested actions wrong");
            assertEquals("ACODE", e.getCode(), "Exception code wrong");
        }
    }
}
