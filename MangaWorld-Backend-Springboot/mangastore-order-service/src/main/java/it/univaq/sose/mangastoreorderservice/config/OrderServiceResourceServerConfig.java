package it.univaq.sose.mangastoreorderservice.config;

import it.univaq.sose.mangastorecommons.security.GlobalResourceServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
public class OrderServiceResourceServerConfig extends GlobalResourceServerConfig {

    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("web").tokenServices(tokenServices);
    }
}
