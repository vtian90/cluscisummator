/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Akbar
 */
public class Helper {

    public static String uploadPaper(HttpServletRequest request) {
        String result = null;
        
        try {
            // Get absolute path
            String folder = request.getServletContext().getRealPath("/data/paper");

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
                        String linkCSS = "" + request.getContextPath();
                        String metadata = "\n<?xml-stylesheet type='text/css' href='" + linkCSS + "/css/paper.css'?>";
                        byte[] metadataBytes = metadata.getBytes();
                        byte[] fileBytes = fileItem.get();
                        char before = '?';
                        char end = '>';
                        byte[] allBytes = new byte[fileBytes.length + metadataBytes.length];

                        boolean found = true;
                        int i = 0;
                        while (found) {
                            byte beforeThisByte;
                            if (i != 0) {
                                beforeThisByte = fileBytes[i - 1];
                            } else {
                                beforeThisByte = 'a';
                            }

                            byte thisByte = fileBytes[i];
                            System.out.print((char) thisByte);
                            if (!(thisByte == end && beforeThisByte == before)) {
                                allBytes[i] = thisByte;
                                ++i;
                            } else {
                                allBytes[i] = thisByte;
                                found = false;
                            }
                        }

                        System.arraycopy(metadataBytes, 0, allBytes, i + 1, metadataBytes.length);
                        System.arraycopy(fileBytes, i + 1, allBytes, i + metadataBytes.length + 1, fileBytes.length - i - 1);

                        fileOutputStream.write(allBytes);
                        fileOutputStream.close();
                        result = folder + fileName;
                    }
                }
            }
        } catch (FileUploadException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
}
