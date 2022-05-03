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

package org.cruk.clarity.api.unittests;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;


public final class CRUKCICheck
{
    public static void assumeInCrukCI()
    {
        assumeTrue(CRUKCICheck.inCrukCI(), "Not in the CRUK-CI institute. This test will not work.");
    }

    public static boolean inCrukCI()
    {
        try
        {
            String hostname = InetAddress.getLocalHost().getHostName();
            return hostname.endsWith(".cri.camres.org");
        }
        catch (UnknownHostException e)
        {
            // Should never happen.
            return false;
        }
    }
}
