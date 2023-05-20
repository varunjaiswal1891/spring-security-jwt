package io.javabrains.varun.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.varun.springsecurityjwt.models.AuthenticationRequest;
import io.javabrains.varun.springsecurityjwt.models.AuthenticationResponse;
import io.javabrains.varun.springsecurityjwt.services.MyUserDetailsService;
import io.javabrains.varun.springsecurityjwt.util.JwtUtil;

@RestController
public class HelloResource {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @GetMapping("/")
    public String home()
    {
        return ("Welcome");
    }

    @GetMapping("/admin")
    public String admin()
    {
        return ("Welcome admin");
    }

    @GetMapping("/user")
    public String user()
    {
        return ("Welcome user");
    }

    @RequestMapping(value="/authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception
    {
        try{

           authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        }catch(BadCredentialsException e) {
            throw new Exception("Incorrect username and password ",e);
        } 

        final UserDetails userDetails =  userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    
}
