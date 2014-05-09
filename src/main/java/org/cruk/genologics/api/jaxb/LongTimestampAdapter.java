/*
 * CRUK-CI Genologics REST API Java Client.
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

package org.cruk.genologics.api.jaxb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.util.StringUtils;

/**
 * Adapter for those dates that are exchanged with the API in the long timestamp format.
 */
public class LongTimestampAdapter extends XmlAdapter<String, Date>
{
    /**
     * Formatter used for parsing and printing.
     */
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * Empty constructor.
     */
    public LongTimestampAdapter()
    {
    }

    /**
     * Parse the string into a Date object.
     *
     * @param v The string to parse.
     *
     * @return A {@code Date} object for the string, or null if {@code v} is null.
     *
     * @throws ParseException if the string cannot be parsed.
     */
    @Override
    public Date unmarshal(String v) throws ParseException
    {
        return StringUtils.isEmpty(v) ? null : formatter.parse(v);
    }

    /**
     * Convert the given date into the long timestamp date format.
     *
     * @param v The date to print.
     *
     * @return The string version of the date, or null if {@code v} is null.
     */
    @Override
    public String marshal(Date v)
    {
        return v == null ? null : formatter.format(v);
    }

}
