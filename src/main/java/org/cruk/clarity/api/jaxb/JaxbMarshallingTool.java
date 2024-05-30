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

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Component;


/**
 * Tool to help debugging: a short cut for marshalling an object to a string.
 *
 * <p>Used to help debugging of the REST client by marshalling the object
 * back into XML form for printing.</p>
 */
@Component("clarityMarshallingTool")
public class JaxbMarshallingTool
{
    /**
     * The marshaller.
     */
    private Marshaller marshaller;

    public JaxbMarshallingTool()
    {
    }

    public JaxbMarshallingTool(Marshaller marshaller)
    {
        setMarshaller(marshaller);
    }

    /**
     * Set the marshaller used to create the XML.
     *
     * @param marshaller The Jaxb Marshaller.
     */
    @Autowired
    @Qualifier("clarityJaxbMarshaller")
    public void setMarshaller(Marshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    /**
     * Marshals the object into a string of XML.
     *
     * @param thing The object to marshal.
     *
     * @return The XML string.
     *
     * @throws XmlMappingException if there is a problem marshalling the object.
     */
    public String marshal(Object thing) throws XmlMappingException
    {
        try
        {
            StringWriter writer = new StringWriter(8192);
            marshaller.marshal(thing, new StreamResult(writer));
            return writer.toString();
        }
        catch (IOException e)
        {
            // Should never happen, as writing to a String.
            throw new MarshallingFailureException("Got an IOException while marshalling to a string:", e);
        }
    }
}
