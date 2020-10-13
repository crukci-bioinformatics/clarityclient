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

package org.cruk.genologics.api.java6;

import java.lang.reflect.Constructor;
import java.security.Provider;
import java.security.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility and bean to add the BouncyCastle security provider if
 * this code is running under Java 6 or earlier.
 *
 * <p>
 * It is possible to create beans of this class so initialisation
 * is done by Spring.
 * </p>
 */
public final class Java6Https
{
    private static boolean checked;

    /**
     * Make sure that the BouncyCastle provider is set up if necessary.
     */
    public static synchronized final void checkHttps()
    {
        if (!checked)
        {
            try
            {
                int spec = Math.round(Float.parseFloat(System.getProperty("java.vm.specification.version")) * 10f);

                if (spec <= 16)
                {
                    Logger logger = LoggerFactory.getLogger(Java6Https.class);

                    try
                    {
                        final String bcClass = "org.bouncycastle.jce.provider.BouncyCastleProvider";

                        boolean haveBC = false;
                        Provider[] registeredProviders = Security.getProviders();
                        for (Provider p : registeredProviders)
                        {
                            if (p.getClass().getName().equals(bcClass))
                            {
                                haveBC = true;
                                break;
                            }
                        }

                        if (!haveBC)
                        {
                            Class<?> bc = Class.forName(bcClass);
                            Constructor<?> bcons = bc.getConstructor();
                            Provider p = (Provider)bcons.newInstance();

                            Security.addProvider(p);
                        }

                        logger.debug("Added BouncyCastle provider to security providers list.");
                    }
                    catch (ClassNotFoundException e)
                    {
                        logger.error("Cannot locate BouncyCastle provider.");
                    }
                    catch (IllegalAccessException e)
                    {
                        logger.error("Cannot instantiate a BouncyCastle provider.");
                    }
                    catch (InstantiationException e)
                    {
                        logger.error("Cannot instantiate a BouncyCastle provider.");
                    }
                    catch (SecurityException e)
                    {
                        logger.error("Not allowed to add the BouncyCastle provider: {}", e.getMessage());
                    }
                    catch (Exception e)
                    {
                        logger.error("Cannot establish the BouncyCastle provider:", e);
                    }
                }
            }
            finally
            {
                checked = true;
            }
        }
    }

    /**
     * Constructor that simply calls {@code checkHttps()} to set up the
     * BouncyCastle provider if necessary and possible. Calling here allows
     * this class to be created as a bean in Spring.
     */
    public Java6Https()
    {
        checkHttps();
    }
}
