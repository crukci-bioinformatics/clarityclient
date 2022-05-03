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

package org.cruk.clarity.api.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.net.URI;

import org.junit.jupiter.api.Test;

import com.genologics.ri.Locatable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.ArtifactLink;

public class ClarityAPIImplTest
{
    @Test
    public void testRemoveStateParameter() throws Throwable
    {
        ClarityAPIImpl api = new ClarityAPIImpl();

        final String base = "https://limsdev.cruk.cam.ac.uk/api/v2/artifacts/1234";

        URI original = new URI(base);
        URI stripped = api.removeStateParameter(original);
        assertEquals(original.toString(), stripped.toString(), "URI without state changed");

        original = new URI(base + "?state=1234");
        stripped = api.removeStateParameter(original);
        assertEquals(base, stripped.toString(), "State not removed");

        original = new URI(base + "?state=1234&type=Hello");
        stripped = api.removeStateParameter(original);
        assertEquals(base + "?type=Hello", stripped.toString(), "State not removed");

        original = new URI(base + "?type=Hello&state=1234");
        stripped = api.removeStateParameter(original);
        assertEquals(base + "?type=Hello", stripped.toString(), "State not removed");

        original = new URI(base + "?type=Hello&state=1234&name=What");
        stripped = api.removeStateParameter(original);
        assertEquals(base + "?type=Hello&name=What", stripped.toString(), "State not removed");

        original = new URI(base + "?type=Hello&state=1234&&name=What");
        stripped = api.removeStateParameter(original);
        assertEquals(base + "?type=Hello&name=What", stripped.toString(), "State not removed");
    }

    @Test
    public void testClassOfEntity() throws Exception
    {
        ClarityAPIImpl api = new ClarityAPIImpl();

        Method classOfEntity = ClarityAPIImpl.class.getDeclaredMethod("classOfEntity", Locatable.class);
        classOfEntity.setAccessible(true);

        Artifact a = new Artifact();

        assertEquals(Artifact.class, classOfEntity.invoke(api, a), "Class of entity for " + a.getClass().getName() + " is wrong.");

        ArtifactLink link = new ArtifactLink(a);

        assertEquals(Artifact.class, classOfEntity.invoke(api, link), "Class of entity for " + link.getClass().getName() + " is wrong.");
    }
}
