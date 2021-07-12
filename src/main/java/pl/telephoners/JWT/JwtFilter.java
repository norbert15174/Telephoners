package pl.telephoners.JWT;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.telephoners.configurations.WebSecurityConfig;
import pl.telephoners.models.UserApp;
import pl.telephoners.services.UserAppService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;


public class JwtFilter extends OncePerRequestFilter {

    @Value("${Algorithm-key}")
    private String key;

    UserAppService userAppService;

    public JwtFilter(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String cookieName = "token";
        Optional <String> authorization = Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie-> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
        if(authorization.isEmpty()){
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization Failed");
        } else{
            try {
                UsernamePasswordAuthenticationToken authenticationToken = getUsernamePasswordAuthenticationToken(authorization.get(),httpServletResponse);
                if(authenticationToken == null){
                    httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Wrong token");
                } else {
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }
            }catch ( IOException e ){
                System.err.println("wrong token");
            }

        }
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String authorization, HttpServletResponse httpServletResponse) throws IOException {
        try {

            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512("x!A%D*G-KaPdSgVkYp3s6v8y/B?E(H+MbQeThWmZq4t7w!z$C&F)J@NcRfUjXn2r")).build();
            DecodedJWT verify = jwtVerifier.verify(authorization);

            String username = verify.getClaim("username").asString();
            String password = verify.getClaim("password").asString();
            UserDetails userDetails = userAppService.accountVerify(username, password);

            if (userDetails == null) return null;

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (Exception e) {
            System.err.println("message : " + e.getMessage());
            return null;
        }


    }


}
