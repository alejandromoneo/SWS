package com.safeline.configuration;

import com.safeline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Created by alejandromoneomartinez on 11/7/17.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Autowired
    private CustomAuthFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private static final String SALT = "salt"; // Salt should be protected carefully

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/css/**",
            "/js/**",
            "/img/**",
            "/i18n/**",
            "/about/**",
            "/login/**",
            "/contact/**",
            "/error/**/*",
            "/console/**",
            "/users/**",
            "/ajax/users/**",
            "/signup/**",
            "/sourcelist/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()
                .and()
            .httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint) //Habilitamos la seguridad en el api rest
                .and()
            .formLogin() //a login form is showed when no authenticated request
                .loginPage("/login")
                //.failureUrl("/login?error")
                .failureHandler(customAuthenticationFailureHandler) //en vez de la linea anterior, ponemos un custom para controlar si la cuenta esta habilitada
                .defaultSuccessUrl("/index")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
                .and()
            .rememberMe()
                .rememberMeParameter("remember-me") //atención sin el rememberMeParameter no funciona!!!
                .tokenValiditySeconds(2419200) //28 days
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //necesario ponerlo así en vez de .logoutUrl("/logout") cuando .csrf() esta habilitado, ya que tendriamos que hacer un post en ese caso
                .logoutSuccessUrl("/login")
                .deleteCookies("remember-me")
                .permitAll()
                .and()
            .csrf()
                .ignoringAntMatchers("/sourcelist/**"); //deshabilitamos la proteccion csrf solo en la api source
    }

    @Component
    class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            /* Types of exceptions here
            if (exception instanceof BadCredentialsException) {
            } else if (exception instanceof CredentialsExpiredException) {
            } else if (exception instanceof DisabledException) {
            } else if (exception instanceof LockedException) {
            }
            */

            String username = request.getParameter("email");

            if (exception instanceof DisabledException)
                getRedirectStrategy().sendRedirect(request, response, "/login?disabled=true&email=" + username);    //aparte del error le pasamos el email para que no se borre cada vez que falla el login
            else
                getRedirectStrategy().sendRedirect(request, response, "/login?error=true&email=" + username);
        }
    }
}
