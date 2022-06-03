package com.jmunoz.springboot.app.view.pdf;

import com.jmunoz.springboot.app.models.entity.Factura;
import com.jmunoz.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

// Notar que esta clase es una VISTA

// IMPORTANTE: La ruta estaba mal, se ha quitado el primer /
// Por qué el nombre factura/ver?
// Porque es el nombre que está retornando el método ver que esta mapeado a /factura/ver/{id},
// del controlador FacturaController
// La idea es exportar o convertir este contenido de la factura a PDF.
// En vez de que muestre la página en HTML, que muestre el documento en PDF.
//
// Lo que cambia es la representación, pero los datos son los mismos, el controlador es el mismo y el método es
// el mismo, ya sea para renderizar en pdf, en html, en json, xml, excel...
//
// Entonces, cómo resuelve si va a mostrar en pdf, o excel o html? A través del parámetro format.
// Cuando es PDF, internamente Spring va a asociar esta bandera a un content-type que corresponde a application/pdf.
// Si se omite ese parámetro, por defecto va a ser html.
// Es bastante automático. Simplemente pasamos el parámetro y si es pdf buscará la vista correspondiente y cambia
// el content-type en la respuesta para poder renderizar correctamente el documento pdf.
//
// Otra regla que tiene que tener cualquier vista con estructura de una clase, es implementar la interface View
// y a su vez implementar el método render() para poder renderizar el contenido.
// Pero en nuestro caso vamos a extender de una clase abstracta, AbstractPdfView, que hereda de AbstractView que a su
// vez implementa View.
//
// NOTA: Si quisiéramos implementar una vista web con soporte web, sería mejor extender directamente de AbstractView.

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    // Primera forma de añadir multilenguaje
    // Es lo mismo que hicimos en LoginSuccessHandler. Inyectamos MessageSource y LocaleResolver
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    // El model, en el controlador, guarda datos en la vista, usando el método addAttribute() y aquí los podemos obtener
    // document representa el documento pdf del API iText
    // writer es el escritor pdf
    // Y nuestro request y response
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Hay que hacer un cast, ya que se añade y recuperan objetos, tipo Object
        Factura factura = (Factura) model.get("factura");

        // Para multilenguaje. Primera forma
        Locale locale = localeResolver.resolveLocale(request);

        // Para multilenguaje. Segunda forma más sencilla heredada de la superclase
        // Con este objeto se puede traducir de forma directa, ya que por debajo maneja el Locale y trabaja por debajo
        // con el messageSource
        MessageSourceAccessor mensajes = getMessageSourceAccessor();

        // Se generan las tablas
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tabla.addCell(cell);

        tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla.addCell(factura.getCliente().getEmail());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        tabla2.addCell(cell);

        // Usando mensajes.getMessages. La segunda forma
        tabla2.addCell(mensajes.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
        tabla2.addCell(mensajes.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
        tabla2.addCell(mensajes.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());

        // Se guardan las tablas en el documento
        document.add(tabla);
        document.add(tabla2);

        // Detalle de la factura
        // 4 columnas: producto, precio, cantidad y total
        PdfPTable tabla3 = new PdfPTable(4);
        // Las medidas son relativas. La primera columna es 3.5 veces más grande que las otras 3 columnas, que son iguales.
        tabla3.setWidths(new float[] {3.5f, 1, 1, 1});

        tabla3.addCell(mensajes.getMessage("text.factura.form.item.nombre"));
        tabla3.addCell(mensajes.getMessage("text.factura.form.item.precio"));
        tabla3.addCell(mensajes.getMessage("text.factura.form.item.cantidad"));
        tabla3.addCell(mensajes.getMessage("text.factura.form.item.total"));

        for (ItemFactura item: factura.getItems()) {
            tabla3.addCell(item.getProducto().getNombre());
            tabla3.addCell(item.getProducto().getPrecio().toString());

            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tabla3.addCell(cell);

            tabla3.addCell(item.calcularImporte().toString());
        }

        // Footer con el texto Total formateado
        // Ocupa 3 columnas y se alinea a la derecha
        cell = new PdfPCell(new Phrase(mensajes.getMessage("text.factura.form.total") + ": "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        // La 4 columna con el total
        tabla3.addCell(factura.getTotal().toString());

        // Se guarda la tabla en el documento
        document.add(tabla3);
    }
}
