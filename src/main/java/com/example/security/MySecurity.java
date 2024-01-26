package com.example.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MySecurity {
	
	@Bean
	public DataSource getDataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
				.build();
	}
	

	
	  @Bean 
	  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
	  
		  httpSecurity.authorizeRequests()
		  .requestMatchers("/admin").hasRole("ADMIN")
		  .requestMatchers("/user").hasAnyRole("USER","ADMIN")
		  .requestMatchers("/").permitAll()
		  .and().formLogin();
		  
		  return httpSecurity.build();
	  }
	
	
	
	@Bean
	public UserDetailsService getUsers() {
		
		UserDetails user = User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build();
		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").build();
		
//		return new InMemoryUserDetailsManager(user,admin);
		

		/* for jdbc users details manager */
		JdbcUserDetailsManager jdbcUserDetails = new JdbcUserDetailsManager(getDataSource());
		
//		createUserWithDefaultSchema(jdbcUserDetails,user,admin);

		
		return jdbcUserDetails;
		
	}
	
	private void createUserWithDefaultSchema(JdbcUserDetailsManager jdbcUserDetails, UserDetails user, UserDetails admin) {
		jdbcUserDetails.createUser(user);
		jdbcUserDetails.createUser(admin);
		
	}



	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
}
