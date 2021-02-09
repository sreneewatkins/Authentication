package com.tts.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/*
The WebSecurityConfig class is annotated with @EnableWebSecurity to enable Spring
Security’s web security support and provide the Spring MVC integration. It also extends
WebSecurityConfigurerAdapter and overrides a couple of its methods to set some specifics
of the web security configuration.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
    We will be configuring the HttpSecurity, which will allow to give access to/restrict
    certain routs.
    To start, we permit all users to access the '/' and '/home' routes.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
        /*
        Next up, we use formLogin to determine what page will be used for logging users in.
        .and() is used to string all of our configurations together.
        We also used permitAll() to make sure all users can access this page as well.
        (Everyone needs to access this page to log in!)
         */
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
        /*
        Finally, we configure logout, which also allows all users to access this point.
         */
                .and()
                .logout()
                .permitAll();
    }

    /*
    we'll also configure a UserService that builds a default user for us to log in with.
    You will probably get an error that withDefaultPasswordEncoder() is deprecated or
    unsecure. That's ok! For testing purposes, it works fine, but for a real application
    you would want to store the user's password in a more encrypted format.
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    /*
    The configure(HttpSecurity) method defines which URL paths should be secured and
    which should not.
    When a user successfully logs in, they are redirected to the previously requested
    page that required authentication. There is a custom /login page (which is specified
    by loginPage()), and everyone is allowed to view it.
    We can see that anyone can visit these pages with the .permitAll().
    The userDetailsService() method sets up an in-memory user store with a single user.
    That user is given a user name of "user", a password of "password", and a role of USER.
     */


}//end WebSecurityConfig class
