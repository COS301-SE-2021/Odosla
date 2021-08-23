package cs.superleague.integration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

    //Only these paths may be exempt from authentication checks.
    private final RequestMatcher DRIVER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("delivery/addDeliveryDetail"),
            new AntPathRequestMatcher("delivery/assignDriverToDelivery"),
            new AntPathRequestMatcher("delivery/getNextOrderForDriver"),
            new AntPathRequestMatcher("delivery/updateDeliveryStatus")
    );
    private final RequestMatcher SHOPPER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("shopping/getQueue")

    );

    private final RequestMatcher CUSTOMER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("delivery/getDeliveryStatus"),
            new AntPathRequestMatcher("delivery/trackDelivery"),
            new AntPathRequestMatcher("delivery/getDeliveryDriverByOrderId")
            );

    private final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("delivery/getDeliveryDetail"),
            new AntPathRequestMatcher("analytics/createUserReport"),
            new AntPathRequestMatcher("analytics/createFinancialReport"),
            new AntPathRequestMatcher("analytics/createMonthlyReport"),
            new AntPathRequestMatcher("shopping/updateShoppers")

    );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/shopping/getStores").hasAuthority("ROLE_SHOPPER")
                .antMatchers(HttpMethod.POST, "/user/loginUser").permitAll()
                .anyRequest().authenticated();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}