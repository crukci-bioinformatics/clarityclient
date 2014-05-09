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

package org.cruk.genologics.api.unittests;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cruk.genologics.api.GenologicsAPI;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class UnitTestApplicationContextFactory
{
    private static ConfigurableApplicationContext context;

    public static ConfigurableApplicationContext getApplicationContext()
    {
        if (context == null)
        {
            context = new ClassPathXmlApplicationContext("/org/cruk/genologics/api/genologics-client-context.xml", "unittest-client-context.xml");

            GenologicsAPI api = context.getBean("genologicsAPI", GenologicsAPI.class);
            setCredentialsOnApi(api);
        }
        return context;
    }

    public static boolean setCredentialsOnApi(GenologicsAPI api)
    {
        InputStream propsIn = UnitTestApplicationContextFactory.class.getResourceAsStream("/testcredentials.properties");
        if (propsIn != null)
        {
            try
            {
                Properties credentials = new Properties();
                credentials.load(propsIn);
                api.setConfiguration(credentials);
                return true;
            }
            catch (IOException e)
            {
                Log logger = LogFactory.getLog(UnitTestApplicationContextFactory.class);
                logger.fatal("Could not read from credentials file: ", e);
            }
            finally
            {
                IOUtils.closeQuietly(propsIn);
            }
        }
        return false;
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
