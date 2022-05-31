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

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // NOTA IMPORTANTE: FilterChain hay que quitarlo del método onAuthenticationSuccess, porque si no no funciona.
    // En esta dirección explica un poco qué significa
    // https://www.oracle.com/java/technologies/filters.html
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        FlashMap flashMap = new FlashMap();

        // Obteniendo el usuario autenticado en el contrador
        flashMap.put("success", "Hola, " + authentication.getName() + " has iniciado sesión con éxito!");

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        // Se puede usar logger directamente porque SimpleUrlAuthenticationSuccessHandler, que hereda de la clase
        // abstracta AbstractAuthenticationTargetUrlRequestHandler, ya lo tiene declarado
        if (authentication != null) {
            logger.info("El usuario '" + authentication.getName() + "' ha iniciado sesión con éxito");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
