/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2019 Cancer Research UK Cambridge Institute.
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

package org.cruk.clarity.api.impl;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Aspect to ensure that the API will not fetch the latest versions of stateful entities
 * on the next call.
 *
 * @since 2.24.8
 */
@Aspect
@Component("clarityLatestVersionsResetAspect")
public class LatestVersionsResetAspect
{
    /**
     * API method names after whose call the latest versions flag should not be reset.
     * These are any methods that appear in the {@link ClarityAPIInternal} interface
     * plus {@code overrideStateful} from the main interface.
     *
     * @see #cancelStatefulOverride(JoinPoint)
     */
    private static final Set<String> NO_RESET_METHODS;

    /**
     * The API instance through its internal access methods.
     */
    protected ClarityAPIInternal api;


    /**
     * Static initialiser. Set up the method names to exclude from resetting the override
     * behaviour (all methods on the ClarityAPIInternalAccess interface).
     */
    static
    {
        Set<String> names = new HashSet<String>();
        names.add("overrideStateful");
        for (Method method : ClarityAPIInternal.class.getMethods())
        {
            names.add(method.getName());
        }
        NO_RESET_METHODS = Collections.unmodifiableSet(names);
    }

    /**
     * Constructor.
     */
    public LatestVersionsResetAspect()
    {
    }

    public LatestVersionsResetAspect(ClarityAPIInternal api)
    {
        setClarityAPI(api);
    }

    /**
     * Sets the API being used.
     *
     * @param api The ClarityAPI bean.
     */
    @Autowired
    @Qualifier("clarityAPI")
    public void setClarityAPI(ClarityAPIInternal api)
    {
        this.api = api;
    }

    /**
     * Method invoked after any call on the API to not fetch the latest versions
     * of stateful entities on the next call.
     *
     * @param jp The join point.
     *
     * @see ClarityAPIInternal#cancelStatefulOverride(String)
     */
    @After("execution(public * *(..)) and bean(clarityAPI)")
    public void cancelStatefulOverride(JoinPoint jp)
    {
        String methodName = jp.getSignature().getName();

        if (!NO_RESET_METHODS.contains(methodName))
        {
            api.cancelStatefulOverride(methodName);
        }
    }
}
