package servlets;

import Engine.EngineServlet;
import RizpaDTO.DTOStocks;
import RizpaEngine.Transaction;
import Users.UserManager;
import com.google.gson.Gson;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FundsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        int currencyFromParameter = Integer.parseInt(request.getParameter("funds"));
        EngineServlet engine = ServletUtils.getEngineServlet(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        if (usernameFromSession == null){
            response.sendRedirect("../../index.html");
        }
        else{
            Transaction transaction = engine.getEngine().addFunds(usernameFromSession,currencyFromParameter);
            String newFunds = userManager.getUser(usernameFromSession).addUserFunds(transaction);
            response.setStatus(200);
            response.getOutputStream().println(newFunds);
        }
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
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        response.getOutputStream().println(Integer.toString(userManager.getUser(usernameFromSession).getUserFunds()));

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
        return "Short description";
    }// </editor-fold>
}

