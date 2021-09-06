package cs.superleague.integration.security;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${env.SHOPPER_AUTHORITY}")
    private String SHOPPER_AUTHORITY = "stub";

    @Value("${env.DRIVER_AUTHORITY}")
    private String DRIVER_AUTHORITY = "stub";

    @Value("${env.ADMIN_AUTHORITY}")
    private String ADMIN_AUTHORITY = "stub";

    @Value("${env.CUSTOMER_AUTHORITY}")
    private String CUSTOMER_AUTHORITY = "stub";

    @Value("${env.SECRET}")
    private String SECRET = "stub";
    //Only these paths may be exempt from authentication checks.
    private final RequestMatcher DRIVER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("delivery/addDeliveryDetail"),
            new AntPathRequestMatcher("delivery/assignDriverToDelivery"),
            new AntPathRequestMatcher("delivery/getNextOrderForDriver"),
            new AntPathRequestMatcher("delivery/updateDeliveryStatus"),
            new AntPathRequestMatcher("user/updateDriverShift"),
            new AntPathRequestMatcher("user/setCurrentLocation"),
            new AntPathRequestMatcher("user/updateDriverDetails"),
            new AntPathRequestMatcher("user/getCustomerByUUID")
    );
    private final RequestMatcher SHOPPER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("shopping/getQueue"),
            new AntPathRequestMatcher("user/updateShopperShift"),
            new AntPathRequestMatcher("user/completePackagingOrder"),
            new AntPathRequestMatcher("user/updateShopperDetails"),
            new AntPathRequestMatcher("shopping/getNextQueued")


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

    private final RequestMatcher NO_AUTHROTITY = new OrRequestMatcher(
            new AntPathRequestMatcher("/user/loginUser"),
            new AntPathRequestMatcher("/user/registerShopper"),
            new AntPathRequestMatcher("/user/registerDriver"),
            new AntPathRequestMatcher("/user/registerCustomer"),
            new AntPathRequestMatcher("/user/verifyAccount")
    );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(SHOPPER_URLS).hasAuthority(SHOPPER_AUTHORITY)
                .requestMatchers(DRIVER_URLS).hasAuthority(DRIVER_AUTHORITY)
                .requestMatchers(ADMIN_URLS).hasAuthority(ADMIN_AUTHORITY)
                .requestMatchers(CUSTOMER_URLS).hasAuthority(CUSTOMER_AUTHORITY)
                .requestMatchers(NO_AUTHROTITY).permitAll()
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