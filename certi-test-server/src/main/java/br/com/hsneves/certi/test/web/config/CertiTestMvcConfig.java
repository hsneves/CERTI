package br.com.hsneves.certi.test.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração do MVC do sistema
 * 
 * @author Henrique Neves
 *
 */
@Configuration
public class CertiTestMvcConfig implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(CertiTestMvcConfig.class);

	@Value("${spring.data.rest.base-path:/rest}")
	private String restPath;

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		logger.debug("Adicionando path prefix para os serviços REST, path = [" + this.restPath + "]");
		configurer.addPathPrefix(this.restPath, HandlerTypePredicate.forAnnotation(RestController.class));
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		logger.debug("Adicionando mapeamento CORS para /** e *");
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(false).maxAge(3600);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		logger.debug("Adicionando controladores da view para /, /home, /login");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/login").setViewName("login");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		logger.debug("Adicionando resource handler para /home.html em classpath:resources/templates/home.html");
		registry.addResourceHandler("/home.html").addResourceLocations("classpath:resources/templates/home.html").setCachePeriod(0);
	}

}