package com.jmunoz.springboot.app.view.xlsx;

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

    }
}
