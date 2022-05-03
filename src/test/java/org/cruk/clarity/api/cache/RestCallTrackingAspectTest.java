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

package org.cruk.clarity.api.cache;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class RestCallTrackingAspectTest
{

    @Test
    public void testIsSearch()
    {
        RestCallTrackingAspect aspect = new RestCallTrackingAspect();

        String base = "http://limsdev.cri.camres.org:8080/api/v2/projects/";

        assertFalse(aspect.isSearch(base + "3"), "Search test failed");
        assertFalse(aspect.isSearch(base + "3?state=1234"), "Search test failed");
        assertTrue(aspect.isSearch(base + "3?name=hello"), "Search test failed");
        assertTrue(aspect.isSearch(base + "3?name=hello&state=1234"), "Search test failed");
        assertTrue(aspect.isSearch(base + "3?state=1234&name=hello"), "Search test failed");

        base = "http://limsdev.cri.camres.org:8080/api/v2/artifacts/";

        assertFalse(aspect.isSearch(base + "MOH1A14SAM1"), "Search test failed");
        assertFalse(aspect.isSearch(base + "MOH1A14SAM1?state=1234"), "Search test failed");
        assertTrue(aspect.isSearch(base + "MOH1A14SAM1?name=hello"), "Search test failed");
        assertTrue(aspect.isSearch(base + "MOH1A14SAM1?name=hello&state=1234"), "Search test failed");
        assertTrue(aspect.isSearch(base + "MOH1A14SAM1?state=1234&name=hello"), "Search test failed");
    }

}
