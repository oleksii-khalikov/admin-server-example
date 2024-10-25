package org.example;

import java.util.Objects;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApplicationContext ctx;

    public SecurityConfig(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers(HttpMethod.GET, "/fetch/addendum/**");
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] names = ctx.getBeanNamesForType(AdminServerProperties.class);
        AdminServerProperties adminServer = null;
        if (names.length > 0) {
            adminServer = ctx.getBean(names[0], AdminServerProperties.class);
        }
        http.authorizeHttpRequests()
                .antMatchers("/**").permitAll()
                .antMatchers((Objects.isNull(adminServer) ? "" : adminServer.getContextPath()) + "/assets/**").permitAll()
                .anyRequest()
                .authenticated();
        return http.csrf(AbstractHttpConfigurer::disable).build();
    }
}
