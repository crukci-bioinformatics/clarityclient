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

package org.cruk.genologics.api.impl;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;

public class GenologicsAPIImplTest
{
    @Test
    public void testRemoveStateParameter() throws Throwable
    {
        GenologicsAPIImpl api = new GenologicsAPIImpl();

        final String base = "https://limsdev.cruk.cam.ac.uk/api/v2/artifacts/1234";

        URI original = new URI(base);
        URI stripped = api.removeStateParameter(original);
        assertEquals("URI without state changed", original.toString(), stripped.toString());

        original = new URI(base + "?state=1234");
        stripped = api.removeStateParameter(original);
        assertEquals("State not removed", base, stripped.toString());

        original = new URI(base + "?state=1234&type=Hello");
        stripped = api.removeStateParameter(original);
        assertEquals("State not removed", base + "?type=Hello", stripped.toString());

        original = new URI(base + "?type=Hello&state=1234");
        stripped = api.removeStateParameter(original);
        assertEquals("State not removed", base + "?type=Hello", stripped.toString());

        original = new URI(base + "?type=Hello&state=1234&name=What");
        stripped = api.removeStateParameter(original);
        assertEquals("State not removed", base + "?type=Hello&name=What", stripped.toString());

        original = new URI(base + "?type=Hello&state=1234&&name=What");
        stripped = api.removeStateParameter(original);
        assertEquals("State not removed", base + "?type=Hello&name=What", stripped.toString());
    }

}
