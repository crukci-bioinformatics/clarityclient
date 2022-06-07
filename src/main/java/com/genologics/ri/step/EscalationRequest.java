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

package com.genologics.ri.step;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.cruk.clarity.api.jaxb.LongTimestampAdapter;

import com.genologics.ri.Linkable;
import com.genologics.ri.researcher.Researcher;

/**
 * @since 2.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "escalation-request", propOrder = { "author", "reviewer", "date", "comment" })
public class EscalationRequest implements Serializable
{
    private static final long serialVersionUID = 4381438520659390791L;

    @XmlElement
    protected UserLink author;

    @XmlElement
    protected UserLink reviewer;

    @XmlElement
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(LongTimestampAdapter.class)
    protected Date date;

    @XmlElement
    protected String comment;

    public UserLink getAuthor()
    {
        return author;
    }

    public void setAuthor(Linkable<Researcher> link)
    {
        this.author = new UserLink(link);
    }

    public UserLink getReviewer()
    {
        return reviewer;
    }

    public void setReviewer(Linkable<Researcher> link)
    {
        this.reviewer = new UserLink(link);
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

}
