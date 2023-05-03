package com.example31._Spring_Security.configs;

import com.example31._Spring_Security.handler.LoginSuccessHandler;
import com.example31._Spring_Security.handler.loginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final DataSource dataSource;

    @Autowired
    public WebSecurityConfig (DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/hello").authenticated()
                        .requestMatchers("/read_profile/**").hasAnyRole("USER")
                        .requestMatchers("/only_for_admins/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(new LoginSuccessHandler())
                        .failureHandler(new loginFailureHandler())
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        String usersByUsernameQuery = "select username, password, active from userstable where username = ?";
        String authsByUserQuery = "SELECT userstable.username, roles.username\n" +
                "FROM userstable\n" +
                "LEFT OUTER JOIN users_roles\n" +
                "  ON userstable.id = users_roles.user_id\n" +
                "LEFT OUTER JOIN roles\n" +
                "  ON users_roles.role_id = roles.id\n" +
                "WHERE userstable.username = ?";
        //String authsByUserQuery = "select u.username, ur.roles from users u inner join user_roles ur on u.id = ur.user_id where u.username=?";

        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery(usersByUsernameQuery);
        users.setAuthoritiesByUsernameQuery(authsByUserQuery);
        return users;
    }

}