package com.yjlee.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private final String secretKey = "privateKey";

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	private JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		//JwtAlgorithms
		tokenConverter.setSigner(new MacSigner(secretKey));
		tokenConverter.setVerifier(new MacSigner(secretKey));
		return tokenConverter;
	}
	
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests()
//        	.antMatchers("/v1/**").access("#oauth2.hasScope('api.post')")
        	.anyRequest()
        	.authenticated();
    }
}