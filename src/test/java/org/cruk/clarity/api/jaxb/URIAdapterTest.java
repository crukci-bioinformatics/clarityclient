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

import static org.cruk.clarity.api.jaxb.URIAdapter.removeStateParameter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;


public class URIAdapterTest
{
    @Test
    public void testRemoveStateParameterString()
    {
        String uriRoot = "http://limsdev.cri.camres.org:8080/api/v2";

        String noQuery = removeStateParameter(uriRoot);
        assertEquals(uriRoot, noQuery, "No query URI wrong");

        String stateOnlyQuery = removeStateParameter(uriRoot + "?state=678");
        assertEquals(uriRoot, stateOnlyQuery, "State only query URI wrong");

        String noStateQuery = removeStateParameter(uriRoot + "?start-index=1023");
        assertEquals(uriRoot + "?start-index=1023", noStateQuery, "No state query URI wrong");

        String stateAndPageQuery = removeStateParameter(uriRoot + "?state=123&start-index=234");
        assertEquals(uriRoot + "?start-index=234", stateAndPageQuery, "State and page query URI wrong");

        String pageAndStateQuery = removeStateParameter(uriRoot + "?start-index=234&state=123");
        assertEquals(uriRoot + "?start-index=234", pageAndStateQuery, "Page and state query URI wrong");

        String startStateQuery = removeStateParameter(uriRoot + "?state=123&start-index=234&batch-size=100");
        assertEquals(uriRoot + "?start-index=234&batch-size=100", startStateQuery, "Middle state query URI wrong");

        String middleStateQuery = removeStateParameter(uriRoot + "?start-index=234&state=123&batch-size=100");
        assertEquals(uriRoot + "?start-index=234&batch-size=100", middleStateQuery, "Middle state query URI wrong");

        String endStateQuery = removeStateParameter(uriRoot + "?start-index=234&batch-size=100&state=123");
        assertEquals(uriRoot + "?start-index=234&batch-size=100", endStateQuery, "End state query URI wrong");

        String multipleStateQuery1 = removeStateParameter(uriRoot + "?state=123&start-index=234&state=123");
        assertEquals(uriRoot + "?start-index=234", multipleStateQuery1, "Multiple state query 1 URI wrong");

        String multipleStateQuery2 = removeStateParameter(uriRoot + "?state=123&start-index=234&state=123&batch-size=100&state=123");
        assertEquals(uriRoot + "?start-index=234&batch-size=100", multipleStateQuery2, "Multiple state query 2 URI wrong");

        String multipleStateQuery3 = removeStateParameter(uriRoot + "?state=123&start-index=234&state=123&state=123&batch-size=100");
        assertEquals(uriRoot + "?start-index=234&batch-size=100", multipleStateQuery3, "Multiple state query 3 URI wrong");
    }

    @Test
    public void testRemoveStateParameterURI()
    {
        try
        {
            String uriRoot = "http://limsdev.cri.camres.org:8080/api/v2";

            URI noQuery = removeStateParameter(new URI(uriRoot));
            assertEquals(uriRoot, noQuery.toString(), "No query URI wrong");

            URI stateOnlyQuery = removeStateParameter(new URI(uriRoot + "?state=678"));
            assertEquals(uriRoot, stateOnlyQuery.toString(), "State only query URI wrong");

            URI noStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=1023"));
            assertEquals(uriRoot + "?start-index=1023", noStateQuery.toString(), "No state query URI wrong");

            URI stateAndPageQuery = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234"));
            assertEquals(uriRoot + "?start-index=234", stateAndPageQuery.toString(), "State and page query URI wrong");

            URI pageAndStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=234&state=123"));
            assertEquals(uriRoot + "?start-index=234", pageAndStateQuery.toString(), "Page and state query URI wrong");

            URI startStateQuery = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&batch-size=100"));
            assertEquals(uriRoot + "?start-index=234&batch-size=100", startStateQuery.toString(), "Middle state query URI wrong");

            URI middleStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=234&state=123&batch-size=100"));
            assertEquals(uriRoot + "?start-index=234&batch-size=100", middleStateQuery.toString(), "Middle state query URI wrong");

            URI endStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=234&batch-size=100&state=123"));
            assertEquals(uriRoot + "?start-index=234&batch-size=100", endStateQuery.toString(), "End state query URI wrong");

            URI multipleStateQuery1 = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&state=123"));
            assertEquals(uriRoot + "?start-index=234", multipleStateQuery1.toString(), "Multiple state query 1 URI wrong");

            URI multipleStateQuery2 = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&state=123&batch-size=100&state=123"));
            assertEquals(uriRoot + "?start-index=234&batch-size=100", multipleStateQuery2.toString(), "Multiple state query 2 URI wrong");

            URI multipleStateQuery3 = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&state=123&state=123&batch-size=100"));
            assertEquals(uriRoot + "?start-index=234&batch-size=100", multipleStateQuery3.toString(), "Multiple state query 3 URI wrong");
        }
        catch (URISyntaxException e)
        {
            fail("One of the URIs produced is invalid: " + e.getMessage());
        }
    }
}
