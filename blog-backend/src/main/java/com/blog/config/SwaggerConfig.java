package com.blog.config;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.blog.security.SystemFilter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

	@Autowired
	private SystemFilter filter;
	
	@Bean
	Docket productApi(ServletContext servletContext) {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.blog.resource"))
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.apiInfo(informationApi()
						.build());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/swagger-ui.html")
		.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
		.addResourceLocations("classpath:/META-INF/resources/webjars/");
		
		registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/");

	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(filter);
	}

	private ApiInfoBuilder informationApi() {

		ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();

		apiInfoBuilder.title("API referente ao blog");
		apiInfoBuilder.description("Api para construção do blog");
		apiInfoBuilder.version("1.0");
		apiInfoBuilder.termsOfServiceUrl("Termo de uso: Propriedade do Victor.");
		apiInfoBuilder.license("Licença - Privada");
		apiInfoBuilder.licenseUrl("http://vgodoy.com.br");
		apiInfoBuilder.contact(contact());

		return apiInfoBuilder;

	}

	private Contact contact() {
		return new Contact(
				"Grupo de Desenvolvimento Godoy",
				"http://vgodoy.com.br", 
				"vp.godoy@outlook.com"
				);
	}
	
	@Override 
	@SuppressWarnings("deprecation")
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) { 
	    configurer.favorPathExtension(false); 
	}
	
}
