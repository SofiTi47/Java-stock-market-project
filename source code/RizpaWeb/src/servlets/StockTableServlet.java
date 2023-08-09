package servlets;

import Engine.EngineServlet;
import RizpaDTO.DTOStock;
import RizpaDTO.DTOStocks;
import RizpaEngine.RizpaEngine;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import utils.ServletUtils;
import com.google.gson.Gson;
import Users.UserManager;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StockTableServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            EngineServlet engineServlet = ServletUtils.getEngineServlet(getServletContext());
            DTOStocks stockTable = engineServlet.getStock();
            List<DTOStock> temp = new LinkedList<>(stockTable.getStockMap().values());
            String json = gson.toJson(temp);
            out.println(json);
            out.flush();
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
        //get the stock and send to the engine to add plus add to the curr user holdings the stock
        String usernameFromSession = SessionUtils.getUsername(request);
        String symbolFromParameter = request.getParameter("symbol");
        String companyNameFromParameter = request.getParameter("companyName");
        int amountFromParameter = Integer.parseInt(request.getParameter("stockAmount"));
        int companyRateFromParameter = Integer.parseInt(request.getParameter("companyRate"));
        EngineServlet engineServlet = ServletUtils.getEngineServlet(getServletContext());
        if(engineServlet.addStock(usernameFromSession,symbolFromParameter,companyNameFromParameter
        ,amountFromParameter,companyRateFromParameter/amountFromParameter))
            response.setStatus(200);
        else
            response.setStatus(409);

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
