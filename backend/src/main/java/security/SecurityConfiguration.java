package security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .httpBasic()
      .and()
        .authorizeRequests()
          .antMatchers("/index.html", "/", "/homepage", "/login").permitAll()
          .anyRequest().authenticated();
          
        return http.build();
    }

}
