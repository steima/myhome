package org.steinbauer.myhome;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.exceptionHandling()
			.and()
		.authorizeRequests()
			.antMatchers(
				"/login", "/health",
				"/webjars/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
        .logout()
        	.permitAll()
        	.and()
		.httpBasic()
			.and()
		.csrf().disable();
	}
	
}
