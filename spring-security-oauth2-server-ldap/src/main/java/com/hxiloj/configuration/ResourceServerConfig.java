package com.hxiloj.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.requestMatchers()
                .antMatchers("/login", "/oauth/authorize")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    	/*User in memory */
//        auth.parentAuthenticationManager(authenticationManager)
//                .inMemoryAuthentication()
//                .withUser("henry")
//                .password("solo123")
//                .roles("USER");
    	
    	/*User in LDAP*/
    	
    	auth.parentAuthenticationManager(authenticationManager)
    	.ldapAuthentication()
    	.userSearchFilter("YOUR_SEARCH_FILER")
        .userSearchBase("YOUR_SEARCH_BASE")
        .contextSource(contextSource())
        .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator());

    }
    
    @Bean
    public static LdapAuthoritiesPopulator ldapAuthoritiesPopulator() throws Exception {
        DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(contextSource(), "YOUR_SEARCH_BASE");
        populator.setIgnorePartialResultException(true);
        return populator;
    }
    
    @Bean
    public static BaseLdapPathContextSource contextSource() throws Exception {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldap://your_ip:389/");

        contextSource.setUserDn("/*your_principleCN*/"); 
        contextSource.setPassword("your_password"); 
        contextSource.setReferral("ignore"); 
        contextSource.afterPropertiesSet();

        return contextSource;
    }
}
