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

import org.aspectj.lang.annotation.Aspect;
import org.cruk.genologics.api.GenologicsException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Aspect that wraps the unmarshalling of responses from the
 * Genologics API and, if the reply is found to be an exception,
 * converts the reply into a Java exception and throws it.
 *
 * @see com.genologics.ri.exception.Exception
 * @see GenologicsException
 */
@Aspect
public class GenologicsExceptionAspect
{
    /**
     * Join point that tests whether the reply object is an
     * error and, if so, creates a Java exception.
     *
     * @param unmarshalled The object unmarshalled from the API reply.
     *
     * @return {@code unmarshalled}, unless it is an error.
     *
     * @throws GenologicsException if {@code unmarshalled} is an error
     * message. It is created with the fields from the XML error.
     *
     * @see Jaxb2Marshaller#unmarshal(javax.xml.transform.Source)
     */
    public Object objectUnmarshalled(Object unmarshalled)
    {
        if (unmarshalled instanceof com.genologics.ri.exception.Exception)
        {
            throw new GenologicsException((com.genologics.ri.exception.Exception)unmarshalled);
        }

        return unmarshalled;
    }
}
