package com.Security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //WebSecurityConfigurerAdapter içerisindeki HTTP security metodunu override ediyoruz
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeRequests().anyRequest().permitAll();//şifreleri devre dışı bıraktık
        http.
                authorizeRequests().//istekleri denetle
                anyRequest(). //tüm istekleri denetle
                authenticated().//şifreli olarak kullan
                and(). //farklı işlemleri birleştirebilmek için
                formLogin(). //form login sayfası olarak giriş yapılsın
                and().
                httpBasic();//basic http kimlik denetimini kullan
    }
}
