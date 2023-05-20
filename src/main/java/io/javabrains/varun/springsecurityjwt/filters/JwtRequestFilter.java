package io.javabrains.varun.springsecurityjwt.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.javabrains.varun.springsecurityjwt.services.MyUserDetailsService;
import io.javabrains.varun.springsecurityjwt.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            final String authHeader =  request.getHeader("Authorization");
            System.out.println("authHeader == "+authHeader);
            String username = null;
            String jwt = null;
            //JwtUtil jwtUtil = new JwtUtil();

            if( authHeader!=null && authHeader.startsWith("Bearer "))
            {
                jwt = authHeader.substring(7);
                System.out.println("token == "+jwt);
               
                username = jwtUtil.getUsernameFromToken(jwt);
                System.out.println("username == "+username);
            }
            
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                 
               UserDetails userDetails =  userDetailsService.loadUserByUsername(username);
               if(jwtUtil.validateToken(jwt, userDetails))
               {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); 
               }
            }
            filterChain.doFilter(request,response);
    }//method ends here
    
}
