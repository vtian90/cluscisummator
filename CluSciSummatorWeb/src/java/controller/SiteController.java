/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import core.sentenceselection.CluSciSummator;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 *
 * @author Akbar
 */
@WebServlet(name = "SiteController", urlPatterns = {"/main","/result", "/upload", "/clear_papers", "/about"})
public class SiteController extends system.Controller {
    
    private CluSciSummator _summarizer;
    private ArrayList<String> _listOfPapersUploadedURI = new ArrayList<String>();
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
        if (userPath.equals("/main")) {
            getAllFilesInFolder(request);
            setView(userPath, request, response);
        } else if (userPath.equals("/result")) {
            if (isGet) {
                processDocuments();
                setView(userPath, request, response);
            } else if (isPost) {
               summarize(request, response);
            } 
        } else if (userPath.equals("/upload")){
            if (isPost) {
                uploadPaper(request);
                getAllFilesInFolder(request);
                userPath = "/main";
                setView(userPath, request, response);
            }
        } else if(userPath.equals("/clear_papers")) {
            clearPapers();
            getAllFilesInFolder(request);
            userPath = "/main";
            setView(userPath, request, response);
        } else if(userPath.equals("/about")){
            userPath = "/about";
            setView(userPath, request, response);
        } else {
            getAllFilesInFolder(request);
            userPath = "/main";
            setView(userPath, request, response);
        }
    }
    
    private void processDocuments(){
        ArrayList<String> listPapersURI = getListUploadedPapers();
        _summarizer = new CluSciSummator(listPapersURI);
        _summarizer.processDocuments();
    }
    
    private void summarize(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String kategori_retorik = request.getParameter("kategori_retorik");
        String minimum_support = request.getParameter("minimum_support");
        String informasi_retorik = request.getParameter("informasi_retorik");

        if (!(kategori_retorik == null || minimum_support == null)) {
            String kategoriRetorik = request.getParameter("kategori_retorik");
            double minimumSupport = Double.parseDouble(request.getParameter("minimum_support"));
            int statusSummarization = -1;
            try {
                statusSummarization = _summarizer.summarize(kategoriRetorik, minimumSupport);
            } catch (Exception ex) {
                Logger.getLogger(SiteController.class.getName()).log(Level.SEVERE, null, ex);
            }

            Hashtable<String, String> errorSummaryMessage = new Hashtable<String, String>();

            String json = new Gson().toString();
            if (statusSummarization == 0)
                json = new Gson().toJson(_summarizer.summary);
            else if (statusSummarization == 1) {
                errorSummaryMessage.put("", "The value of minimum support is too small. Frequent term set should be contained by more than 1 document </br>");
                json = new Gson().toJson(errorSummaryMessage);
            } else if (statusSummarization == 2) {
                errorSummaryMessage.put("", "There's no sentence with this rhetorical status from all documents</br>");
                json = new Gson().toJson(errorSummaryMessage);
            } else if (statusSummarization == 3) {
                errorSummaryMessage.put("", "There's no concept collected from this rhetorical status</br>");
                json = new Gson().toJson(errorSummaryMessage);
            } else if (statusSummarization == 4) {
                errorSummaryMessage.put("", "There's no frequent term set identified by Aprori. It means that there's no common topic there</br>");
                json = new Gson().toJson(errorSummaryMessage);
            } else {
                errorSummaryMessage.put("", "Ini kasus belum ditangani");
                json = new Gson().toJson(errorSummaryMessage);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);                    
        } else if (informasi_retorik != null) {
            String information = getInformation(kategori_retorik);

            String json = new Gson().toJson(kategori_retorik.toUpperCase() + " : " + information);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
    
    /**
     * Mendapatkan semua list paper dari suatu folder
     * @param request 
     */
    private void getAllFilesInFolder(HttpServletRequest request) {
        ArrayList<String> listOfPapersURI = new ArrayList<String>();
        ArrayList<String> listOfPapersFileName = new ArrayList<String>();
        
        //Path folder data/paper:
        String folderPath = getServletContext().getRealPath("/data/paper");
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; ++i) {
            if (listOfFiles[i].getName().endsWith(".xml") || listOfFiles[i].getName().endsWith(".XML")) {
                listOfPapersFileName.add(listOfFiles[i].getName());
                listOfPapersURI.add(listOfFiles[i].getAbsolutePath());
            }
        }
        
        request.setAttribute("listOfPaperURI", listOfPapersURI);
        request.setAttribute("listOfPapersFileName", listOfPapersFileName);
    }
    
    private ArrayList<String> getListUploadedPapers(){
        ArrayList<String> result = new ArrayList<String>();
         //Path folder data/paper:
        String folderPath = getServletContext().getRealPath("/data/paper");
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; ++i) {
            if (listOfFiles[i].getName().endsWith(".xml") || listOfFiles[i].getName().endsWith(".XML")) {
                result.add(listOfFiles[i].getAbsolutePath());
            }
        }
        return result;
    }
    
    private void uploadPaper(HttpServletRequest request){
        try {
            // Get absolute path
            String folder = getServletContext().getRealPath("/data/paper");

            // Check multipart form
            boolean isMultiPart = ServletFileUpload.isMultipartContent(request);

            if (isMultiPart) {
                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());

                // Parse request
                List<FileItem> fileItems = upload.parseRequest(request);
                System.out.println("" + fileItems.size());

                // Process the upload items
                for (FileItem fileItem : fileItems) {
                    if (fileItem.getFieldName().equals("paper")) {
                        String fileName = fileItem.getName();
                        File file = new File(folder, fileName);

                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(fileItem.get());
                        fileOutputStream.close();
                        _listOfPapersUploadedURI.add(folder+fileName);
                    }
                }
            }
        } catch (FileUploadException ex) {
            Logger.getLogger(SiteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SiteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearPapers(){
        //Path folder data/paper:
        String folderPath = getServletContext().getRealPath("/data/paper");
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().endsWith(".xml") || file.getName().endsWith(".XML")) {
                file.delete();
            }
        }
        
        _listOfPapersUploadedURI.clear();
    }
    
    private String getInformation(String kategori_retorik) {
        if (kategori_retorik.equals("aim")) 
            return "Statement of speciﬁc research goal, or hypothesis of current paper";
        else if (kategori_retorik.equals("nov_adv")) 
            return "Novelty or advantage of own approach";
        else if (kategori_retorik.equals("co_gro")) 
            return "No knowledge claim is raised (or knowledge claim not signiﬁcant for the paper)";
        else if (kategori_retorik.equals("othr")) 
            return "Knowledge claim (signiﬁcant for paper)held by somebody else. Neutral description";
        else if (kategori_retorik.equals("prev_own")) 
            return "Knowledge claim (signiﬁcant) held by authors in a previous paper. Neutral description";
        else if (kategori_retorik.equals("own_mthd")) 
            return "New Knowledge claim, own work: methods";
        else if (kategori_retorik.equals("own_fail")) 
            return "A solution/method/experiment in the paper that did not work";
        else if (kategori_retorik.equals("own_res")) 
            return "Measurable/objective outcome of own work";
        else if (kategori_retorik.equals("own_conc")) 
            return "Findings, conclusions (non-measurable) of own work";
        else if (kategori_retorik.equals("codi")) 
            return "A Comparison, contrast, difference to other solution (neutral)";
        else if (kategori_retorik.equals("gap_weak")) 
            return "Lack of solution in ﬁeld, problem with other solutions";
        else if (kategori_retorik.equals("antisupp")) 
            return "Clash with somebody else’s results or theory; superiority of own work";
        else if (kategori_retorik.equals("support")) 
            return "Other work supports current work or is supported by current work";
        else if (kategori_retorik.equals("use")) 
            return "Other work is used in own work";
        else if (kategori_retorik.equals("fut")) 
            return "Statements/suggestions about future work (own or general)";
        else
            return "";
    }
}

//                ArrayList<String> papersURI = (ArrayList<String>) request.getAttribute("papersURI");
//                System.out.println(""+papersURI);
//                String URI1 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A92-1024(1).xml";
//                String URI2 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\A97-1049_FINAL_1.xml";
//                String URI3 = "D:\\Kuliah\\Semester VIII\\TA2\\Implementasi\\Dataset PAPER\\final200511\\C02-1144_FINAL_2.xml";
//                
//                ArrayList<String> URIS = new ArrayList<String>();
//                URIS.add(URI1);
//                URIS.add(URI2);
//                URIS.add(URI3);