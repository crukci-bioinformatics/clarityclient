
package com.genologics.ri.artifact;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.sample.Sample;

/**
 * The details of a sample that is part of a pooled artifact.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux-artifact-sample")
public class DemuxArtifactSample implements LimsEntityLink<Sample>, Serializable
{
    private static final long serialVersionUID = 7139520312009522858L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    public DemuxArtifactSample()
    {
    }

    public DemuxArtifactSample(URI uri)
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

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    @Override
    public Class<Sample> getEntityClass()
    {
        return Sample.class;
    }

    @Override
    public String toString()
    {
        return limsid == null ? "null" : limsid;
    }

}
