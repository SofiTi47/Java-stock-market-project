package servlets;
//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import Engine.EngineServlet;
import RizpaEngine.FileException;
import RizpaEngine.RizpaEngine;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Scanner;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ReadXMLServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="fileINP">
        //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();
        EngineServlet engineServlet = ServletUtils.getEngineServlet(getServletContext());
        RizpaEngine engine = engineServlet.getEngine();
        String usernameFromSession = SessionUtils.getUsername(request);
        boolean success = true;
        if (usernameFromSession == null){
            response.sendRedirect("../../index.html");
        }
        try {
            engine.loadDataFromFile(fileContent,usernameFromSession);
        }
        catch (FileException e){
            response.setStatus(409);
            response.getOutputStream().println(e.getMessage());
            success = false;
        }
        catch (Exception e) {
            response.setStatus(500);
            response.getOutputStream().println(e.getMessage());
            success = false;
        }
        if(success) {
            response.setStatus(200);
            response.getOutputStream().println("File loaded successfully");
        }
        response.sendRedirect("../pages/homepages/brokerHomePage.html");
    }
}
