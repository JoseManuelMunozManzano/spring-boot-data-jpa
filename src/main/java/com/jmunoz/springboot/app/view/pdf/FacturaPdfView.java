package com.jmunoz.springboot.app.view.pdf;

import com.jmunoz.springboot.app.models.entity.Factura;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// Notar que esta clase es una VISTA

// Por qué el nombre /factura/ver?
// Porque es el nombre que está retornando el método ver que esta mapeado a /factura/ver/{id},
// del controlador FacturaController
// La idea es exportar o convertir este contenido de la factura a PDF.
// En vez de que muestre la página en HTML, que muestre el documento en PDF.
//
// Lo que cambia es la representación, pero los datos son los mismos, el controlador es el mismo y el método es
// el mismo, ya sea para renderizar en pdf, en html, en json, xml, excel...
//
// Entonces, cómo resuelve si va a mostrar en pdf, o excel o html? A través del parámetro form.
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

@Component("/factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    // El model, en el controlador, guarda datos en la vista, usando el método addAttribute() y aquí los podemos obtener
    // document representa el documento pdf del API iText
    // writer es el escritor pdf
    // Y nuestro request y response
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Hay que hacer un cast, ya que se añade y recuperan objetos, tipo Object
        Factura factura = (Factura) model.get("factura");

        // Se generan las tablas
        PdfPTable tabla = new PdfPTable(1);
        tabla.addCell("Datos del Cliente");
        tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla.addCell(factura.getCliente().getEmail());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.addCell("Datos de la Factura");
        tabla2.addCell("Folio: " + factura.getId());
        tabla2.addCell("Descripción: " + factura.getDescripcion());
        tabla2.addCell("Fecha: " + factura.getCreateAt());

        // Se guardan las tablas en el documento
        document.add(tabla);
        document.add(tabla2);
    }
}
