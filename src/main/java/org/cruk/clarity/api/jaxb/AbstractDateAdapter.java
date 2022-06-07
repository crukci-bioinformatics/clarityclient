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

package org.cruk.clarity.api.jaxb;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Base for date adapters. Uses a thread local to keep thread safe with the
 * thread unsafe DateFormat objects.
 */
public abstract class AbstractDateAdapter extends XmlAdapter<String, Date>
{
    /**
     * Holder for the date formatter.
     */
    private ThreadLocal<DateFormat> formatter = new ThreadLocal<DateFormat>();

    /**
     * Empty constructor.
     */
    protected AbstractDateAdapter()
    {
    }

    /**
     * Create the correct date format object for the type of dates expected.
     *
     * @return The DateFormat object.
     */
    protected abstract DateFormat createFormatter();

    /**
     * To keep things thread safe with the formatter, get or create the date
     * formatter in the local thread.
     *
     * @return The DateFormat.
     */
    private DateFormat get()
    {
        DateFormat df = formatter.get();
        if (df == null)
        {
            df = createFormatter();
            formatter.set(df);
        }
        return df;
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
        return isEmpty(v) ? null : get().parse(v);
    }

    /**
     * Convert the given date into the expected date format.
     *
     * @param v The date to print.
     *
     * @return The string version of the date, or null if {@code v} is null.
     */
    @Override
    public String marshal(Date v)
    {
        return v == null ? null : get().format(v);
    }

}
