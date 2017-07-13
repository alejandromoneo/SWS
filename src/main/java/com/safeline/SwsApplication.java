package com.safeline;

import com.safeline.entity.Role;
import com.safeline.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SwsApplication extends WebMvcConfigurerAdapter {

	@Autowired
	private RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(SwsApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				if (roleService.findByName("ROLE_USER") == null && roleService.findByName("ROLE_ADMIN") == null){
					List<Role> roleList = new ArrayList<Role>();
					roleList.add(new Role("ROLE_USER"));
					roleList.add(new Role("ROLE_ADMIN"));
					roleService.save(roleList);
				}
			}
		};
	}
}