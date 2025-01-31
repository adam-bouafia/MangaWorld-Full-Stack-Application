package it.univaq.sose.mangastorecatalogservice.config;

import it.univaq.sose.mangastorecommons.security.GlobalResourceServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * This class is responsible for configuring the resource server in the MangaStore catalog service.
 * It extends the GlobalResourceServerConfig class and overrides its methods to customize the configuration.
 */
@Configuration
public class ResourceServerConfig extends GlobalResourceServerConfig {
    
    @Autowired
    private ResourceServerTokenServices tokenServices;
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("web").tokenServices(tokenServices);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/actuator/**", "/api-docs/**", "/h2-console/**", "/signin").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/token").permitAll()
                .antMatchers(HttpMethod.GET, "/product**/**").permitAll()
                .antMatchers(HttpMethod.GET, "/review/**").permitAll()
                .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                .antMatchers("/**").authenticated();
    }
}
