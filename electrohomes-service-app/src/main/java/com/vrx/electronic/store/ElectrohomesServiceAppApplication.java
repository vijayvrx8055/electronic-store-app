package com.vrx.electronic.store;

import com.vrx.electronic.store.config.SecurityConfig;
import com.vrx.electronic.store.entity.Role;
import com.vrx.electronic.store.repository.RoleRepository;
import io.jsonwebtoken.lang.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

@SpringBootApplication
public class ElectrohomesServiceAppApplication implements CommandLineRunner {

//	@Autowired
//	private PasswordEncoder passwordEncoder;

//    @Autowired
//    private RoleRepository roleRepository;

    @Value("${role.normal.id}")
    private String normalRoleId;

    @Value("${role.admin.id}")
    private String adminRoleId;

    public static void main(String[] args) {
        SpringApplication.run(ElectrohomesServiceAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//		System.out.println(passwordEncoder.encode("abcd1234"));
//        try {
//            Role role_admin = Role.builder().roleId(adminRoleId)
//                    .roleName("ROLE_ADMIN").build();
//            Role role_normal = Role.builder().roleId(normalRoleId)
//                    .roleName("ROLE_NORMAL").build();
//            roleRepository.save(role_admin);
//            roleRepository.save(role_normal);
//        } catch (Exception ex) {
//            System.out.println("Error in creating role!!");
//        }
    }
}
