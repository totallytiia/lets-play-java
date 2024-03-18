package com.example.letsplay.security;


import com.example.letsplay.exceptions.InvalidUserException;
import com.example.letsplay.services.UserService;
import com.example.letsplay.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil; // Utility class to work with JWT

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

		String email = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				email = jwtUtil.extractUsername(jwt);
			} catch (Exception ex) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
				return; // Stop further processing
			}
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				UserDetails userDetails = this.userService.getUserByEmail(email);
				if (jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				} else {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
					return; // Stop further processing
				}
			} catch (InvalidUserException ex) {
				// Set an appropriate HTTP response
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user credentials");
				return; // Stop further processing
			}
		}

		filterChain.doFilter(request, response);
	}
}



	/*
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Extract the Authorization header from the request
		final String authorizationHeader = request.getHeader("Authorization");

		String email = null;
		String jwt = null;

		// Check if the Authorization header is present and starts with "Bearer "
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// Extract the JWT token from the header
			jwt = authorizationHeader.substring(7);
			try {
				// Extract the username (email) from the token
				email = jwtUtil.extractUsername(jwt);
			} catch (Exception ex) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
				return; // Stop further processing
			}

		}

		// Authenticate the user if the JWT is valid and the user is not already authenticated
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userService.getUserByEmail(email); // Fetch user details
			if (jwtUtil.validateToken(jwt, userDetails)) { // Validate the token
				// Create an authentication token
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// Set the authentication in the security context
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
				return; // Stop further processing
			}
		}
		// Continue with the filter chain
		filterChain.doFilter(request, response);
	}

}

	 */
