package com.genologics.ri.stepconfiguration;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class ProtocolStepIdExtractionTest
{
    @Test
    public void testProtocolStepGetId() throws URISyntaxException
    {
        ProtocolStep step = new ProtocolStep();

        assertNull("Got id without URI.", step.getId());
        assertNull("Got protocol id without URI.", step.getProtocolId());

        step.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/workflows/7/stages/86"));

        assertNull("Got id with incorrect URI.", step.getId());
        assertNull("Got protocol id with incorrect URI.", step.getProtocolId());

        step.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/protocols/17/steps/49"));

        assertEquals("Step id not extracted properly", Integer.valueOf(49), step.getId());
        assertEquals("Protocol id not extracted properly", Integer.valueOf(17), step.getProtocolId());
    }

}
