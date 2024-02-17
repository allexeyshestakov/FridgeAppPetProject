package com.example.fridgeapppetproject;

import jdk.jfr.Enabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//  Свойство prePostEnabled позволяет создавать аннотации Spring Security до / после публикации.
//  Свойство securedEnabled определяет, должна ли быть включена аннотация @Secured.
//  Свойство jsr250Enabled позволяет нам использовать аннотацию @RoleAllowed.
// @Secured используется для указания списка ролей в методе
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Здесь @Secured(“ROLE_VIEWER”) аннотация определяет,
    // что только пользователи, у которых есть роль ROLE_VIEWER,
    // могут выполнять метод GetUserName.
//    @Secured("USER")
//    public String getUsername() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        return securityContext.getAuthentication().getName();
//    }
    // В этом случае в конфигурации указано,
    // что если у пользователя есть либо ROLE_VIEWER , либо ROLE_EDITOR,
    // этот пользователь может вызвать метод isValidUsername.
//    @Secured({ "USER", "MANAGER" })
//    public boolean isValidUsername(String username) {
//        return userRoleRepository.isValidUsername(username);
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // .anonymous() - доступ к странице только НЕ авторизированному пользователю
        // .denyAll - никто не имеет доступ к данной странице
        // .authentificated() - доступ к странице только авторизованному пользователю
        // .rememberMe() - доступ к странице для пользователей с долго живущей ссесией
        // .fullyAuthenticated() - доступ к странице для пользователей с полной авторизацей, а не с восстановленной(ремембе ми)
        // .hasAuthority("view") - доступ к странице для пользователей с правом(полномочиями) view
        // .hasAnyAuthority("view" , "delete") - так же как выше только для нескольких
        // hasRole("admin") - доступ к странице для пользователей с ролью admin / hasAnyRole
        // .access(authorization, object) -> new Authorization("c.norris".equals(authorization.get().getName()))
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers( "/admin").hasRole("ADMIN")
                        .requestMatchers( "/user").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("1")
                        .password("1")
                        .roles("USER")
                        .build();

        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("2")
                        .password("2")
                        .roles("ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
