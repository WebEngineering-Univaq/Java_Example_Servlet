package it.univaq.f4i.iw.examples;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe Della Penna
 */
public class Salutami extends HttpServlet {

    private LocalDateTime startup;

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        String message = "Unknown error";
        //assumiamo che l'eccezione sia passata tramite gli attributi della request
        //ma per sicurezza controlliamo comunque il tipo effettivo dell'oggetto
        //we assume that the exception has been passed using the request attributes        
        //but we always check the real object type
        if (request.getAttribute("exception") instanceof Exception) {
            Exception exception = (Exception) request.getAttribute("exception");
            if (exception != null && exception.getMessage() != null && !exception.getMessage().isEmpty()) {
                message = exception.getMessage();
            }
        }
        //Scriviamo il messaggio di errore nel log del server
        //Log the error message in the server log
        System.err.println(message);
        // ATTENZIONE: in un ambiente di produzione, i messaggi di errore DEVONO essere limitati a informazioni generiche, non a stringhe di complete di eccezione
        //e.g., potremmo mappare solo la classe dell'eccezione (IOException, SQLException, ecc.) in messaggi come "Errore IO", "Errore database", ecc.
        //WARNING: in a production environment, error messages MUST be limited to generic information, not full exception strings
        //e.g., we may map the exception class only (IOException, SQLException, etc.) to messages like "IO Error", "Database Error", etc.
        try {
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            HTMLHelpers.printPageHeader(out, "ERROR");
            out.println("<p>" + message + "</p>");
            HTMLHelpers.printPageFooter(out);
        } catch (IOException ex) {
            //if error page cannot be sent, try a standard HTTP error message
            //se non possiamo inviare la pagina di errore, proviamo un messaggio di errore HTTP standard
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            } catch (IOException ex1) {
                //if ALSO this error status cannot be notified, write to the server log
                //se ANCHE questo stato di errore non può essere notificato, scriviamo sul log del server
                Logger.getLogger(Salutami.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void action_saluta_noto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nome = request.getParameter("n");
        //qualche controllo di sicurezza (ridondante)
        //some (redundant) security check
        if (nome == null || nome.isEmpty()) {
            nome = "Unknown";
        } else {
            //qui dovremmo "sanitizzare" il parametro            
            //ad esempio con https://github.com/OWASP/java-html-sanitizer
            //ma usiamo il nostro sanitizzatore "di base" direttamente in fase di output
            //here we should "sanitize" the parameter
            //for example using https://github.com/OWASP/java-html-sanitizer
            //but we use our "basic" sanitizer directly in the output statement        
        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HTMLHelpers.printPageHeader(out, "Salutami!");
            out.println("<p>Hello, " + HTMLHelpers.sanitizeHTMLOutput(nome) + "!</p>");
            HTMLHelpers.printPageFooter(out);
        }
    }

    private void action_saluta_anonimo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HTMLHelpers.printPageHeader(out, "Salutami!");
            out.println("<p>Hello!</p>");
            out.println("<form method=\"get\" action=\"salutami\">");
            out.println("<p>What is your name?");
            out.println("<input type=\"text\" name=\"n\"/>");
            out.println("<input type=\"submit\" name=\"s\" value=\"Hello!\"/>");
            out.println("</p>");
            out.println("</form>");
            out.println("<p><small>Current timestamp is " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</small></p>");
            out.println("<p><small>I'm greeting all users since " + startup.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</small></p>");
            HTMLHelpers.printPageFooter(out);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            String n = request.getParameter("n");
            if (n == null || n.isEmpty()) {
                action_saluta_anonimo(request, response);
            } else {
                action_saluta_noto(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        startup = LocalDateTime.now();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
        return "A kind servlet";

    }// </editor-fold>
}
