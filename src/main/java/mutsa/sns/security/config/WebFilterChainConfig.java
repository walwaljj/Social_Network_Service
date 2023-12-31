package mutsa.sns.security.config;

import lombok.RequiredArgsConstructor;
import mutsa.sns.security.jwt.JwtFilter;
import mutsa.sns.security.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebFilterChainConfig {
    private final JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/users/login",
                                "/v1/users/sign",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(new JwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .deleteCookies("jwtToken")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/v1/users/logout"))
                        .logoutSuccessUrl("/v1/users/login"))

                .build();
    }
}
