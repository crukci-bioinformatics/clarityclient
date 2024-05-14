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

package org.cruk.clarity.api.debugging;

import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.cruk.clarity.api.jaxb.JaxbMarshallingTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * Aspect used to examine calls from the REST template to the
 * Clarity API. If logging is set to DEBUG, this class will print the
 * information being sent to the API and the response received.
 *
 * @see RestTemplate
 */
@Aspect
@Component("clarityRestClientSnoopingAspect")
public class RestClientSnoopingAspect
{
    private Logger logger = LoggerFactory.getLogger(RestClientSnoopingAspect.class);

    /**
     * The tool to convert an object back into XML for printing.
     */
    private JaxbMarshallingTool marshaller;


    public RestClientSnoopingAspect()
    {
    }

    public RestClientSnoopingAspect(JaxbMarshallingTool marshaller)
    {
        setMarshaller(marshaller);
    }

    /**
     * Set the tool to convert an object back into XML for printing.
     *
     * @param marshaller The marshalling tool.
     */
    @Autowired
    @Qualifier("clarityMarshallingTool")
    public void setMarshaller(JaxbMarshallingTool marshaller)
    {
        this.marshaller = marshaller;
    }

    /**
     * Join point for the rest template's {@code get} operations.
     * Logs the URI being fetched and the reply received.
     *
     * @param pjp The AspectJ join point object.
     *
     * @return The reply object.
     *
     * @throws Throwable if there is any failure from the operation.
     * This is also logged if logging is set to DEBUG.
     *
     * @see RestTemplate#getForEntity(java.net.URI, Class)
     * @see RestTemplate#getForObject(java.net.URI, Class)
     */
    @Around("execution(public * get*(..)) and bean(clarityRestTemplate)")
    public Object checkGet(ProceedingJoinPoint pjp) throws Throwable
    {
        Object uri = pjp.getArgs()[0];
        Class<?> type = (Class<?>)pjp.getArgs()[1];

        if (logger.isDebugEnabled())
        {
            logger.debug("Requesting a {} with {} from {}",
                    ClassUtils.getShortClassName(type), pjp.getSignature().getName(), uri);
        }

        try
        {
            Object reply = pjp.proceed();

            if (logger.isDebugEnabled())
            {
                displayAfter(reply);
            }

            return reply;
        }
        catch (Throwable e)
        {
            fail(e);
            throw e;
        }
    }

    /**
     * Join point for the rest template's {@code put} and {@code post} operations.
     * Logs the XML being sent and the reply received.
     *
     * @param pjp The AspectJ join point object.
     *
     * @return The reply object.
     *
     * @throws Throwable if there is any failure from the operation.
     * This is also logged if logging is set to DEBUG.
     *
     * @see RestTemplate#put(java.net.URI, Object)
     * @see RestTemplate#postForEntity(java.net.URI, Object, Class)
     * @see RestTemplate#postForObject(java.net.URI, Object, Class)
     */
    @Around("(execution(public * put*(..)) or execution(public * post*(..))) and bean(clarityRestTemplate)")
    public Object checkPutOrPost(ProceedingJoinPoint pjp) throws Throwable
    {
        Object uri = pjp.getArgs()[0];
        Object request = pjp.getArgs()[1];

        if (logger.isDebugEnabled())
        {
            logger.debug("Calling {} to {}", pjp.getSignature().getName(), uri);

            displayBefore(request);
        }

        try
        {
            Object reply = pjp.proceed();

            if (logger.isDebugEnabled())
            {
                displayAfter(reply);
            }

            return reply;
        }
        catch (Throwable e)
        {
            fail(e);
            throw e;
        }
    }

    /**
     * Join point for the rest template's {@code exchange} operations.
     * Logs the XML being sent and the reply received.
     *
     * @param pjp The AspectJ join point object.
     *
     * @return The reply object.
     *
     * @throws Throwable if there is any failure from the operation.
     * This is also logged if logging is set to DEBUG.
     *
     * @see RestTemplate#exchange(java.net.URI, HttpMethod, HttpEntity, Class)
     */
    @Around("execution(public * exchange*(..)) and bean(clarityRestTemplate)")
    public Object checkExchange(ProceedingJoinPoint pjp) throws Throwable
    {
        Object uri = pjp.getArgs()[0];
        HttpMethod method = (HttpMethod)pjp.getArgs()[1];
        HttpEntity<?> entity = (HttpEntity<?>)pjp.getArgs()[2];
        Object request = entity.getBody();

        if (logger.isDebugEnabled())
        {
            logger.debug("Calling {} via {} to {}", pjp.getSignature().getName(), method, uri);

            displayBefore(request);
        }

        try
        {
            Object reply = pjp.proceed();

            if (logger.isDebugEnabled())
            {
                displayAfter(reply);
            }

            return reply;
        }
        catch (Throwable e)
        {
            fail(e);
            throw e;
        }
    }

    /**
     * Join point for the rest template's {@code exchange} operations.
     * Logs the URI being deleted.
     *
     * @param pjp The AspectJ join point object.
     *
     * @return The reply object.
     *
     * @throws Throwable if there is any failure from the operation.
     * This is also logged if logging is set to DEBUG.
     *
     * @see RestTemplate#delete(java.net.URI)
     */
    @Around("execution(public * delete*(..)) and bean(clarityRestTemplate)")
    public Object checkDelete(ProceedingJoinPoint pjp) throws Throwable
    {
        Object uri = pjp.getArgs()[0];

        if (logger.isDebugEnabled())
        {
            logger.debug("Deleting with {} from {}", pjp.getSignature().getName(), uri);
        }

        try
        {
            return pjp.proceed();
        }
        catch (Throwable e)
        {
            fail(e);
            throw e;
        }
    }

    /**
     * Marshal the given object to XML and log it for XML being sent to the server.
     *
     * @param thing The object being sent.
     */
    private void displayBefore(Object thing)
    {
        try
        {
            String xml = marshaller.marshal(thing);
            logger.debug("Sending:\n{}", xml);
        }
        catch (Exception e)
        {
            logger.debug("Request cannot be marshalled: {}", e.getMessage());
        }
    }

    /**
     * Marshal the given object to XML and log it for XML received from to the server.
     *
     * @param reply The object received. If this is a {@code ResponseEntity},
     * then the status of the call is also available and logged.
     */
    private void displayAfter(Object reply)
    {
        Object thing = reply;
        if (reply instanceof ResponseEntity<?> response)
        {
            thing = response.getBody();
            logger.debug("Reply status is {}", response.getStatusCode());
        }

        if (thing != null)
        {
            try
            {
                String xml = marshaller.marshal(thing);
                logger.debug("Response is:\n{}", xml);
            }
            catch (Exception e)
            {
                logger.debug("Response cannot be marshalled: {}", e.getMessage());
            }
        }
    }

    /**
     * Called when something about the REST call has failed. This may be an error
     * from the server (a {@code ClarityException}) or it may be an error
     * in the code nearer the call than this aspect.
     *
     * @param e The failure.
     *
     * @throws Throwable Rethrows {@code e} after logging its information.
     */
    private void fail(Throwable e) throws Throwable
    {
        logger.debug("Call failed with {}: {}", ClassUtils.getShortClassName(e.getClass()), e.getMessage());
        throw e;
    }
}
