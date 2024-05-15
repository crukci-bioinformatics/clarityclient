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

package com.genologics.ri.protocolconfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.Batch;
import com.genologics.ri.ClarityQueryResult;

/**
 *
 * Returns the representation of a list of protocols
 * <p>
 * A list of protocols available in the system, providing names and URIs to
 * access more detailed representations of the protocols
 * </p>
 */
@ClarityQueryResult(entityClass = Protocol.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "protocols")
@XmlRootElement(name = "protocols")
public class Protocols implements Batch<ProtocolLink>, Serializable
{
    private static final long serialVersionUID = -586199251176099998L;

    @XmlElement(name = "protocol")
    protected List<ProtocolLink> protocols;

    public List<ProtocolLink> getProtocols()
    {
        if (protocols == null)
        {
            protocols = new ArrayList<ProtocolLink>();
        }
        return protocols;
    }

    @Override
    public Iterator<ProtocolLink> iterator()
    {
        return getProtocols().iterator();
    }

    @Override
    public List<ProtocolLink> getList()
    {
        return getProtocols();
    }

    @Override
    public int getSize()
    {
        return protocols == null ? 0 : protocols.size();
    }

}
