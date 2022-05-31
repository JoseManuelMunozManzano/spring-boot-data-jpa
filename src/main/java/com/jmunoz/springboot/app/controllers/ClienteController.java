package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.entity.Cliente;
import com.jmunoz.springboot.app.models.service.IClienteService;
import com.jmunoz.springboot.app.models.service.IUploadFileService;
import com.jmunoz.springboot.app.util.paginator.PageRender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;


    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

        Cliente cliente = clienteService.fetchByIdWithFacturas(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Detalle cliente: " + cliente.getNombre());

        return "ver";
    }

    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Hola usuario autenticado, tu username es: ".concat(auth.getName()));
        }

        if (hasRole("ROLE_ADMIN")) {
            logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        // Otra forma más simple de chequear la autorización del role en nuestras clases controladoras
        // Esta clase de Spring Security envuelve el objeto HttpServletRequest (añadido como parámetro al método)
        // y nos permite validar el role.
        // Si revisamos la clase SecurityContextHolderAwareRequestWrapper y buscamos el método isUserInRole que
        // invoca el método isGranted, y revisamos este método, veremos que se implementa algo muy parecido a
        // nuestro método hasRole de abajo
        //
        // Se indica el prefijo y por eso en la segunda línea se indica solo ADMIN.
        // Si el prefijo fuera blancos, entonces en la segunda línea se indicaria ROLE_ADMIN
        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
        if (securityContext.isUserInRole("ADMIN")) {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper. Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper. Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

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

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if (!foto.isEmpty()) {
            if (cliente.getId() != null
                    && cliente.getId() > 0
                    && cliente.getFoto() != null
                    && cliente.getFoto().length() > 0) {
                uploadFileService.delete(cliente.getFoto());
            }

            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }

            flash.addFlashAttribute("info", "Has subido correctamente '" +
                    uniqueFilename + "'");

            cliente.setFoto(uniqueFilename);
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

            if (uploadFileService.delete(cliente.getFoto())) {
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito!");
            }
        }

        return "redirect:/listar";
    }

    // Obtener el role programáticamente y ver si cumple o no dicho role
    // NOTA: Por defecto SpringBoot maneja los roles con la clase SimpleGrantedAuthority que implementa la interface
    //       GrantedAuthority.
    // Podríamos crear nuestra propia clase role, heredando de SimpleGrantedAuthority o implementando GrantedAuthority
    private boolean hasRole(String role) {
        SecurityContext context = SecurityContextHolder.getContext();
        // No tiene acceso
        if (context == null) {
            return false;
        }

        Authentication auth = context.getAuthentication();
        // No tiene acceso
        if (auth == null) {
            return false;
        }

        // Cualquier clase Role o que representa un Role en nuestra app tiene que implementar esta interface.
        // Nos vale cualquier objeto que implemente la interface GrantedAuthority.
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        // Usando un for
        // La ventaja es que nos permite usar el logger
//        for (GrantedAuthority authority : authorities) {
//            if (role.equals(authority.getAuthority())) {
//                logger.info("Hola usuario ".concat(auth.getName()).concat(" tu role es ").concat(authority.getAuthority()));
//                return true;
//            }
//        }

//        return false;

        // Otra forma de validar buscando si contiene el role
        return authorities.contains(new SimpleGrantedAuthority(role));

    }
}
