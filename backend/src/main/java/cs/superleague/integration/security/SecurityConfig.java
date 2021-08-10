//package cs.superleague.integration.security;
//
//import cs.superleague.user.UserService;
//import cs.superleague.user.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
//import org.springframework.security.web.util.matcher.OrRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//@Configuration
//@EnableWebSecurity
////@EnableJpaRepositories({"cs.superleague.user.repos","cs.superleague.payment.repos","cs.superleague.shopping.repos"})
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    /* Paths that can be extempted from authentication checks*/
//
//    private final RequestMatcher USER_URLS=new OrRequestMatcher(
//            new AntPathRequestMatcher("/api/user/login"),
//            new AntPathRequestMatcher("/api/user/register"),
//            new AntPathRequestMatcher("/error")
//    );
//
//    /* Everything else must be authenticated */
//
//    private final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(USER_URLS);
//
//    @Autowired
//    private UserService userService;
//
//
////    @Override
////    public void configure(WebSecurity web) {
////        web.ignoring().requestMatchers(USER_URLS);
////    }
//
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}
