package com.genologics.ri.stepconfiguration;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.genologics.ri.protocolconfiguration.Protocol;

public class ProtocolIdExtractionTest
{

    @Test
    public void testProtocolGetId() throws URISyntaxException
    {
        Protocol protocol = new Protocol();

        assertNull("Got id without URI.", protocol.getId());

        protocol.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/workflows/7"));

        assertNull("Got id with incorrect URI.", protocol.getId());

        protocol.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/protocols/17"));

        assertEquals("Protocol id not extracted properly", Integer.valueOf(17), protocol.getId());
    }
}
