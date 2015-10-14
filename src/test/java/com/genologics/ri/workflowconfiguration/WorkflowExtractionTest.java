package com.genologics.ri.workflowconfiguration;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class WorkflowExtractionTest
{

    @Test
    public void testWorkflowGetId() throws URISyntaxException
    {
        Workflow workflow = new Workflow();

        assertNull("Got id without URI.", workflow.getId());

        workflow.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/protocols/17"));

        assertNull("Got id with incorrect URI.", workflow.getId());

        workflow.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/workflows/7"));

        assertEquals("Workflow id not extracted properly", Integer.valueOf(7), workflow.getId());
    }
}
