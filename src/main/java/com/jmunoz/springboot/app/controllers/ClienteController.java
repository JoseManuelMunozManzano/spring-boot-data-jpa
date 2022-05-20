package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.entity.Cliente;
import com.jmunoz.springboot.app.models.service.IClienteService;
import com.jmunoz.springboot.app.util.paginator.PageRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";

    // Para ver la foto
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

        Cliente cliente = clienteService.findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Detalle cliente: " + cliente.getNombre());

        return "ver";
    }

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);

        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();

        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    // Otra forma de cargar imágenes de forma programática, a través de código Java usando el recurso
    // InputStreamResource. Es un stream de entrada que contiene toda la información de la imagen en bytes.
    // No usa la clase de configuración MvcConfig, por lo que se borra.
    // Se usa un método handler en el controlador que se encarga de recibir la imagen como parámetro,
    // la convierte a un recurso y la carga en la respuesta HTTP como un ResponseEntity.
    //
    // En la ruta irá el nombre del archivo.
    // Esto se corresponde a lo que se ve en ver.html, th:src="@{'/uploads/' + ${cliente.foto}}"
    // La expresión regular :.+ evita que Spring borre la extensión del archivo
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        // Convertimos la imagen a un path absoluto para poder cargarla
        // resolve ya incluye el /
        Path pathFoto = Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
        log.info("pathFoto: " + pathFoto);

        Resource recurso = null;
        try {
            // Se carga la imagen
            recurso = new UrlResource(pathFoto.toUri());
            if (!recurso.exists() || !recurso.isReadable()) {
                throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Devolvemos el archivo en la respuesta HTTP. Se anexa al body
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    // Esta parte sigue quedando como esta porque es la parte de guardar en la ruta.
    // La parte nueva es la de cargar.
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if (!foto.isEmpty()) {
            // Se elimina la antigua foto y se guarda la nueva
            if (cliente.getId() != null
                    && cliente.getId() > 0
                    && cliente.getFoto() != null
                    && cliente.getFoto().length() > 0) {
                // Para borrar la imagen primero obtenemos su ruta absoluta, de él obtenemos el archivo
                // y si existe y se puede leer se pregunta si se eliminó y se manda un mensaje flash
                Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
                File archivo = rootPath.toFile();
                if (archivo.exists() && archivo.canRead()) {
                    archivo.delete();
                }
            }

            String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

            Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);
            Path rootAbsolutePath = rootPath.toAbsolutePath();

            log.info("rootPath: " + rootPath);
            log.info("rootAbsolutePath: " + rootAbsolutePath);

            try {
                Files.copy(foto.getInputStream(), rootAbsolutePath);

                flash.addFlashAttribute("info", "Has subido correctamente '" +
                        uniqueFilename + "'");

                cliente.setFoto(uniqueFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";

        clienteService.save(cliente);
        status.setComplete();
        // Usamos el método addFlashAttribute para mandar el mensaje flash
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito!");

            // Para borrar la imagen primero obtenemos su ruta absoluta, de él obtenemos el archivo
            // y si existe y se puede leer se pregunta si se eliminó y se manda un mensaje flash
            Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
            File archivo = rootPath.toFile();
            if (archivo.exists() && archivo.canRead()) {
                if (archivo.delete()) {
                    flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito!");
                }
            }
        }

        return "redirect:/listar";
    }
}
