package com.yjlee.oauth2;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MainApplication.class)
public class MainApplicationTest {

	private MockMvc mockMvc;

	@Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(springSecurityFilterChain).build();
    }

	private final String CLIENT_ID = "clientId";
	private final String CLIENT_SECRET = "clientSecret";
    private String SECURITY_USERNAME = "admin";
    private String SECURITY_PASSWORD = "admin";
    
	@Test
	public void testOAuthTokenPassword() throws Exception {
		
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", SECURITY_USERNAME);
        params.add("password", SECURITY_PASSWORD);
//        (option)
//        params.add("scope", "api.get");
        
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                ;

        String resultString = result.andReturn().getResponse().getContentAsString();
        System.out.println(resultString);
        
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> parseMap = jsonParser.parseMap(resultString);
        
        String accessToken = parseMap.get("access_token").toString();
        System.out.println(accessToken);
        
        String refreshToken = parseMap.get("refresh_token").toString();
        System.out.println(refreshToken);
	}

	@Test
	public void testOAuthTokenClientCredentials() throws Exception {
		
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
//        (option)
//        params.add("scope", "api.get");
        
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                ;

        String resultString = result.andReturn().getResponse().getContentAsString();
        System.out.println(resultString);
        
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> parseMap = jsonParser.parseMap(resultString);
        
        String accessToken = parseMap.get("access_token").toString();
        System.out.println(accessToken);
	}

	@Test
	public void testOAuthCheckToken() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcGkuZ2V0IiwiYXBpLnBvc3QiXSwiZXhwIjoxNTY1MTY4NTI3LCJqdGkiOiJjYzU4YzI1Yi01NjgyLTRkNjctOTg1NC0xMmFmZmY5ZmMyNmQiLCJjbGllbnRfaWQiOiJjbGllbnRJZCJ9.HrjzNKR9EGb1M8pK2XYwJmZUmgie3o9onfX4uBsJy1c";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("token", accessToken);
		
		ResultActions result 
				= mockMvc.perform(post("/oauth/check_token")
				.params(params)
				.with(httpBasic(CLIENT_ID, CLIENT_SECRET))
				.accept("application/json;charset=UTF-8"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		;
		
		String resultString = result.andReturn().getResponse().getContentAsString();
		System.out.println(resultString);
	}
	
	@Test
	public void testOAuthTokenKey() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Njg2Nzc3MzUsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJiZTA3NDVjYy0yN2RmLTQ0NjEtODA5NS1jNzY1ZWY4Yzg1MjEiLCJjbGllbnRfaWQiOiJjbGllbnRJZCIsInNjb3BlIjpbImFwaS5nZXQiXX0.oewZdLPukMhxpaG3YOcjGrCrcwzQpl2H57tlOpUWirM";
		String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFwaS5nZXQiXSwiYXRpIjoiYmUwNzQ1Y2MtMjdkZi00NDYxLTgwOTUtYzc2NWVmOGM4NTIxIiwiZXhwIjoxNjUxNDc3NzM1LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjYyMmExOTM3LThlZTMtNDJlMi04OGY3LTYwNTFmMzBjMDU4MSIsImNsaWVudF9pZCI6ImNsaWVudElkIn0.UP7wKiPnUwgAjW4xhjLg_eSvKEcCvDZyGtY1rGAwfcA";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


		ResultActions result 
				= mockMvc.perform(post("/oauth/token_key")
				.params(params)
				.with(httpBasic(CLIENT_ID, CLIENT_SECRET))
				.accept("application/json;charset=UTF-8"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"))
		;

		String resultString = result.andReturn().getResponse().getContentAsString();
		System.out.println(resultString);

	}
}
