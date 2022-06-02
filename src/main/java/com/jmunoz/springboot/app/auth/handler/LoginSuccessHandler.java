package com.jmunoz.springboot.app.auth.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    // NOTA IMPORTANTE: FilterChain hay que quitarlo del método onAuthenticationSuccess, porque si no no funciona.
    // En esta dirección explica un poco qué significa
    // https://www.oracle.com/java/technologies/filters.html
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        FlashMap flashMap = new FlashMap();

        // En este caso no se puede pasar el Locale como argumento del método
        // Lo obtenemos a través del LocaleResolver y se le pasa el request
        Locale locale = localeResolver.resolveLocale(request);
        String mensaje = String.format(messageSource.getMessage("text.login.success", null, locale), authentication.getName());

        flashMap.put("success", mensaje);

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        // Se puede usar logger directamente porque SimpleUrlAuthenticationSuccessHandler, que hereda de la clase
        // abstracta AbstractAuthenticationTargetUrlRequestHandler, ya lo tiene declarado
        if (authentication != null) {
            logger.info("El usuario '" + authentication.getName() + "' ha iniciado sesión con éxito");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
