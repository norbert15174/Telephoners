package pl.telephoners.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.telephoners.JWT.JwtFilter;
import pl.telephoners.services.UserAppService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserAppService userAppService;

    @Autowired
    public WebSecurityConfig(UserAppService userAppService){
        this.userAppService = userAppService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/auth/register")
                .antMatchers("/auth/login");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/persondata/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/projects/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/auth/**").hasAnyRole("ADMIN")
                .and()
                .addFilterBefore(new JwtFilter(userAppService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.csrf().disable();
    }

}
