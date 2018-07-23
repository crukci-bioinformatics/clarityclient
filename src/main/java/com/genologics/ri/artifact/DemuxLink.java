
package com.genologics.ri.artifact;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 * Identifies the resource that will demultiplex this artifact. This will only
 * be present if the artifact is a pool.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux-link")
public class DemuxLink implements LimsLink<DemuxArtifact>, Serializable
{
    private static final long serialVersionUID = -3140263256856604670L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public DemuxLink()
    {
    }

    public DemuxLink(URI uri)
    {
        this.uri = uri;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public void setArtifact(Linkable<DemuxArtifact> artifact)
    {
        this.uri = artifact == null ? null : artifact.getUri();
    }

    @Override
    public Class<DemuxArtifact> getEntityClass()
    {
        return DemuxArtifact.class;
    }
}
