package com.yjlee.api.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class TestApiController {
	
	private static final Logger log = LoggerFactory.getLogger(TestApiController.class);

	@PreAuthorize("#oauth2.hasScope('api.get')")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String get(@RequestParam Map<String, ?> map) {
		log.info("v1/api/Get");
		return "Get";
	}
	
	@PreAuthorize("#oauth2.hasScope('api.post')")
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String post(@RequestBody Map<String, ?> map) {
		log.info("v1/api/post");
		return "Post";
	}
	
	@PreAuthorize("#oauth2.hasScope('api.none')")
	@RequestMapping(value = "/none", method = { RequestMethod.GET, RequestMethod.POST} )
	public String none() {
		log.info("v1/api/none");
		return "None";
	}
}
