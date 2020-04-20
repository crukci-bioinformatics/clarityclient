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

import static org.junit.Assert.*;

import static org.cruk.genologics.api.jaxb.URIAdapter.removeStateParameter;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class URIAdapterTest
{

    @Test
    public void testRemoveStateParameterString()
    {
        String uriRoot = "http://limsdev.cri.camres.org:8080/api/v2";

        String noQuery = removeStateParameter(uriRoot);
        assertEquals("No query URI wrong", uriRoot, noQuery);

        String stateOnlyQuery = removeStateParameter(uriRoot + "?state=678");
        assertEquals("State only query URI wrong", uriRoot, stateOnlyQuery);

        String noStateQuery = removeStateParameter(uriRoot + "?start-index=1023");
        assertEquals("No state query URI wrong", uriRoot + "?start-index=1023", noStateQuery);

        String stateAndPageQuery = removeStateParameter(uriRoot + "?state=123&start-index=234");
        assertEquals("State and page query URI wrong", uriRoot + "?start-index=234", stateAndPageQuery);

        String pageAndStateQuery = removeStateParameter(uriRoot + "?start-index=234&state=123");
        assertEquals("Page and state query URI wrong", uriRoot + "?start-index=234", pageAndStateQuery);

        String startStateQuery = removeStateParameter(uriRoot + "?state=123&start-index=234&batch-size=100");
        assertEquals("Middle state query URI wrong", uriRoot + "?start-index=234&batch-size=100", startStateQuery);

        String middleStateQuery = removeStateParameter(uriRoot + "?start-index=234&state=123&batch-size=100");
        assertEquals("Middle state query URI wrong", uriRoot + "?start-index=234&batch-size=100", middleStateQuery);

        String endStateQuery = removeStateParameter(uriRoot + "?start-index=234&batch-size=100&state=123");
        assertEquals("End state query URI wrong", uriRoot + "?start-index=234&batch-size=100", endStateQuery);

        String multipleStateQuery1 = removeStateParameter(uriRoot + "?state=123&start-index=234&state=123");
        assertEquals("Multiple state query 1 URI wrong", uriRoot + "?start-index=234", multipleStateQuery1);

        String multipleStateQuery2 = removeStateParameter(uriRoot + "?state=123&start-index=234&state=123&batch-size=100&state=123");
        assertEquals("Multiple state query 2 URI wrong", uriRoot + "?start-index=234&batch-size=100", multipleStateQuery2);

        String multipleStateQuery3 = removeStateParameter(uriRoot + "?state=123&start-index=234&state=123&state=123&batch-size=100");
        assertEquals("Multiple state query 3 URI wrong", uriRoot + "?start-index=234&batch-size=100", multipleStateQuery3);
    }

    @Test
    public void testRemoveStateParameterURI()
    {
        try
        {
            String uriRoot = "http://limsdev.cri.camres.org:8080/api/v2";

            URI noQuery = removeStateParameter(new URI(uriRoot));
            assertEquals("No query URI wrong", uriRoot, noQuery.toString());

            URI stateOnlyQuery = removeStateParameter(new URI(uriRoot + "?state=678"));
            assertEquals("State only query URI wrong", uriRoot, stateOnlyQuery.toString());

            URI noStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=1023"));
            assertEquals("No state query URI wrong", uriRoot + "?start-index=1023", noStateQuery.toString());

            URI stateAndPageQuery = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234"));
            assertEquals("State and page query URI wrong", uriRoot + "?start-index=234", stateAndPageQuery.toString());

            URI pageAndStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=234&state=123"));
            assertEquals("Page and state query URI wrong", uriRoot + "?start-index=234", pageAndStateQuery.toString());

            URI startStateQuery = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&batch-size=100"));
            assertEquals("Middle state query URI wrong", uriRoot + "?start-index=234&batch-size=100", startStateQuery.toString());

            URI middleStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=234&state=123&batch-size=100"));
            assertEquals("Middle state query URI wrong", uriRoot + "?start-index=234&batch-size=100", middleStateQuery.toString());

            URI endStateQuery = removeStateParameter(new URI(uriRoot + "?start-index=234&batch-size=100&state=123"));
            assertEquals("End state query URI wrong", uriRoot + "?start-index=234&batch-size=100", endStateQuery.toString());

            URI multipleStateQuery1 = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&state=123"));
            assertEquals("Multiple state query 1 URI wrong", uriRoot + "?start-index=234", multipleStateQuery1.toString());

            URI multipleStateQuery2 = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&state=123&batch-size=100&state=123"));
            assertEquals("Multiple state query 2 URI wrong", uriRoot + "?start-index=234&batch-size=100", multipleStateQuery2.toString());

            URI multipleStateQuery3 = removeStateParameter(new URI(uriRoot + "?state=123&start-index=234&state=123&state=123&batch-size=100"));
            assertEquals("Multiple state query 3 URI wrong", uriRoot + "?start-index=234&batch-size=100", multipleStateQuery3.toString());
        }
        catch (URISyntaxException e)
        {
            fail("One of the URIs produced is invalid: " + e.getMessage());
        }
    }
}
