## Spring Configuration

The main Spring configuration file for the client is:

```
/org/cruk/clarity/api/clarity-client-context.xml
```

Add this file to your Spring application context path. If you wish to use
the caching feature too, you'll also need to add:

```
/org/cruk/clarity/api/clarity-cache-context.xml
```

See the [caching page](caching.html) for more information on using
the cache.

`clarity-client-context.xml` and `clarity-cache-context.xml`
are provided in the client's JAR file.

One **must** define the bean `clarityAPI` in the client program's XML
configuration files or a Spring `@Configuration` annotated class from
release 2.24.14 of the client. It has been found that overriding the default
definition in `clarity-client-context.xml` causes problems with Spring
Boot when the bean is overridden to initialise with properties and so forth.

In XML configuration, the line to add is (with optional initialisation
properties in _apiCredentials_):

```XML
<bean id="clarityAPI" parent="clarityAPIBase">
    <constructor-arg ref="apiCredentials"/>
</bean>
```

In Spring annotation configuration, autowiring is correct for the normal
bean initialisation. As such, one needs to add the bean as:

```Java
@Configuration
@ImportResource({
    "classpath:/org/cruk/clarity/api/clarity-client-context.xml",
    "classpath:/org/cruk/clarity/api/clarity-cache-context.xml"
})
public class MyClarityProgramConfiguration
{
    @Bean
    public ClarityAPI clarityAPI()
    {
        return new ClarityAPIImpl();
    }
}
```

Initialisation of the bean using properties or the setter methods can be
done in this method too before the object is returned, or done with a
`@PostConstruct` annotated method.
