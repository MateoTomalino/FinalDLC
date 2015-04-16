package servlet;

import finaldlc.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BuscadorController extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String consulta = request.getParameter("consulta");
        String html = "";
        String paginado = "";
        int pagina_int = 0;
        
        if (consulta != null && consulta.compareTo("") != 0) {
            Buscador.getInstance().obtenerResultados(consulta);
            agregarTagCloud(consulta);
            html = mostrarResultados(1);
            paginado = obtenerPaginado();
            if (html.indexOf("<li>") == -1) {
                html = "<div class='alert alert-danger'><strong>Lo siento!</strong> Su búsqueda no fue encontrada.</div>";
                paginado = "";
            }
            request.setAttribute("resultados", html);
            request.setAttribute("paginado", paginado);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            String pagina = request.getParameter("pagina");
            pagina_int = Integer.parseInt(pagina);
            html = mostrarResultados(pagina_int);
            paginado = obtenerPaginado();
            out.print(html);
            out.print(paginado);
        }
    }
    
    @Override
    public void init() {
        TagCloud.getInstance();
        Indice.getInstance();
        Vocabulario.getInstance();
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

    private String mostrarResultados(int numeroPagina) {
        StringBuilder builder = new StringBuilder();
        builder.append("<ul>");
        String[] nombreArchivo = null;
        LinkedList<ItemResultado> lista = Buscador.getInstance().obtenerPagina(numeroPagina);
        ItemResultado temp = null;
        int downloadPage = (numeroPagina - 1) * 8;
        for (int i = 0; i < lista.size(); i++) {
            temp = lista.get(i);
            builder.append("<li>");
            nombreArchivo = temp.getRutaDocumento().split("/");
            builder.append("<h5><button type='button' class='btn btn-default' href='http://localhost:8080/oven/DownloadController?id=" + (downloadPage + i) + "' ><span class='glyphicon glyphicon-import'></span></button><b class='fileName'>" + nombreArchivo[nombreArchivo.length-1] + "</b></h5>");
            builder.append("<p><b>Ubicacion:</b>" + temp.getRutaDocumento() + "</p>");
            builder.append("<p><b>Ocurrencia:</b>" + temp.getOcurrencia() + "</p>");
            builder.append("</li>");
        }
        builder.append("</ul>");
        return builder.toString();
    }

    private String obtenerPaginado() {
        StringBuilder builder = new StringBuilder();
        
        int pag = Buscador.getInstance().totalPaginas();

        builder.append("<div class=\"paginate\"> <ul class=\"pagination\">");
        builder.append("<li id ='irPrimero'><a  onclick='load(" + 1 + ");'><<</a></li>");

        for (int i = 1; i <= pag; i++) {
            if (i == 1) {
                builder.append("<li class='primero nodo' onclick='load(" + i + ");'><a>" + i + "</a></li>");
            } else if ( i == pag) {
                builder.append("<li class='ultimo nodo' onclick='load(" + i + ");'><a>" + i + "</a></li>");
            } else {
                builder.append("<li class='medio nodo' onclick='load(" + i + ");'><a>" + i + "</a></li>");
            }
        }
        builder.append("<li id='irUltimo' onclick='load(" + pag + ");'><a>>></a></li>");
        builder.append("</ul>");
        builder.append("</div>");

        return builder.toString();
    }

    private void agregarTagCloud(String consulta) {
        
        String[] palabras = Util.getInstance().limpiarPalabra(consulta).split(" ");
        for (int i = 0; i < palabras.length; i++) {
            if (!Util.getInstance().esPalabraBasura(palabras[i])) {
                TagCloud.getInstance().agregar(palabras[i]);
            }
        }
        TagCloud.getInstance().guardar();
    }
}