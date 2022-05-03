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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.impl.ClarityAPIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/org/cruk/clarity/api/clarity-client-context.xml")
public class ClarityClientTestConfiguration
{
    public ClarityClientTestConfiguration()
    {
    }

    @Bean
    public ClarityAPI clarityAPI()
    {
        ClarityAPIImpl api = new ClarityAPIImpl();

        try (InputStream propsIn = getClass().getResourceAsStream("/testcredentials.properties"))
        {
            if (propsIn != null)
            {
                Properties credentials = new Properties();
                credentials.load(propsIn);
                api.setConfiguration(credentials);
            }
        }
        catch (IOException e)
        {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Could not read from credentials file: ", e);
        }

        return api;
    }
}
