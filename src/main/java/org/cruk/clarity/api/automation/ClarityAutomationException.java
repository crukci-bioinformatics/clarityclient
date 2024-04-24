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

package org.cruk.clarity.api.automation;


/**
 * Runtime exception when there is a failure in automating a Clarity process.
 *
 * @since 2.31.2
 */
public class ClarityAutomationException extends RuntimeException
{
    private static final long serialVersionUID = 1609079834226439767L;

    /**
     * {@inheritDoc}
     */
    public ClarityAutomationException()
    {
    }

    /**
     * {@inheritDoc}
     */
    public ClarityAutomationException(String message)
    {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public ClarityAutomationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    public ClarityAutomationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
