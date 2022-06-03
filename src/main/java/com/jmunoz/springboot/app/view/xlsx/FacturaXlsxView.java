package com.jmunoz.springboot.app.view.xlsx;

import com.jmunoz.springboot.app.models.entity.Factura;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// Esta vista se genera igual que la vista FacturaPdfView
// Pero no podemos tener dos vistas que tegan anotado el mismo nombre, ya que este debe ser único.
// El nombre factura/ver ya está asociado a la vista FacturaPdfView, así que, a pesar de que ambos hagan referencia
// al nombre de la vista, tenemos que marcar alguna diferencia entre uno y otro.
// De ahí que la nombremos factura/ver.xlsx
// La idea es colocar la extensión que tenemos configurado en el application.properties
// spring.mvc.contentnegotiation.media-types.xlsx

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    // En vez de obtener el document de pdf, ahora obtenemos la planilla de cálculo (workbook)
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura = (Factura) model.get("factura");

        Sheet sheet = workbook.createSheet("Factura Spring");

        // Primera forma de informar los datos en el Excel es a través de los objetos row y cell
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Datos del Cliente");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        // Segunda forma de informar los datos en el Excel más sencilla y directa sería encadenando métodos
        sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripción: " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());
    }
}
