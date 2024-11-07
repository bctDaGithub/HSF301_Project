// package hsf301.fe.project.security;

// import javax.sql.DataSource;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.provisioning.JdbcUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class MySecurity {

//     @Bean
//     @Autowired
//     public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
//         JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        
//         userDetailsManager.setUsersByUsernameQuery("select email, password, active from Users where email=?");
        
//         userDetailsManager.setAuthoritiesByUsernameQuery("select email, role from Users where email=?");
        
//         return userDetailsManager;
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//         http.authorizeHttpRequests(
//             configurer -> configurer
//                     .requestMatchers("/public/**").permitAll() 
                    
//                     .requestMatchers("/admin/**").hasRole("ADMIN") 
//                     .requestMatchers("/customer/**").hasAuthority("CUSTOMER") 
//                     .requestMatchers("/seller/**").hasAuthority("SELLER") 
                    
//                     .requestMatchers("/shared/**").hasAnyAuthority("ADMIN", "CUSTOMER", "SELLER") 
                    
//                     .anyRequest().authenticated() 
//         ).formLogin(
//                 form -> form.loginPage("/showLoginPage") 
//                              .loginProcessingUrl("/authenticateTheUser") 
//                              .permitAll() 
//         ).logout(
//                 logout -> logout.permitAll() 
//         ).exceptionHandling(
//                 configurer -> configurer.accessDeniedPage("/showPage403") 
//         );
    
//         return http.build();
//     }
// }
