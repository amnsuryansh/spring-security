package com.project.springsecsection1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String sayWelcome() {
        // before adding security starter dependency
//        return "Welcome to Spring Application without Security";
        // after adding security starter dependency - we will get login screen
        // username: user
        // password: auto generated in console logs
        // even after multiple refreshes, password will saved by browser session remaining spring security takes care of browser session
        // check package org.springframework.boot.autoconfigure.security SecurityProperties;
        // there the user class has default username as user andm paswrod us uuid autognerated
        // if i want to customize username and password, then we can do that in application.properties file -
        // securityproperties class is a config class which has @ConfigurationProperties("spring.security")  - inside it we have user and password
        // so we use spring.security.user.name and spring.security.user.password in application.properties file
        // IF WE GIVE THEE USER NAME AND PASSWORD IN APPLICATION.PROPERTIES FILE, THEN IT WILL OVERRIDE THE DEFAULT ONE - I MEAN NO PASSWORD WILL BE GENERATED INTRNALLY BY SPRING BOOT
        // there is a logic prsent in spring boot securityproperties class - if user name and password is present in application.properties file,
        // then it will not generate the password internally - using the setPassword method of user class and the isPasswordGenerated method
        return "Welcome to Spring Application with Security";
    }


    /*
     * What is security and why do we need it?
     * Security is the practice of protecting systems, networks, data and business logic insid your web apps
     * from unauthorized access, attacks
     * Security is a non functional requirement - security, performance, scalability, maintainability are non functional requirements
     * Functional is business logic - what the application does
     *
     * Security should be considerd right from the starting the development of the project
     * Before a decade security was done after testing in sdlc but now it is moved to left hand side of sdlc and it is done while dev start pahse
     * we have devsecops now alogn with devops - dev + security + ops
     *
     * Types of security - using firewalls, using authentication and authorization, using encryption, using hashing, using ssl/tls
     *
     * security doesnt mean lossing data or money - it means loosing brand, trust from users build in long period of time
     *
     * Common security threats in our applications
     * sql injection,
     * session fixation,
     * xss(cross site scripting),
     * csrf(cross site request forgery),
     * ddos(distributed denial of service)
     *
     *
     * how spring security works?
     *
     * What are servlets and filters?
     * Servlet is a java class which handles http requests and responses - All servlets are present in servlet container or web server
     * Servlet container (web server) takes care if translating the http messages coming from browsers (clint sending requests) so that java code can understand
     * Apache Tomcat is one of th most used servlet container
     * Servlet container converts http messages servletRequest and handover to servlet method as parameter - ServletRequest is a java object - so we can simply tell http  msg is converted to java object for out web app
     *
     * The response is also sent as ServletResponse object - servlet container converts the servletResponse object to http response and sends it back to browser
     * Before we use to make servlet classes in java - but now spring boot takes care of it - so dev's focus on business logic
     *
     * Filters
     * Filters intercept each http requests/responses and do some pre work before out business logic. using the same filters Spring security enforce security based on our configuration inside web app
     *
     * Webserver -> filters (executed first) + servlets (executed after filters - has business logic)
     *
     * Spring Security Internal Flow
     *
     * Once user enters creds and hits any protected API - it is intercepted by spring security filters - based on our configuration i will be intercepted by 5-20 filters
     * Spring security filters will check creds and convert creds to Authentication object (has username, password, boolean value isAuthenticated or not - initially it will be false)
     * Authentication is a common contract which will be used by whole security framework to validate user
     *
     * Once authentication object is populated request is forwarded to AuthenticationManager - which takes authentication object as input and takes responsibility of authenticating the object - it will not do the authentication
     * It will forward the request to Authentication Provider - which does the actual authenticator
     *
     * Authentication Provider take help from UserDetailsManager / UserDetailsService and PasswordEncoder
     *
     * UserDetailsService - it has a method loadUserByUsername(String username) - which returns UserDetails object - which will be returned back to Authentication Provider
     * For Passwords Authentication Provider will take help from PasswordEncoder - which has methods to encode the password and match the passwords
     * Once passwords are matching - Authentication Provider will set the isAuthenticated to true and return the same authentication object back to AuthenticationManager
     *
     * AuthenticationManager will send the same authentication object to spring security filters - which will store the authentication object inside SecurityContext regardless authentication was successfull or not - which is a thread local class
     *
     * Authentication Object and sessionId for a browser is stored in Security Context - so that for subsequent requests we dont have to do authentication again and again
     * once next api hit comes - based on the session id the spring security filters will fetch the authentication object from security context and check the isAuthenticated value - if it is true - then user is allowed to access the protected api
     * All the authentication will happen only for the first time - for subsequent requests authentication object is fetched from security context
     *
     * At last we will send the response back to client - based on the user is authenticated or not
     *
     * Spring Security is similar to SDLC
     *
     * Authentication Provider - there can be multiple authentication providers - DaoAuthenticationProvider, LdapAuthenticationProvider etc - AI
     *
     *
     *
     *
     *
     * AuthorizationFilter - it checks whether the request is for protected api or not - if it is for protected api - then it will check whether user is authenticated or not - if not authenticated - then it will send to login page
     * DefaultLoginPageFilter - if user is not authenticated - it will send to default login page
     *
     * AbstractAuthenticationProcessingFilter - once user enters creds and hits login button - this filter will intercept the request - it will take username and password from request and create authentication object and send it to authentication manager
     * UsernamePasswordAuthenticationFilter - extends AbstractAuthenticationProcessingFilter - it is responsible to take username and password from request and create authentication object and send it to authentication manager
     * these 2 filters will also check whether authentication is required or not
     *
     * doFilter() is the method responsible to intercept the request and do some pre work - it call attemptAuthentication() - present in UsernamePasswordAuthenticationFilter
     * attemptAuthentication() - this will get the username and password from parameters of httpservletrequest object
     * Then it creates of UsernamePasswordAuthenticationToken - which implements Authentication interface - and set the isAuthenticated to false
     * UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken
     * AbstractAuthenticationToken implements Authentication interface
     *
     *
     * method then has getAuthenticationManager().authenticate() - which will return the authentication manager - which will call authenticate() method of authentication manager
     * We get ProviderManager object - which implements AuthenticationManager interface insdie Spring security
     *
     * here we have authenticate() method - it has for loop which will go to each authentication provider and call authenticate method of each authentication provider by passing authentication object
     *
     * Default Authentication provider is DaoAuthenticationProvider - it has authenticate method- which is presen in AbstractUserDetailsAuthenticationProvider
     * * authenticate method - it calls retrieveUser() method - which takes username and authentication object as input if the username is not present in cache
     * * retrieveUser() method calls loadUserByUsername() method of UserDetailsService - loadUserByUsername is present in InMemoryUserDetailsManager class - which is an implementation of UserDetailsService interface
     * loaduserbyusername has a hashmap loaded during start of application - which has username as key and User object as value - User class implements UserDetails interface
     * inside additonalAuthenticationCheck() method - we will check password is matching or not - for that it will take help of PasswordEncoder
     *
     *
     * This all happens only first time - for subsequent requests - it will fetch the authentication object from security context based on session id
     *
     * requiresAuthentication() method of AuthorizationFilter will be false - so provider manager, DaoAuthenticationProvider, UserDetailsService, PasswordEncoder will not be called
     * * loadUserByUsername() method returns UserDetails object - which has username, password, authorities(roles)
     *
     * - which takes help from UserDetailsService and PasswordEncoder
     *
     *
     *
     *
     *
     *
     * */
}
