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

package org.cruk.genologics.api.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.genologics.ri.reagenttype.ReagentTypes;

@Aspect
public class PrerecordedServerAspect
{
    private File messageDirectory = new File("src/test/serverexchanges");

    private Jaxb2Marshaller jaxbMarshaller;

    public PrerecordedServerAspect()
    {
    }

    public void setMessageDirectory(File messageDirectory)
    {
        this.messageDirectory = messageDirectory;
    }

    @Required
    public void setJaxbMarshaller(Jaxb2Marshaller jaxbMarshaller)
    {
        this.jaxbMarshaller = jaxbMarshaller;
    }

    public Object doGet(ProceedingJoinPoint pjp) throws Throwable
    {
        Object uriObj = pjp.getArgs()[0];
        Class<?> type = (Class<?>)pjp.getArgs()[1];

        File file;

        if (type.equals(ReagentTypes.class))
        {
            file = new File(messageDirectory, "ReagentTypes.xml");
        }
        else
        {
            URI uri;
            try
            {
                uri = (URI)uriObj;
            }
            catch (ClassCastException e)
            {
                uri = new URI(uriObj.toString());
            }

            String path = uri.getPath();

            String limsid = path.substring(path.lastIndexOf('/') + 1);

            String filename = ClassUtils.getShortClassName(type) + "-" + limsid + ".xml";
            file = new File(messageDirectory, filename);
        }

        if (!file.exists())
        {
            throw new FileNotFoundException("There is no file " + file.getName() + " recorded.");
        }

        Object thing = jaxbMarshaller.unmarshal(new StreamSource(file));

        return thing;
    }

    public ResponseEntity<?> doGetEntity(ProceedingJoinPoint pjp) throws Throwable
    {
        ResponseEntity<?> response;
        try
        {
            Object thing = doGet(pjp);

            response = new ResponseEntity<Object>(thing, HttpStatus.OK);
        }
        catch (FileNotFoundException e)
        {
            response = new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }

        return response;
    }
}
