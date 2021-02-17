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
                .antMatchers("/auth/login")
                .antMatchers("/posts/page/**")
                .antMatchers("/posts/name/**")
                .antMatchers("/sendmail/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//            http.authorizeRequests().antMatchers("/**").permitAll();

        http.authorizeRequests()
                .antMatchers("/posts/addpost").hasRole("ADMIN")
                .antMatchers("/posts/{id}").hasRole("ADMIN")
                .antMatchers("/posts/photos/add/**").hasRole("ADMIN")
                .antMatchers("/posts/update/{postId}").hasRole("ADMIN")
                .antMatchers("/posts/author/**").hasRole("ADMIN")
                .antMatchers("/persondata/").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/persondata/lastname/**").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/persondata/lastname/**").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/persondata/admin/**").hasRole("ADMIN")
                .antMatchers("/persondata/userinfo").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/add").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/add/admin/personal").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/admin/**").hasRole("ADMIN")
                .antMatchers("/projects/enrol/{id}").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/leave").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/participant").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/leader/delete/{id}").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/recruitment").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/finish").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/leader/get/{id}").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/projects/addmainphoto/**").hasAnyRole("ADMIN","USER","MEMBER")
                .antMatchers("/persondata/set/photo").hasAnyRole("ADMIN","USER","MEMBER")
                .and()
                .addFilterBefore(new JwtFilter(userAppService), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
    }

}
