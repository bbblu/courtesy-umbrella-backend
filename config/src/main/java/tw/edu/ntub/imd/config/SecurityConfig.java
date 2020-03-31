package tw.edu.ntub.imd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tw.edu.ntub.imd.config.entrypoint.CustomEntryPoint;
import tw.edu.ntub.imd.config.filter.CustomLoginFilter;
import tw.edu.ntub.imd.config.filter.JwtAuthenticationFilter;
import tw.edu.ntub.imd.config.handler.CustomAuthenticationSuccessHandler;
import tw.edu.ntub.imd.config.handler.CustomerAccessDeniedHandler;
import tw.edu.ntub.imd.config.provider.CustomAuthenticationProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${server.image.name}")
    private String imageUrlName;
    @Value("${server.file.name}")
    private String fileUrlName;
    private UserDetailsService userDetailsService;
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(
            @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, passwordEncoder()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    這個表示哪些頁面"不會用到SpringSecurity"，相當於xml中的security="none"
    //    代表在這些連結中會抓不到登入資訊
    //    即SpringContextHolder.getContext() = null
    //    因此這些只能用在靜態資源上
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/static/**", String.format("/%s/**", imageUrlName), String.format("/%s/**", fileUrlName));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()// 出錯時的例外處理
                .authenticationEntryPoint(new CustomEntryPoint())// 未登入處理
                .accessDeniedHandler(new CustomerAccessDeniedHandler())// 偵測權限不足的處理
                .and()
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLoginFilter(authenticationManager(), customAuthenticationSuccessHandler), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()// 設定Requests的權限需求
                .antMatchers(HttpMethod.GET, "/", "/index", "/user/register", "/login", "/logout", "/timeout", "/error**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .anyRequest()// 表示除了上述請求，都需要權限
                .authenticated()
                .and()
                .formLogin()// 設定Login，如果是用Form表單登入的話
                .loginPage("/login")// 設定Login頁面的URL
                .loginProcessingUrl("/login")// 設定Login動作的URL
                .failureUrl("/login?error")// 設定Login失敗的URL
                .permitAll()// Login不需要權限
                .usernameParameter("account")
                .passwordParameter("password")
                .and()
                .logout()// 設定Logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))// 設定Logout URL
                .logoutSuccessUrl("/login")// 設定登出成功後的URL
                .deleteCookies("JSESSIONID")
                .and()
                .sessionManagement()// Session管理
                .sessionFixation()// Session固定ID保護
                .migrateSession()// 每次登入，都會產生新的，並將舊的屬性複製，預設值
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .invalidSessionUrl("/timeout")// Session過期時的URL導向
                .maximumSessions(1)// 設定Session數只能一個
                .expiredUrl("/timeout")// 設定因為再次登入而導致的URL過期的URL導向
        ;
    }
}
