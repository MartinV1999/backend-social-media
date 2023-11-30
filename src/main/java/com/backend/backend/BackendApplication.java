package com.backend.backend;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.backend.backend.models.entities.Role;
import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.services.UserService;

@SpringBootApplication
public class BackendApplication{

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Component
	public class Inicializador implements ApplicationListener<ContextRefreshedEvent> {
			@Autowired
			private UserService userService;

			@Autowired
			private com.backend.backend.services.RoleService roleService;

			public void InicializadorUser(UserService userService, com.backend.backend.services.RoleService roleService) {
				this.userService = userService;
				this.roleService = roleService;
			}

			// Método que se ejecuta al iniciar la aplicación
			@Override
			public void onApplicationEvent(ContextRefreshedEvent event) {
				// Verifica si es la primera vez que se inicia la aplicación
				Long id = 1L;
				Optional<UserDto> op = userService.findById(id);
				Optional<Role> or = roleService.findByName("ROLE_ADMIN");

				if (event.getApplicationContext().getParent() == null && !op.isPresent() && !or.isPresent()) {
					// Lógica de inicialización (por ejemplo, insertar el usuario maestro)
					insertarRoles();
					insertarUsuarioMaestro();
				}
			}
			// Método para insertar un usuario maestro
			private void insertarUsuarioMaestro() {
				User user = new User();

				user.setFirstname("Admin");
				user.setLastname("Maestro");
				user.setUsername("admin");
				user.setEmail("admin@correo.cl");
				user.setRut("20300952");
				user.setAddress("Direccion falsa");
				user.setIdentificator("6");
				user.setPassword("admin");
				user.setAdmin(true);
				userService.save(user);
				System.out.println("Usuario maestro insertado correctamente.");
			}
			private void insertarRoles(){
				roleService.save("ROLE_ADMIN");
				roleService.save("ROLE_USER");
				System.out.println("Roles insertados");
			}
	}

}
