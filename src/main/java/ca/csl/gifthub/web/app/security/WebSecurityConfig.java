package ca.csl.gifthub.web.app.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.csl.gifthub.core.model.account.User;
import ca.csl.gifthub.web.app.WebConfig;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] OPEN_PAGES = { };
    private static final String CONTENT_SECURITY_POLICY = "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval' "
            + "https://apis.google.com https://ajax.googleapis.com;"
            + " connect-src 'self'; img-src 'self'; style-src 'self' 'unsafe-inline';"
            + " frame-src 'self'; object-src 'self';";

    private UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.userPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder userPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(User.DEFAULT_USER_PASSWORD_HASH_ID, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(User.DEFAULT_USER_PASSWORD_HASH_ID, encoders);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // setup authorized pages
            .authorizeRequests()
                // allow all users to access these pages no matter authentication status
                .antMatchers(OPEN_PAGES).permitAll()
                .antMatchers(WebConfig.STATIC_RESOURCE_PAGES).permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                // ensure all other requests are authenticated
                .anyRequest().authenticated()
                .and()
            // setup login redirection and logic
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/auth")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/home")
                .permitAll(true)
                .and()
            // setup logout
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
            // setup sessionManagement
            .sessionManagement()
                .invalidSessionUrl("/login")
                .sessionFixation()
                .migrateSession()
                .and()
            // enable csrf protection
            .csrf().and()
            // add security headers
            .headers()
                .contentSecurityPolicy(CONTENT_SECURITY_POLICY)
                .and()
            .frameOptions()
                .sameOrigin();
    }
}
