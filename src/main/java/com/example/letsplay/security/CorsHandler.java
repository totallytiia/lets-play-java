package com.example.letsplay.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Component
public class CorsHandler implements CorsConfigurationSource {

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		// Create a new CorsConfiguration object to define CORS (Cross-Origin Resource Sharing) settings
		CorsConfiguration returnValue = new CorsConfiguration();

		// Allow requests from all origins. The '*' pattern means any domain can send requests.
		returnValue.setAllowedOriginPatterns(List.of("*"));

		// Allow credentials (like cookies or HTTP authentication) to be included in the requests.
		returnValue.setAllowCredentials(true);

		// Define which HTTP headers can be used in the actual request. This is important for security and to control
		// which headers are allowed in cross-origin requests.
		returnValue.setAllowedHeaders(Arrays.asList(
				"Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
				"Access-Control-Request-Method", "Access-Control-Request-Headers",
				"Origin", "Cache-Control", "Content-Type", "Authorization"
		));

		// Specify which HTTP methods are allowed for cross-origin requests.
		returnValue.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));

		// Return the configured CorsConfiguration object
		return returnValue;
	}


}
