package hsf301.fe.project.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MySecurity {
    @Bean
    @Autowired
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        
        userDetailsManager.setUsersByUsernameQuery("select email, password, active from Users where email=?");
        
        userDetailsManager.setAuthoritiesByUsernameQuery("select email, role from Users where email=?");
        
        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(
            configurer -> configurer
                    .requestMatchers("/public/**").permitAll()
                    
                    // Quy định quyền truy cập cho từng vai trò
                    .requestMatchers("/admin/**").hasRole("1") // Chỉ admin
                    .requestMatchers("/customer/**").hasRole("2") // Chỉ customer
                    .requestMatchers("/seller/**").hasRole("3") // Chỉ seller
                    .requestMatchers("/shipper/**").hasRole("4") // Chỉ shipper
                    
                    // Cấp quyền truy cập dựa trên các vai trò khác nhau
                    .requestMatchers("/shared/**").hasAnyRole("1", "2", "3", "4") // Các trang chung cho tất cả các vai trò
                    
                    .anyRequest().authenticated() // Các request khác yêu cầu xác thực
        ).formLogin(
                form -> form.loginPage("/showLoginPage")
                             .loginProcessingUrl("/authenticateTheUser")
                             .permitAll()
        ).logout(
                logout -> logout.permitAll()
        ).exceptionHandling(
                configurer -> configurer.accessDeniedPage("/showPage403")
        );
    
        return http.build();
    }
    


}
