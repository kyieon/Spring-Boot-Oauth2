package com.yjlee.oauth2.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.yjlee.oauth2.service.DefaultUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {

	private final String secretKey = "privateKey";
	
	private final String clientId = "clientId";
	private final String clientSecret = "clientSecret";
	
	private final int accessTokenValidSecond = 30 * 60; 	// 30 Min
	private final int refreshTokenValidSecond = 60 * 60;    //	1 Hour 
	
	@Autowired
    private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DefaultUserDetailsService defaultUserDetailsService;
	
	@Bean
	public TokenStore tokenStore() throws Exception {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	private JwtAccessTokenConverter accessTokenConverter() throws Exception {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		//JwtAlgorithms
		tokenConverter.setSigner(new MacSigner(secretKey));
		tokenConverter.setVerifier(new MacSigner(secretKey));
		return tokenConverter;
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
				.tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter())
				.authenticationManager(authenticationManager)
				.userDetailsService(defaultUserDetailsService)
		;
	}
	
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                ;
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	clients.inMemory()
    			.withClient(clientId)
    			.secret(passwordEncoder.encode(clientSecret))
    			.scopes("api.get", "api.post")
    			.authorizedGrantTypes("password", "refresh_token", "client_credentials")
    			.accessTokenValiditySeconds(accessTokenValidSecond)
    			.refreshTokenValiditySeconds(refreshTokenValidSecond)
    			;
    }
}