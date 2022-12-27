package br.com.hsneves.certi.test.web.security;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.firewall.HttpStatusRequestRejectedHandler;
import org.springframework.security.web.firewall.RequestRejectedHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.hsneves.certi.test.web.utils.AjaxTimeoutRedirectFilter;

/**
 * Configurações de segurança do sistema
 * 
 * @author Henrique Neves
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class CertiTestSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String JSESSIONID_COOKIE = "JSESSIONID";

	private static final String REMEMBER_ME_COOKIE = "REMEMBERME";

	private static final String[] LOGOUT_DELETE_COOKIES = { JSESSIONID_COOKIE, REMEMBER_ME_COOKIE };

	private static final String[] PERMIT_ALL = { 
			"/3rdps/**", 
			"/css/**", 
			"/img/**", 
			"/rest/pokemon/all/**", // listar todos os pokemons
			"/rest/pokemon/catch/**", // capturar pokemon
			"/rest/pokemon/pokeballs/**", // recuperar todas as suas pokebolas
			"/sse/**" // conectar ao SSE para receber notificações via push
			};

	private static final String LOGIN_PAGE = "/login";

	private static final String REMEMBER_ME_KEY = "AGUgdjkfD^%%sajiusyiuy";

	private static final int REMEMBER_ME_TOKEN_VALIDITY = 604800;

	@Value("${server.max.sessions.per.user:-1}")
	private Integer maxSessionsPerUser;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(this.userDetailsService);
		authProvider.setPasswordEncoder(getPasswordEncoder());
		return authProvider;
	}

	@Bean
	public Filter ajaxTimeOutRedirectFilter() {
		AjaxTimeoutRedirectFilter ajaxTimeoutFilter = new AjaxTimeoutRedirectFilter();
		return ajaxTimeoutFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable().sessionManagement().maximumSessions(this.maxSessionsPerUser).expiredUrl(LOGIN_PAGE).sessionRegistry(sessionRegistry()).and().and()
				.addFilterAfter(ajaxTimeOutRedirectFilter(), ExceptionTranslationFilter.class).authorizeRequests().antMatchers(PERMIT_ALL).permitAll().anyRequest().authenticated()
				.and().formLogin().loginPage(LOGIN_PAGE).permitAll().and().logout().invalidateHttpSession(true).deleteCookies(LOGOUT_DELETE_COOKIES).logoutSuccessUrl(LOGIN_PAGE)
				.and().rememberMe().rememberMeCookieName(REMEMBER_ME_COOKIE).key(REMEMBER_ME_KEY).tokenValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY);
	}

	@Bean
	RequestRejectedHandler requestRejectedHandler() {
		return new HttpStatusRequestRejectedHandler();
	}

	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new CertiTestPasswordEncoder();
	}
}