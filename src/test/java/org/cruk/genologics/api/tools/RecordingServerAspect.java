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
import java.util.Collection;

import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.genologics.ri.LimsEntity;
import com.genologics.ri.Locatable;

@Aspect
public class RecordingServerAspect
{
    private File messageDirectory = new File("src/test/serverexchanges");

    private Jaxb2Marshaller jaxbMarshaller;

    public RecordingServerAspect()
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

    public Object doLoad(ProceedingJoinPoint pjp) throws Throwable
    {
        Object thing = pjp.proceed();

        writeEntity(thing);

        return thing;
    }

    public Object doLoadAll(ProceedingJoinPoint pjp) throws Throwable
    {
        Collection<?> list = (Collection<?>)pjp.proceed();

        for (Object thing : list)
        {
            writeEntity(thing);
        }

        return list;
    }

    private void writeEntity(Object thing)
    {
        try
        {
            String id;
            if (thing instanceof LimsEntity<?>)
            {
                id = ((LimsEntity<?>)thing).getLimsid();
            }
            else
            {
                id = ((Locatable)thing).getUri().toString();
                int lastSlash = id.lastIndexOf('/');
                id = id.substring(lastSlash + 1);
            }

            String fileName = ClassUtils.getShortClassName(thing.getClass()) + "-" + id + ".xml";
            File file = new File(messageDirectory, fileName);

            jaxbMarshaller.marshal(thing, new StreamResult(file));
        }
        catch (Exception e)
        {
            // Ignore.
        }
    }
}
