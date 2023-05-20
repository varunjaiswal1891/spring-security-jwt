package io.javabrains.varun.springsecurityjwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration {

      //in memory authentication 
    @Bean
    public InMemoryUserDetailsManager userDetailsService()
    {
        UserDetails user1  = User.withUsername("blah")
                                .password("blah")
                                .roles("ADMIN")
                                .build();

        UserDetails user2  = User.withUsername("foo")
                                .password("foo")
                                .roles("USER")
                                .build();                       
        return new InMemoryUserDetailsManager(user1,user2);
    }

     @Bean
     public PasswordEncoder getPasswordEncoder()
     {
         return NoOpPasswordEncoder.getInstance();
     }

      //working of authorization using HttpSecurity
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/admin").hasRole("ADMIN")
        .antMatchers("/user").hasAnyRole("USER","ADMIN")
        .antMatchers("/").permitAll()
        .and().formLogin();

        return http.build();
     }
}
