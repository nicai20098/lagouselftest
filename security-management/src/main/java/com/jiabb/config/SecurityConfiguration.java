package com.jiabb.config;

import com.jiabb.domain.Permission;
import com.jiabb.filter.ValidateCodeFilter;
import com.jiabb.handle.MyAccessDeniedHandler;
import com.jiabb.service.PermissionService;
import com.jiabb.service.impl.MyAuthenticationService;
import com.jiabb.service.impl.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;


/**
 * spring security 配置类
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private MyAuthenticationService myAuthenticationService;
    @Resource
    private DataSource dataSource;

    @Resource
    private ValidateCodeFilter validateCodeFilter;
    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Resource
    private PermissionService permissionService;

    /**
     * 身份安全管理器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);// 使用自定义用户认证
    }

    /**
     * WebSecurity
     */
    @Override
    public void configure(WebSecurity web) {

        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/favicon.ico", "/code/**");
    }

    /**
     * http请求处理方法
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        // 设置/user开头得请求需要ADMIN权限
//        http.authorizeRequests().antMatchers("/user/**").hasRole("ADMIN");
//        http.authorizeRequests().antMatchers("/user/**").access("@myAuthorizationService.check(authentication, httpServletRequest)");
        List<Permission> permissions = permissionService.list();
        for (Permission permission : permissions) {
            http.authorizeRequests().antMatchers(permission.getPermissionUrl()).hasAuthority(permission.getPermissionTag());
        }

        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
        http.sessionManagement()
                .invalidSessionUrl("/toLoginPage");// session无效后跳转的路径
//                .maximumSessions(1)//设置session最大会话数量 ,1同一时间只能有一个
//                .maxSessionsPreventsLogin(true)//当达到最大会话个数时阻止登录
//                .expiredUrl("/toLoginPage");//设置session过期后跳转路径
        // 加在用户名密码过滤器的前面
        http.addFilterBefore(validateCodeFilter,
                UsernamePasswordAuthenticationFilter.class);
        http.formLogin()//开启表单认证
                .loginPage("/toLoginPage")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successForwardUrl("/")
                .successHandler(myAuthenticationService)//**自定义成功处理:**
                .failureHandler(myAuthenticationService)//**自定义失败处理:**
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(myAuthenticationService)//自定义退出处理
                .and().authorizeRequests().antMatchers("/toLoginPage").permitAll()
                .anyRequest().authenticated()
                .and().rememberMe().tokenValiditySeconds(1209600)//token过期时间
                .rememberMeParameter("remember-me")
                .tokenRepository(getPersistentTokenRepository());//所有请求都需要登录认证才能访问; }

        // 关闭csrf防护
        http.csrf().disable();
        //开启csrf防护, 可以设置哪些不需要防护
//        http.csrf().ignoringAntMatchers("/user/save");
        // 允许iframe加载页面
        http.headers().frameOptions().sameOrigin();
        //允许跨域
        http.cors().configurationSource(corsConfigurationSource());
    }

    /**
     * 跨域配置信息源
     *
     */
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration(); // 设置允许跨域的站点
        corsConfiguration.addAllowedOrigin("*");
        // 设置允许跨域的http方法 corsConfiguration.addAllowedMethod("*");
        // 设置允许跨域的请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许带凭证
        corsConfiguration.setAllowCredentials(true);
        // 对所有的url生效
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 持久化token,负责token与数据库之间的相关操作 *
     *
     */
    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();

        tokenRepository.setDataSource(dataSource);//设置数据源
        // 启动时创建一张表, 第一次启动的时候创建, 第二次启动的时候需要注释掉, 否则会报错
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

}
