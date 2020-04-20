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

package com.genologics.ri.userdefined;

public class MissingUDFException extends RuntimeException
{
    private static final long serialVersionUID = -4554179841298001722L;

    private String name;

    public MissingUDFException(String name)
    {
        this.name = name;
    }

    public MissingUDFException(String name, String message)
    {
        super(message);
        this.name = name;
    }

    public String getFieldName()
    {
        return name;
    }

}
