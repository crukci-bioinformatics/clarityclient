/*
 * CRUK-CI Genologics REST API Java Client.
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

package org.cruk.genologics.api.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.cruk.genologics.api.GenologicsAPI;
import org.springframework.beans.factory.annotation.Required;

/**
 * Aspect to ensure that the API will not fetch the latest versions of stateful entities
 * on the next call.
 *
 * @since 2.24.8
 */
@Aspect
public class LatestVersionsResetAspect
{
    /**
     * API method names after whose call the latest versions flag should not be reset.
     *
     * @see #fetchStatefulVersions(JoinPoint)
     */
    private static final Set<String> NO_RESET_METHODS =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("overrideStateful", "getStatefulOverride", "cancelStatefulOverride")));

    /**
     * The API this aspect will call through to.
     */
    protected GenologicsAPI api;


    /**
     * Constructor.
     */
    public LatestVersionsResetAspect()
    {
    }

    /**
     * Sets the API being used.
     *
     * @param api The GenologicsAPI bean.
     */
    @Required
    public void setGenologicsAPI(GenologicsAPI api)
    {
        this.api = api;
    }

    /**
     * Method invoked after any call on the API to not fetch the latest versions
     * of stateful entities on the next call.
     *
     * @param jp The join point.
     *
     * @see GenologicsAPI#cancelStatefulOverride(String)
     */
    public void cancelStatefulOverride(JoinPoint jp)
    {
        String methodName = jp.getSignature().getName();

        if (!NO_RESET_METHODS.contains(methodName))
        {
            api.cancelStatefulOverride(methodName);
        }
    }
}
