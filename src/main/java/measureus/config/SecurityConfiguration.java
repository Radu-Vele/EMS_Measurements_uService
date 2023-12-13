package measureus.config;

import lombok.RequiredArgsConstructor;
import measureus.enums.UserRole;
import measureus.filters.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static measureus.constants.SecurityConstants.NO_AUTH_REQUIRED_PATTERNS;
import static measureus.constants.SecurityConstants.USER_AUTH_REQUIRED_PATTERNS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(NO_AUTH_REQUIRED_PATTERNS)
                        .permitAll()
                        .requestMatchers(USER_AUTH_REQUIRED_PATTERNS)
                        .hasAnyAuthority(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                )
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}