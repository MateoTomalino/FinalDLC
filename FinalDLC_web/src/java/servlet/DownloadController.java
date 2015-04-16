package servlet;

import finaldlc.Buscador;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DownloadController", urlPatterns = {"/DownloadController"})
public class DownloadController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String descargar = request.getParameter("id");
        int descargar_int = Integer.parseInt(descargar);
        String archivo = Buscador.getInstance().obtenerRutaDoc(descargar_int);
        
        if (descargar != null) {
            File archivoDescargar = new File(archivo);
            FileInputStream fis = new FileInputStream(archivoDescargar);

            response.setContentLength((int) archivoDescargar.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + archivoDescargar.getName() + "\"");
            ServletOutputStream sos = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int leido = fis.read(buffer);
            while (leido != -1) {
                sos.write(buffer, 0, leido);
                leido = fis.read(buffer);
            }
            sos.flush();
            sos.close();
            fis.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
