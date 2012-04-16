/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import core.summarizer.CluSciSummator;
import java.util.ArrayList;

/**
 *
 * @author Akbar
 */
@WebServlet(name = "SiteController", urlPatterns = {"/about", "/main", "/result"})
public class SiteController extends system.Controller {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userPath = request.getServletPath();
        if (userPath.equals("/result")) {
            if (isGet) {
            } else if (isPost) {
                System.out.println("AHAHAHAHAHHHHHHHHHHHHHHHHH");
                String URI1 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A92-1024(1).xml";
                String URI2 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1049_FINAL_1.xml";
                String URI3 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\C02-1144_FINAL_2.xml";
                //        String URI4 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1053(1).xml";
                ArrayList<String> URIS = new ArrayList<String>();
                URIS.add(URI1);
                URIS.add(URI2);
                URIS.add(URI3);
                //        URIS.add(URI4);
                
                if (!(request.getParameter("kategori_retorik").equals("") || request.getParameter("minimum_support").equals(""))) {
                    String kategoriRetorik = request.getParameter("kategori_retorik");
                    double minimumSupport = Double.parseDouble(request.getParameter("minimum_support"));
                    System.out.println("AHAHAHAHAHHHHHHHHHHHHHHHHH");
                    CluSciSummator summarizer = new CluSciSummator(URIS, kategoriRetorik, minimumSupport);
                    try {
                        summarizer.summarize();
                        request.setAttribute("summarization", summarizer.summarization);
                        request.setAttribute("tes", "TES DOANG");
                        userPath = "/result";
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SiteController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(SiteController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        setView(userPath, request, response);
    }
}
