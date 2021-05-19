package cn.com.datablau.springgraph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
 * @author 邵明华
 * create date 2021年5月6日
 *
 */
//启动时不需要数据库
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})	
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"cn.com.datablau.springgraph.controller"})
@SpringBootApplication
public class SpringgraphApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringgraphApplication.class, args);
	}

}
