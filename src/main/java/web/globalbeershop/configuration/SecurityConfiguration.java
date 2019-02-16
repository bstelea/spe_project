package web.globalbeershop.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //
    @Autowired
    private DataSource dataSource;


    //name of realm of accessible resources
    public static final String REALM_NAME = "globalbeershop";

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
//        for when using mySQL DB
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, role from users where username=?")
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
//
//        //for when using H2 DB
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(bCryptPasswordEncoder.encode("password")).roles("ADMIN");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //PAGE ACCESS
                .authorizeRequests()

                //can only access user pages if logged in as user
                .antMatchers("/user/**").hasRole("USER")

                //can only access user pages if logged in as user
                .antMatchers("/admin/**").hasRole("ADMIN")

                //all other pages can be accessed by anyone
                .anyRequest().permitAll()

                .antMatchers("/h2_console/**").permitAll()



                .and()

                //LOGIN
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login/error")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();

                http.csrf().disable();
                http.headers().frameOptions().disable();


    }
}

