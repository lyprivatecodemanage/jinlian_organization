package com.xiangshangban.organization;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.xiangshangban.organization.filter.ServletFilter;


@SpringBootApplication
public class OrganizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizationApplication.class, args);
	}
	
	@Bean
    public FilterRegistrationBean filterRegistrationBean() {
 	   FilterRegistrationBean registrationBean = new FilterRegistrationBean();
 	   ServletFilter weChatFilter = new ServletFilter();
 	   registrationBean.setFilter(weChatFilter);
 	   List<String> urlPatterns = new ArrayList<String>();
 	   urlPatterns.add("/*");
 	   registrationBean.setUrlPatterns(urlPatterns);
 	   return registrationBean;
 	}
}
