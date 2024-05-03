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

package org.cruk.clarity.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cruk.clarity.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.clarity.api.impl.ClarityAPIImpl;
import org.junit.jupiter.api.Test;

import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.process.ClarityProcess;
import com.genologics.ri.project.Project;
import com.genologics.ri.protocolconfiguration.Protocol;
import com.genologics.ri.stage.Stage;
import com.genologics.ri.step.Actions;
import com.genologics.ri.stepconfiguration.ProtocolStep;
import com.genologics.ri.workflowconfiguration.Workflow;

public class ClarityAPITest
{
    static final String URI_BASE = "http://limsdev.cruk.cam.ac.uk:8080";

    @Test
    public void testLimsIdToUri1() throws Exception
    {
        ClarityAPIImpl api = new ClarityAPIImpl();
        api.setHttpRequestFactory(mock(AuthenticatingClientHttpRequestFactory.class));

        try
        {
            api.limsIdToUri(null, null);
            fail("limsIdToUri succeeded with a null lims id");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("", null);
            fail("limsIdToUri succeeded with an empty lims id");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("ABC", null);
            fail("limsIdToUri succeeded with a null entity class");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("ABC", Artifact.class);
            fail("limsIdToUri succeeded with no server address set");
        }
        catch (IllegalStateException e)
        {
            // Correct.
        }

        api.setServer(new URL(URI_BASE));

        // Simple case

        URI expected = new URI(api.getServerApiAddress() + "artifacts/ABC");
        URI result = api.limsIdToUri("ABC", Artifact.class);

        assertEquals(expected, result, "Artifact URI wrong.");

        // Process step component

        expected = new URI(api.getServerApiAddress() + "steps/ABC/actions");
        result = api.limsIdToUri("ABC", Actions.class);

        assertEquals(expected, result, "Actions URI wrong.");

        // Configuration, but single case.

        expected = new URI(api.getServerApiAddress() + "configuration/workflows/123");
        result = api.limsIdToUri("123", Workflow.class);

        assertEquals(expected, result, "Actions URI wrong.");

        // Configuration, needing two ids.

        try
        {
            api.limsIdToUri("123", Stage.class);
            fail("limsIdToUri succeeded when the type requested needs two ids");
        }
        catch (IllegalArgumentException e)
        {
            // Correct.
        }
    }

    @Test
    public void testLimsIdToUri2() throws Exception
    {
        ClarityAPIImpl api = new ClarityAPIImpl();
        api.setHttpRequestFactory(mock(AuthenticatingClientHttpRequestFactory.class));

        try
        {
            api.limsIdToUri(null, null, null);
            fail("limsIdToUri succeeded with two null lims ids");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("", null, null);
            fail("limsIdToUri succeeded with an empty first lims id");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("ABC", null, null);
            fail("limsIdToUri succeeded with a null second lims id");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("ABC", "", null);
            fail("limsIdToUri succeeded with an empty second lims id");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("ABC", "XYZ", null);
            fail("limsIdToUri succeeded with a null entity class");
        }
        catch (IllegalArgumentException e)
        {
            // Ok.
        }

        try
        {
            api.limsIdToUri("123", "456", ProtocolStep.class);
            fail("limsIdToUri succeeded with no server address set");
        }
        catch (IllegalStateException e)
        {
            // Correct.
        }

        api.setServer(new URL(URI_BASE));

        // Valid.

        URI expected = new URI(api.getServerApiAddress() + "configuration/protocols/123/steps/456");
        URI result = api.limsIdToUri("123", "456", ProtocolStep.class);

        assertEquals(expected, result, "Protocol step URI wrong.");

        // An entity needing only one id.

        try
        {
            api.limsIdToUri("123", "456", Protocol.class);
            fail("limsIdToUri succeeded when the type requested needs one id");
        }
        catch (IllegalArgumentException e)
        {
            // Correct.
        }
    }

    @Test
    public void testIllegalSearchTerms() throws Exception
    {
        ClarityAPIImpl api = new ClarityAPIImpl();
        api.setHttpRequestFactory(mock(AuthenticatingClientHttpRequestFactory.class));

        Map<String, Object> searchTerms = new HashMap<>();

        try
        {
            api.find(searchTerms, Project.class);

            fail("find succeeded with no server address set");
        }
        catch (IllegalStateException e)
        {
            // Correct.
        }

        api.setServer(new URL(URI_BASE));

        try
        {
            searchTerms.put("name", null);

            api.find(searchTerms, ClarityProcess.class);

            fail("Did not get an IllegalSearchTermException exception for a value being null.");
        }
        catch (IllegalSearchTermException e)
        {
            // Correct.
        }

        searchTerms.clear();

        try
        {
            String[] ids = new String[0];

            searchTerms.put("inputartifactlimsid", ids);

            api.find(searchTerms, ClarityProcess.class);

            fail("Did not get an IllegalSearchTermException exception for an array with no items.");
        }
        catch (IllegalSearchTermException e)
        {
            // Correct.
        }

        try
        {
            Set<String> ids = Collections.emptySet();

            searchTerms.put("inputartifactlimsid", ids);

            api.find(searchTerms, ClarityProcess.class);

            fail("Did not get an IllegalSearchTermException exception for a collection with no items.");
        }
        catch (IllegalSearchTermException e)
        {
            // Correct.
        }


        searchTerms.clear();

        try
        {
            String[] ids = { "2-1234", "2-54345", null, "2-12312" };

            searchTerms.put("inputartifactlimsid", ids);

            api.find(searchTerms, ClarityProcess.class);

            fail("Did not get an IllegalSearchTermException exception for an array containing a null value.");
        }
        catch (IllegalSearchTermException e)
        {
            // Correct.
        }

        try
        {
            List<String> ids = Arrays.asList("2-1234", "2-54345", null, "2-12312");

            searchTerms.put("inputartifactlimsid", ids);

            api.find(searchTerms, ClarityProcess.class);

            fail("Did not get an IllegalSearchTermException exception for a collection containing a null value.");
        }
        catch (IllegalSearchTermException e)
        {
            // Correct.
        }
    }
}
