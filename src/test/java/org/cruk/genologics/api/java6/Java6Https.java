package org.cruk.genologics.api.java6;

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

    public static synchronized final void checkHttps()
    {
        if (!checked)
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
                        Provider p = (Provider)bc.newInstance();

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
                finally
                {
                    checked = true;
                }
            }
        }
    }

    public Java6Https()
    {
        checkHttps();
    }
}
