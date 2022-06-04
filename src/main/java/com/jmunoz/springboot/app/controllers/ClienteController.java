package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.entity.Cliente;
import com.jmunoz.springboot.app.models.service.IClienteService;
import com.jmunoz.springboot.app.models.service.IUploadFileService;
import com.jmunoz.springboot.app.util.paginator.PageRender;
import com.jmunoz.springboot.app.view.xml.CilenteList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

// Implementación de locale en los controladores

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    // A través de este bean se obtendrá el mensaje de message_xx.properties
    @Autowired
    private MessageSource messageSource;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash, Locale locale) {

        Cliente cliente = clienteService.fetchByIdWithFacturas(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
            return "redirect:/listar";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));

        return "ver";
    }

    // Tenemos que pasar como argumento el locale
    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, HttpServletRequest request,
                         Locale locale) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Hola usuario autenticado, tu username es: ".concat(auth.getName()));
        }

        if (hasRole("ROLE_ADMIN")) {
            logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
        if (securityContext.isUserInRole("ADMIN")) {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper. Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper. Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        if (request.isUserInRole("ROLE_ADMIN")) {
            logger.info("Forma usando HttpServletRequest. Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Forma usando HttpServletRequest. Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        // Uso de messageSource. Se indica el property, null, y el locale que se ha pasado como argumento
        model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);

        return "listar";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model, Locale locale) {
        Cliente cliente = new Cliente();

        model.put("cliente", cliente);
        model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
        return "form";
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
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

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));
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

            flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFilename + "'");

            cliente.setFoto(uniqueFilename);
        }

        String mensajeFlash = (cliente.getId() != null)
                ? messageSource.getMessage("text.cliente.flash.editar.success", null, locale)
                : messageSource.getMessage("text.cliente.flash.crear.success", null, locale);

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
        return "form";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash, Locale locale) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));

            if (uploadFileService.delete(cliente.getFoto())) {
                String mensajeFotoEliminar = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
                flash.addFlashAttribute("info", mensajeFotoEliminar);
            }
        }

        return "redirect:/listar";
    }

    private boolean hasRole(String role) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }

        Authentication auth = context.getAuthentication();
        if (auth == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return authorities.contains(new SimpleGrantedAuthority(role));
    }

    // Esta segunda forma de implementar un API Rest es automática y se basa en crear NO UNA CLASE VISTA, sino un método
    // handler del CONTROLADOR que se encarga únicamente de renderizar una vista del tipo REST, ya sea JSON o XML.
    //
    // En vez de retornar un String hay que devolver un objeto simple de Java (POJO) o un JavaBean que queramos
    // desplegar (mostrar) sus atributos en formato JSON o XML
    // Y no se nos olvide incluir @ResponseBody, que indica que la respuesta es de tipo REST.
    // También nos dice que el listado de clientes se va a almacenar en el body de la respuesta.
    // Al guardarse, de forma automática, Spring va a deducir que es un REST
    //
    // Todas las anotaciones @Json que se aplicaron en los entities también se aplican a esta forma de implementación,
    // ya que solo cambia la forma en la que se despliega la vista.
    // Otra diferencia es que el atributo clientes no nos aparece al principio del documento JSON, como si aparece
    // cuando se usa la implementación de clase vista, porque ese clientes corresponde al nombre que le dimos
    // en model.put("clientes", clientes.getContent()); y que contiene los clientes.
    // Aquí no tenemos nombre para la lista de clientes, solo se devuelve, de ahí que no aparezca ningún nombre en el
    // documento JSON. Si nos hiciera falta ese nombre, se podría colocar como argumento el Model y añadir ese
    // atributo.
    //
    // NOTA: Para probar JSON poner la ruta
    // http://localhost:8080/listar-rest
    //
    // Y esto también vale, en principio, para mostrar el XML, pero no olvidar que necesitamos una clase wrapper
    // y, por tanto, también debemos de cambiar la clase de retorno a ese wrapper.
    // Para probar XML poner la ruta
    // http://localhost:8080/listar-rest?format=xml
    //
    // Ahora nos funciona tanto para JSON como para XML
    // Pero si solamente nos hiciera falta JSON, no haría falta la clase wrapper.
    // Otra cosa importante es que, si tenemos implementado tanto JSON como XML, por defecto, poniendo la ruta
    // http://localhost:8080/listar-rest
    // ya sale XML
    // Si queremos ver el JSON, la ruta a informar es
    // http://localhost:8080/listar-rest?format=json
    @GetMapping(value = "/listar-rest")
    public @ResponseBody CilenteList listarRest() {

        return new CilenteList(clienteService.findAll());
    }

}
