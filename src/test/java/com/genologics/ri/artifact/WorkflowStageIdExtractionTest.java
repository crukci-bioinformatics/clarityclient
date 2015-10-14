package com.genologics.ri.artifact;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class WorkflowStageIdExtractionTest
{
    @Test
    public void testStageGetId() throws URISyntaxException
    {
        WorkflowStage stage = new WorkflowStage();

        assertNull("Got id without URI.", stage.getId());
        assertNull("Got workflow id without URI.", stage.getWorkflowId());

        stage.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/protocols/17/steps/49"));

        assertNull("Got id with incorrect URI.", stage.getId());
        assertNull("Got workflow id with incorrect URI.", stage.getWorkflowId());

        stage.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/workflows/7/stages/86"));

        assertEquals("Stage id not extracted properly", Integer.valueOf(86), stage.getId());
        assertEquals("Workflow id not extracted properly", Integer.valueOf(7), stage.getWorkflowId());
    }
}
