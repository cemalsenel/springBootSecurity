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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//METHOD-BASED Authentication anatasyonlarını aktif hale getirir
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }


    //WebSecurityConfigurerAdapter içerisindeki HTTP security metodunu override ediyoruz
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeRequests().anyRequest().permitAll();//şifreleri devre dışı bıraktık
        http.
                csrf().disable().//Cross-Site-Request-Forgery disabled
                authorizeRequests().//istekleri denetle
                antMatchers("/","index","/css/*","/js/*"). //bu uzantılara ve anasayfaya şifresiz girişe izin ver
                permitAll().
                //===============ROLE-BASED Authentication
                // USER rolune sahip kullanicinin erişebileceği path'in tanımlanmas
//                antMatchers("/kisiler/ara/**").hasRole(KisiRole.USER.name()).
                // ADMIN rolune sahip olan kullanicinin erişebeilceği paty in tanimlanmasi
//                antMatchers("/kisiler/**").hasRole(KisiRole.ADMIN.name()).

                // ==================== METHOD-BASED AUTHENTICATION =====================
                // Metot-tabanlı kimlik denetimi için yapılması gereken adımlar.
                // 1- @EnableGlobalMethodSecurity(prePostEnabled = true) anotasyonunun Security
                //    class'ına konulması gerekir.
                // 2- Rollerin ROLE_ISIM şeklinde tanımlanması gerekir. Bunlar hard-coded olabileceği
                //    gibi KisiRole içerisinde varolan rollerin kullanılmasi ile de olabilir.
                //    Tabi bunun için Enum olan role isimleri ile sabit "ROLE_" kelimesini birleşirecek bir
                //    metot yazmak gerekir.
                // 3- UserDetailService metodu içerisinde kişilerin roles() tanımlamalarını authorities() olarak ,
                //    değiştirmeli ve KisiRole isimlerini ROLE_ISIM şeklinde almak için KisiRole içerisinde yazdığımız
                //    metotu kullanmalıyız.
                // 4- İzinleri ayarlamak için KisiContorller'a giderek metot başına hangi Rollere izin verileceğini
                //    belirlemek gerekmektedir.Bunun için  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                //    anotasyonu kullanılabilir.
//                antMatchers("/people").hasRole(PersonRole.USER.name()). //USER rolüne sahip kullanıcının erişebileceği path'in tanımlanması
//                antMatchers("/people/**").hasRole(PersonRole.ADMIN.name()).//ADMIN rolüne sahip kullanıcının erişebileceği path'in tanımlanması
                anyRequest(). //tüm istekleri denetlea
                authenticated().//şifreli olarak kullan
                and(). //farklı işlemleri birleştirebilmek için
                formLogin(). //form login sayfası olarak giriş yapılsın


                // === kendi login sayfamızı kullanmak için======
                // 1- Webapp klasöründe yeni login.html sayfası oluşturlur.
                //  2- HomeController içerisinde bir RequestMapping metodu ile path tanımlanır
                //  3- SecurityConfig içerisinde loginPage(/login) metodu ile aktif hale getirilir.
                and().
                httpBasic();//basic http kimlik denetimini kullan
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user1 = User
                .builder()
                .username("user")
                .password(passwordEncoder.encode("456"))
                //.roles("USER").build();
                //roles(PersonRole.USER.name()).build();
                .authorities(PersonRole.USER.getGrantedAuthorities()).build();

        UserDetails admin1 = User
                .builder()
                .username("admin")
                .password(passwordEncoder.encode("123"))
                //.roles("ADMIN").build(); // Hard-Coded Role tahsisi
                //roles(PersonRole.ADMIN.name()).build();
                .authorities(PersonRole.ADMIN.getGrantedAuthorities()).build();// Method-based aut için role tahsis.

        return new InMemoryUserDetailsManager(user1, admin1);
    }
}
