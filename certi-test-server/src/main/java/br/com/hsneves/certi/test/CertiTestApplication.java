package br.com.hsneves.certi.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * Classe responsável pela inicialização do sistema
 * 
 * @author Henrique Neves
 *
 */
@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration(exclude = { 
		DataSourceAutoConfiguration.class, 
		AopAutoConfiguration.class, 
		WebSocketServletAutoConfiguration.class, 
		GsonAutoConfiguration.class,
		HttpEncodingAutoConfiguration.class })
public class CertiTestApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(CertiTestApplication.class);
		springApplication.setBanner(new CertiTestBanner());
		springApplication.run(args);
	}
	
}
