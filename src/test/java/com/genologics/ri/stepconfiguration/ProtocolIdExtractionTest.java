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

package com.genologics.ri.stepconfiguration;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import com.genologics.ri.protocolconfiguration.Protocol;

public class ProtocolIdExtractionTest
{
    @Test
    public void testProtocolGetId() throws URISyntaxException
    {
        Protocol protocol = new Protocol();

        assertNull(protocol.getId(), "Got id without URI.");

        protocol.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/workflows/7"));

        assertNull(protocol.getId(), "Got id with incorrect URI.");

        protocol.setUri(new URI("http://limsdev.cri.camres.org:8080/api/v2/configuration/protocols/17"));

        assertEquals(Integer.valueOf(17), protocol.getId(), "Protocol id not extracted properly");
    }
}
