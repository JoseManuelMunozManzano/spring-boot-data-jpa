package com.jmunoz.springboot.app.view.csv;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// Se va a generar un fichero CSV con la lista de clientes existentes
// Se nombra listar porque es la vista que se carga en el controlador ClienteController
// Como solo tenemos una sola clase con el nombre de componente listar, no hace falta ponerle la extensión .csv
// Estamos definiendo esta vista a través del nombre del Bean, BeanNameViewResolver
//
// No tenemos una vista de Spring para implementar un archivo plano. Tenemos que crearnos nuestra propia vista.
// Para eso implementamos una clase más abstracta, AbstractView
//
// Suponiendo que vamos a tener más vistas utilizando @Component a través del BeanNameViewResolver,
// se podría agregar la extensión .csv
// Pero ahora, para que funcione y encuentre esta vista, hay que agregar el contentnegotiation en el fichero de
// properties.
@Component("listar.csv")
public class ClienteCsvView extends AbstractView {

    // Asignamos el tipo de contenido (media types o MIME types) en el constructor
    public ClienteCsvView() {
        setContentType("text/csv");
    }

    // Como es un archivo que se descarga, tenemos que configurar este método, indicando que el contenido
    // si es descargable
    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    // Este método que tenemos que implementar es muy parecido al que teníamos en PDF (buildPdfDocument)
    // y Excel (buildExcelDocument)
    // Solo tenemos que envolver el contenido de nuestro archivo plano dentro del response.
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Asignamos un nombre al archivo plano
        response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");

        // Pasamos el content type a la respuesta. el getContentType() pertenece a la clase que estamos heredando
        response.setContentType(getContentType());

        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");

        // Para que se pueda descargar, guardamos el fichero en el writer de la respuesta
        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        // Nombres de columna
        // Se crea un arreglo de Strings que tenga los nombres de los campos de la clase que se va a convertir a CSV.
        // Para resumir, el beanWriter toma un Bean (como una clase Entity) como cliente y eso lo convierte en una
        // línea en el archivo plano.
        // Pero necesitamos saber que atributos o campos de esa clase Entity se va a incluir. Por eso tenemos que
        // tener el header del archivo plano.
        String[] header = {"id", "nombre", "apellido", "email", "createAt"};
        beanWriter.writeHeader(header);

        // for que itere en los clientes y se guarda cada objeto en el archivo plano
        for (Cliente cliente : clientes) {
            beanWriter.write(cliente, header);
        }

        beanWriter.close();
    }
}
