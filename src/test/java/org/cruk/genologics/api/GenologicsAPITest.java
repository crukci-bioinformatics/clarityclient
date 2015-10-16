package org.cruk.genologics.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URL;

import org.cruk.genologics.api.impl.GenologicsAPIImpl;
import org.junit.Test;

import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.protocolconfiguration.Protocol;
import com.genologics.ri.stage.Stage;
import com.genologics.ri.step.Actions;
import com.genologics.ri.stepconfiguration.ProtocolStep;
import com.genologics.ri.workflowconfiguration.Workflow;

public class GenologicsAPITest
{
    static final String URI_BASE = "http://limsdev.cruk.cam.ac.uk:8080";

    @Test
    public void testLimsIdToUri1() throws Exception
    {
        GenologicsAPI api = new GenologicsAPIImpl();

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

        assertEquals("Artifact URI wrong.", expected, result);

        // Process step component

        expected = new URI(api.getServerApiAddress() + "steps/ABC/actions");
        result = api.limsIdToUri("ABC", Actions.class);

        assertEquals("Actions URI wrong.", expected, result);

        // Configuration, but single case.

        expected = new URI(api.getServerApiAddress() + "configuration/workflows/123");
        result = api.limsIdToUri("123", Workflow.class);

        assertEquals("Actions URI wrong.", expected, result);

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
        GenologicsAPI api = new GenologicsAPIImpl();

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

        assertEquals("Protocol step URI wrong.", expected, result);

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
}
