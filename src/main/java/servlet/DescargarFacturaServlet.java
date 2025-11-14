package servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelos.DetalleCarro;
import modelos.ItemCarro;
import services.LoginService;
import services.LoginServiceSessionImpl; // Necesitas esta clase para obtener el nombre
import java.util.Optional; // Necesitas esta clase para manejar el nombre

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * Servlet encargado de generar y enviar una factura de la compra actual
 * en formato PDF, utilizando la librería iText.
 * Mapeado a la ruta "/descargar-factura".
 */
@WebServlet("/descargar-factura")
public class DescargarFacturaServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para generar el documento PDF y enviarlo al cliente.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtener el carrito de la sesión
        DetalleCarro detalleCarro = (DetalleCarro) req.getSession().getAttribute("carro");

        // 1.1. Obtener el nombre del usuario logueado de la sesión
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);
        String nombreComprador = usernameOptional.orElse("Invitado"); // Si no está logueado, usar "Invitado"

        // 1.2. Verificación del carrito
        if (detalleCarro == null || detalleCarro.getItem().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/ver-carro");
            return;
        }

        // 2. Configurar la respuesta HTTP para PDF
        DecimalFormat df = new DecimalFormat("#,##0.00");

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=factura_" + LocalDate.now() + "_" + nombreComprador + ".pdf");
        // Nota: Agregué el nombre al nombre del archivo para mejor identificación.

        // 3. Generar el PDF
        Document document = new Document();

        try (OutputStream outputStream = resp.getOutputStream()) {

            resp.resetBuffer();

            // 3.1. Crear el escritor de PDF que apunta al flujo de salida de la respuesta.
            PdfWriter.getInstance(document, outputStream);

            // 3.2. Abrir el documento para comenzar a escribir contenido.
            document.open();

            // 3.3. Cálculos
            double total = detalleCarro.getTotal();
            double iva = detalleCarro.getIva();
            double totalConIva = detalleCarro.getTotalConIva();

            // 3.4. Contenido del PDF (Título, fecha, detalle de productos)
            document.add(new Paragraph("Factura de Compra"));
            document.add(new Paragraph("--------------------------------------------------"));

            // 🟢 AÑADIR EL NOMBRE DE LA PERSONA LOGUEADA
            document.add(new Paragraph("Comprador: " + nombreComprador));
            document.add(new Paragraph("Fecha de Emisión: " + LocalDate.now()));
            document.add(new Paragraph("--------------------------------------------------"));


            document.add(new Paragraph("Productos comprados:"));
            // Iterar sobre los ítems del carrito y agregar el detalle al PDF.
            for (ItemCarro item : detalleCarro.getItem()) {
                document.add(new Paragraph(item.getProducto().getNombre() +
                        " - Cantidad: " + item.getCantidad() +
                        " - Subtotal: " + df.format(item.getSubtotal())));
            }

            // 3.5. Resumen de Totales
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Total (Base): " + df.format(total)));
            document.add(new Paragraph("IVA (15%): " + df.format(iva)));
            document.add(new Paragraph("Total con IVA: " + df.format(totalConIva)));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Gracias por su compra!"));

            // 3.6. Cerrar el documento.
            document.close();

        } catch (DocumentException e) {
            throw new ServletException("Error al generar el documento PDF", e);
        }
    }
}