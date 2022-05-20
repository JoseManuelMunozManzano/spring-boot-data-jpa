package com.jmunoz.springboot.app.models.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

// Servicio para manejar el tema de subida/borrado de ficheros
public interface IUploadFileService {

    // El recurso se guarda en el ResponseEntity, en el método handle del controlador
    Resource load(String filename) throws MalformedURLException;

    // Nombre único de la imagen
    String copy(MultipartFile file) throws IOException;

    boolean delete(String filename);
}
