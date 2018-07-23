
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


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux-source-artifact")
public class DemuxSourceArtifact implements LimsLink<Artifact>, Serializable
{
    private static final long serialVersionUID = -3058956251238626047L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    public DemuxSourceArtifact()
    {
    }

    public DemuxSourceArtifact(URI uri)
    {
        this.uri = uri;
    }

    public DemuxSourceArtifact(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public void setArtifact(Linkable<Artifact> artifact)
    {
        this.uri = artifact == null ? null : artifact.getUri();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

    @Override
    public String toString()
    {
        return name == null ? "null" : name;
    }
}
