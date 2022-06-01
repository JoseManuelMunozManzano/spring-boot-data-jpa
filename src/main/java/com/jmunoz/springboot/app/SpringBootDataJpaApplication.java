package com.jmunoz.springboot.app;

import com.jmunoz.springboot.app.models.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Se implementa la interfaz CommandLineRunner para que se cree automáticamente el
// directorio uploads, contenedor de imágenes
// Se ha borrado físicamente el directorio uploads, ya que ahora la app lo crea.
// Esto para testing está muy bien, ya que cada vez que se cierre la app desaparece el directorio y
// cuando se inicie (o se lancen las pruebas) se genera.
//
// Ver Interface IUploadFileService, donde se añaden un par de métodos

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	IUploadFileService uploadFileService;

	// Aquí también usamos BCryptPasswordEncoder
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	// Ejecuta lo primero de to-do. Sirve para hacer configuraciones, en este caso del directorio de imágenes
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();

		// Generamos nuestras contraseñas de ejemplo, ya usando JDBC
		// Vamos a generar 2 password encriptados, uno para admin y otro para jmunoz
		// La ventaja de bcrypt es que podemos usar varias encriptaciones para un mismo password.
		// La encriptación generada va a ser distinta, lo que lo hace muy seguro y robusto.
		String password = "1234";
		for (int i=0; i<2; i++) {
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
	}
}
