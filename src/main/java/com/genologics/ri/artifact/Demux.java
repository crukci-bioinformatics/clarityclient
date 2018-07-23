
package com.genologics.ri.artifact;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.Locatable;

/**
 * The detailed representation of the demultiplexing of a pooled artifact
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "demux")
@XmlType(name = "demux", propOrder = { "artifact", "demuxDetails" })
public class Demux implements Locatable, Serializable
{
    private static final long serialVersionUID = 8873892961025197978L;

    @XmlElement
    protected DemuxSourceArtifact artifact;

    @XmlElement(name = "demux")
    protected DemuxDetails demuxDetails;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Demux()
    {
    }

    public DemuxSourceArtifact getArtifact()
    {
        return artifact;
    }

    public void setArtifact(DemuxSourceArtifact artifact)
    {
        this.artifact = artifact;
    }

    public DemuxDetails getDemuxDetails()
    {
        return demuxDetails;
    }

    public void setDemuxDetails(DemuxDetails demuxDetails)
    {
        this.demuxDetails = demuxDetails;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }
}
