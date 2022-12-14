package com.fuyin.boot.config;

//import com.fuyin.boot.filter.JwtAuthenticationTokenFilter;
import com.fuyin.boot.filter.JwtAuthenticationTokenFilter;
import com.fuyin.boot.handler.AccessDeniedHandlerImpl;
import com.fuyin.boot.handler.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author
 */
//定义配置类
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled=true)
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;

    //配置认证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //重新返回HttpSecurity对象去继续操作其它啊配置
                .and()
                //请求认证配置
                .authorizeRequests()
                // 对于登录接口 允许匿名访问(未登录状态是可以访问的，已登录并认证就不能再访问)
                // 另外一个用到的方法：  .permitAll()---》 无论登录没登录都可以访问
                .antMatchers("/user/test").permitAll()

                //TODO 测试是否可以访问，但后面要删掉加权限:
                .antMatchers("/admin/adminControllerTest").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/pdf/test").permitAll()
                .antMatchers("/pdf/createPdfDownload").permitAll()
                .antMatchers("/jasper/test").permitAll()
                .antMatchers("/jasper/test2").permitAll()
                .antMatchers("/pdf/createPdfSaveFile").permitAll()
                .antMatchers("/picture/uploadSingle").permitAll()
                .antMatchers("/business/businessTest").permitAll()
//                .antMatchers("/debt/getDebtData").permitAll()
                //authenticated()：要认证后才能访问
                .antMatchers("/user/hello").authenticated()
                //匿名访问，没登陆可以访问，登录了就不可以访问
                .antMatchers("/user/login").anonymous()
                .antMatchers("/admin/login").anonymous()
                // 除上面外的其他所有请求全部需要鉴权认证  .authenticated()--》 任意访问路径可以被认证过的所有用户访问
                .anyRequest().authenticated();

        //把token校验过滤器添加到过滤器链中,addFilterBefore:添加过滤器方法
        // 参数:  Filter filter: 自己定义的过滤器
        //       Class <? extends Filter> beforeFilter: 字节码，用来表示放在哪个过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http.exceptionHandling()
                //配hi认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                //授权失败处理器
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }}
