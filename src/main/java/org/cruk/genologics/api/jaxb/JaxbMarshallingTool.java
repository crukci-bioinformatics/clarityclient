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

package org.cruk.genologics.api.jaxb;

import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


/**
 * Tool to help debugging: a short cut for marshalling an object to a string.
 *
 * <p>Used to help debugging of the REST client by marshalling the object
 * back into XML form for printing.</p>
 */
public class JaxbMarshallingTool
{
    /**
     * The marshaller.
     */
    private Jaxb2Marshaller marshaller;

    /**
     * Set the marshaller used to create the XML.
     *
     * @param marshaller The Jaxb2Marshaller.
     */
    @Required
    public void setMarshaller(Jaxb2Marshaller marshaller)
    {
        this.marshaller = marshaller;
    }

    /**
     * Marshals the object into a string of XML.
     *
     * @param thing The object to marshal.
     *
     * @return The XML string.
     */
    public String marshal(Object thing)
    {
        StringWriter writer = new StringWriter(8192);
        marshaller.marshal(thing, new StreamResult(writer));
        return writer.toString();
    }
}
