
package com.genologics.ri.artifact;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.step.ProcessStep;

/**
 * The details of the pooling step that created the pooled sample(s).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pool-step")
public class PoolStep implements LimsLink<ProcessStep>, Serializable
{
    private static final long serialVersionUID = 2462830072429085619L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    public PoolStep()
    {
    }

    public PoolStep(URI uri)
    {
        this.uri = uri;
    }

    public PoolStep(URI uri, String name)
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Class<ProcessStep> getEntityClass()
    {
        return ProcessStep.class;
    }

    @Override
    public String toString()
    {
        return name == null ? "null" : name;
    }
}
