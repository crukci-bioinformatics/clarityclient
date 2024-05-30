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

import jakarta.xml.bind.JAXBElement;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.cruk.clarity.api.ClarityException;
import org.springframework.stereotype.Component;

/**
 * Aspect that wraps the unmarshalling of responses from the
 * Clarity API. Changes the returned value from a {@code JAXBElement}
 * (seemingly now what is returned from Spring's {@code Jaxb2Marshaller}
 * as of Spring 4) to the actual object and, if the reply is found to be
 * an exception from the API, converts the reply into a Java exception
 * and throws it.
 *
 * @see com.genologics.ri.exception.Exception
 * @see ClarityException
 * @see JAXBElement
 */
@Aspect
@Component("clarityUnmarshallingAspect")
@SuppressWarnings("exports")
public class JaxbUnmarshallingAspect
{
    /**
     * Join point that replaces the JAXBElement object with the
     * real object it contains, then tests whether the reply object is an
     * error and, if so, throws a Java exception.
     *
     * @param pjp The AspectJ join point object. The return value from
     * its {@code proceed} call gives the result of unmarshalling.
     *
     * @return The real object extracted from {@code unmarshalled}
     * (if {@code unmarshalled} is a JAXBElement).
     *
     * @throws ClarityException if {@code unmarshalled} is an error
     * message. It is created with the fields from the XML error.
     *
     * @throws Throwable if there is another throwable from continuing
     * the join point control chain.
     */
    @Around("execution(public * unmarshal(..)) and bean(clarityJaxbUnmarshaller)")
    public Object objectUnmarshalled(ProceedingJoinPoint pjp) throws Throwable
    {
        Object unmarshalled = pjp.proceed();

        if (unmarshalled instanceof JAXBElement<?> element)
        {
            unmarshalled = element.getValue();
        }
        if (unmarshalled instanceof com.genologics.ri.exception.Exception ge)
        {
            throw new ClarityException(ge);
        }

        return unmarshalled;
    }
}
