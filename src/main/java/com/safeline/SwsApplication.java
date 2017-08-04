package com.safeline;

import com.safeline.entity.Role;
import com.safeline.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { JacksonAutoConfiguration.class }) /* EXCLUIMOS LA AUTOCONFIGURACIÓN DE JACKSON PARA USAR LA NUESTRA DE GSON*/
public class SwsApplication extends WebMvcConfigurerAdapter {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(SwsApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				if (roleRepository.findByName("ROLE_USER") == null && roleRepository.findByName("ROLE_ADMIN") == null){
					List<Role> roleList = new ArrayList<Role>();
					roleList.add(new Role("ROLE_USER"));
					roleList.add(new Role("ROLE_ADMIN"));
					roleRepository.save(roleList);
				}
			}
		};
	}
}