package com.Security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }


    //WebSecurityConfigurerAdapter içerisindeki HTTP security metodunu override ediyoruz
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeRequests().anyRequest().permitAll();//şifreleri devre dışı bıraktık
        http.
                csrf().//Cross-Site-Request-Forgery disabled
                disable().
                authorizeRequests().//istekleri denetle
                antMatchers("/","index","/css/*","/js/*"). //bu uzantılara ve anasayfaya şifresiz girişe izin ver
                permitAll().

                //===============ROLE-BASED Authentication
                antMatchers("/people").hasRole(PersonRole.USER.name()). //USER rolüne sahip kullanıcının erişebileceği path'in tanımlanması
                antMatchers("/people/**").hasRole(PersonRole.ADMIN.name()).//ADMIN rolüne sahip kullanıcının erişebileceği path'in tanımlanması
                anyRequest(). //tüm istekleri denetlea
                authenticated().//şifreli olarak kullan
                and(). //farklı işlemleri birleştirebilmek için
                formLogin(). //form login sayfası olarak giriş yapılsın
                and().
                httpBasic();//basic http kimlik denetimini kullan
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user1 = User
                .builder()
                .username("user")
                .password(passwordEncoder.encode("12345"))
                //.roles("USER")
                //.build();
                .authorities(PersonRole.USER.getGrantedAuthorities())
                .build();

        UserDetails admin1 = User
                .builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                //.roles("ADMIN")
                //.build();
                .authorities(PersonRole.USER.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(user1, admin1);
    }
}
