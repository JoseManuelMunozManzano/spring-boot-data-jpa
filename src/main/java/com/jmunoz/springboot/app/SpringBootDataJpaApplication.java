package com.jmunoz.springboot.app;

import com.jmunoz.springboot.app.models.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	// Ejecuta lo primero de to-do. Sirve para hacer configuraciones, en este caso del directorio de imágenes
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
	}
}
