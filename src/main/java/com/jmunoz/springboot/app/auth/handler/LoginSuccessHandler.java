package com.jmunoz.springboot.app.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Para personalizar mensaje cuando hay un login exitoso
// Registramos la clase como un bean de Spring, es decir, se guarda en el contenedor dentro del contexto de Spring
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // NOTA IMPORTANTE: FilterChain hay que quitarlo del método onAuthenticationSuccess, porque si no no funciona.
    // En esta dirección explica un poco qué significa
    // https://www.oracle.com/java/technologies/filters.html
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Registrar un flashMap, es decir, un administrador de Map que contenga los mensajes Flash, en este caso el success
        // Aquí no se puede inyectar como argumento en el método el RedirectAttributes (como hacemos en el controlador)
        // y por eso se hace de esta forma, pero es lo mismo.
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        FlashMap flashMap = new FlashMap();
        flashMap.put("success", "Ha iniciado sesión con éxito!");

        // Se guarda en nuestro administrador de FlashMap los flashMaps que hemos informado, junto al request y
        // al response
        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        // Quitar también de aquí chain
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
