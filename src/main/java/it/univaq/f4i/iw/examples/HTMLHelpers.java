package it.univaq.f4i.iw.examples;

import java.io.PrintWriter;

/**
 *
 * @author IngegneriaDelWeb
 */
public class HTMLHelpers {

    public static void printPageHeader_XHTML(PrintWriter out, String title) {
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head><title>" + title + "</title></head><body>");
    }

    public static void printPageHeader_HTML5(PrintWriter out, String title) {
        out.println("<!doctype html>"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head><meta charset=\"UTF-8\"/><title>" + title + "</title></head><body>");
    }

    public static void printPageHeader(PrintWriter out, String title) {
        printPageHeader_HTML5(out, title);
    }

    public static void printPageFooter(PrintWriter out) {
        out.println("</body></html>");
    }

    public static String sanitizeHTMLOutput(String s) {
        return s.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll("<", "&gt;")
                .replaceAll("'", "&#039;")
                .replaceAll("\"", "&#034;");
    }
}
