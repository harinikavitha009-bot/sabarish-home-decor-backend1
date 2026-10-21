package home_decor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

    // Public website can display projects
    .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()

    // Public customers can submit enquiries
    .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()

    // Admin authentication required to view enquiries
    .requestMatchers(HttpMethod.GET, "/api/contact").authenticated()

    // Admin authentication required to delete enquiries
    .requestMatchers(HttpMethod.DELETE, "/api/contact/**").authenticated()

    // Admin login check
    .requestMatchers("/api/admin/check").authenticated()

    .anyRequest().authenticated()
);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
            "https://sabarishhome-decor.netlify.app",
            "https://sabarish-home-decor.vercel.app",
            "http://127.0.0.1:5500",
            "http://localhost:5500"
        ));

        configuration.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}