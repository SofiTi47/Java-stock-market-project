package servlets;

import Engine.EngineServlet;
import RizpaDTO.DTOStocks;
import RizpaDTO.DTOTransaction;
import RizpaDTO.DTOUser;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TransactionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String symbolFromParameter = request.getParameter("symbol");
        int amountFromParameter = Integer.parseInt(request.getParameter("amount"));
        int rateFromParameter = Integer.parseInt(request.getParameter("rate"));
        String bsFromParameter = request.getParameter("bs");
        String actionFromParameter = request.getParameter("action");
        String userNameFromSession = SessionUtils.getUsername(request);
        EngineServlet engineServlet = ServletUtils.getEngineServlet(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        List<DTOTransaction> transactionList = engineServlet.createTransaction(symbolFromParameter
                ,amountFromParameter,rateFromParameter,userNameFromSession,actionFromParameter,bsFromParameter);
        String res = appendCreatedTransactions(transactionList);
        userManager.getUser(userNameFromSession).getUserAlerts().add(res);
        if(!transactionList.get(0).isCompleted())
            userManager.getUser(transactionList.get(0).getExecutorName()).getUserAlerts()
            .add("Transaction completed!"+transactionList.get(0).toString());
        response.setStatus(200);
    }
    private String appendCreatedTransactions(List<DTOTransaction> createdTransactions)
    {
        Iterator<DTOTransaction> itr = createdTransactions.iterator();
        DTOTransaction transaction = itr.next(); //the original request always in the list
        StringBuilder res = new StringBuilder();
        int i = 1;
        if(transaction.isCompleted() && !itr.hasNext())
            res.append("Transaction pending!");
        else if(transaction.isCompleted())
            res.append("Transaction not fully completed!");
        else
            res.append("Transaction completed!");
        res.append(transaction.toString()).append("\n");
        if(itr.hasNext())
            res.append("Completed transactions:").append("\n");
        while(itr.hasNext()) {
            if(i == createdTransactions.size()-1 && transaction.isCompleted())
                res.append("Remaining transaction:\n").append(itr.next().getBasicData());
            else {
                i++;
                res.append(itr.next().toString()).append("\n");
            }
        }
        return res.toString();
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
        return "Short description";
    }// </editor-fold>
}

