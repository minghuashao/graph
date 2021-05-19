package cn.com.datablau.springgraph.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
  * 
  * @author 邵明华
  * create date 2021年5月6日
  *
  */

@Configuration
@EnableSwagger2
public class Swagger2Config {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.com.datablau.api.controller"))
                .paths(PathSelectors.any()) 
                .build();				
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
                .title("datablau knowledge graph system")
                .description("DAM to API")
                .termsOfServiceUrl("http://127.0.0.1:8012/")
                .version("1.0")
                .build();
	}


}
