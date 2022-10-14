package com.dms.guyiyao.security.config;
import com.dms.guyiyao.security.filter.JwtAuthenticationTokenFilter;
import com.dms.guyiyao.security.handler.EntryPointUnauthorizedHandler;
import com.dms.guyiyao.security.handler.LogoutSuccessHandler;
import com.dms.guyiyao.security.handler.SecurityAuthenticationFailHandler;
import com.dms.guyiyao.security.handler.SecurityAuthenticationSuccessHandler;
import com.dms.guyiyao.security.provider.SpringSecurityProvider;
import com.dms.guyiyao.security.userdetail.UserdetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.sql.DataSource;
@Configuration
@EnableWebSecurity() //开启Spring Security的功能
@EnableGlobalMethodSecurity(prePostEnabled=true)//开启注解控制权限
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * עSpringSecurityProvider
     */
//    @Autowired
//    private SpringSecurityProvider provider;

    /**
     *AuthenticationSuccessHandler
     */

    @Autowired
    private DataSource dataSource; // 数据源
    @Autowired
    private SecurityAuthenticationSuccessHandler successHandler;
    @Autowired
    private SecurityAuthenticationFailHandler failHandler;
    @Autowired
    EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;
    @Value("${security.disable}")
    Boolean secutityDisable;
    /**
     * 定义需要过滤的静态资源（等价于HttpSecurity的permitAll）
     */
    @Autowired
    UserdetailServiceImpl userdetailService;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    //    @Autowired
//    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        String path=secutityDisable==true?"/**":"/loginPage.html";

        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user/login","/book/getBookInfo/{page}/{size}","/regist").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//        // 自定义身份验证提供者
//        builder.authenticationProvider(provider);
//    }

    /**
     * 返回 RememberMeServices 实例
     *
     * @return the remember me services
     */
//    @Bean
//    public RememberMeServices rememberMeServices() {
//        JdbcTokenRepositoryImpl rememberMeTokenRepository = new JdbcTokenRepositoryImpl();
//
//        // 此处需要设置数据源，否则无法从数据库查询验证信息
//        rememberMeTokenRepository.setDataSource(dataSource);
//        // 启动创建表，创建成功后注释掉
////        rememberMeTokenRepository.setCreateTableOnStartup(true);
//
//        // 此处的 key 可以为任意非空值(null 或 "")，单必须和起前面
//        // rememberMeServices(RememberMeServices rememberMeServices).key(key)的值相同
//        PersistentTokenBasedRememberMeServices rememberMeServices =
//                new PersistentTokenBasedRememberMeServices("INTERNAL_SECRET_KEY", provider.getUserDetailsService(), rememberMeTokenRepository);
//
//        // 该参数不是必须的，默认值为 "remember-me", 但如果设置必须和页面复选框的 name 一致
//        rememberMeServices.setParameter("remember-me");
//        return rememberMeServices;
//    }

    /*配置加密器*/
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
//        $2a$10$t5Zv6NcdCWvKF/Sls9cgZe
    }

    /*用户实现注册成功后的自动登录*/
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return  super.authenticationManagerBean();
    }
}