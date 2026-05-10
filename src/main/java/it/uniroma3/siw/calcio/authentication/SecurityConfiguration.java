package it.uniroma3.siw.calcio.authentication;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userdetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(this.dataSource);
        manager.setUsersByUsernameQuery("SELECT username, password, 1 as enabled FROM app_user WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT username, role FROM app_user WHERE username = ?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.GET, "/", "/index", "/matches", "/matches/**",
                    "/teams", "/teams/**", "/tournaments", "/tournaments/**", "/login", "/register").permitAll();
            authorize.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll();
            authorize.requestMatchers("/admin/**").hasRole("ADMIN");
            authorize.anyRequest().authenticated();
        });
        http.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.failureUrl("/login?error=true");
        });
        http.logout(logout -> {
            logout.logoutUrl("/logout"); // endpoint trigger for logout
            logout.invalidateHttpSession(true); // invalidate http session
            logout.deleteCookies("JSESSIONID"); // delete session cookie
            logout.clearAuthentication(true); // removes the authentication from the security context
            logout.permitAll(); // allow all to access logout
        });
        return http.build();
    }

}