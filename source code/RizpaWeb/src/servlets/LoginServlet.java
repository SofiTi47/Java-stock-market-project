package servlets;
import Engine.EngineServlet;
import Users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet{
    private final String BROKER_URL = "pages/homepages/brokerHomePage.html";
    private final String ADMIN_URL = "pages/homepages/adminHomePage.html";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        EngineServlet engineServlet = ServletUtils.getEngineServlet(getServletContext());
        if (usernameFromSession == null) { //user is not logged in yet
            String usernameFromParameter = request.getParameter("username");
            String check = request.getParameter("admin");
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(409);

                // returns answer to the browser to go back to the sign up URL page
                response.getOutputStream().println("Enter Username");
            }
            else {
                //normalize the username value
                String name = usernameFromParameter;
                String role = "Broker";
                usernameFromParameter = usernameFromParameter.trim();
                boolean adminFromParameter = false;
                if (check != null && check.equalsIgnoreCase("on")){
                    adminFromParameter = true;
                    role = "Admin";
                }
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(401);
                        response.getOutputStream().println(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter,role);
                        engineServlet.addUser(name,adminFromParameter);
                        request.getSession(true).setAttribute("username", usernameFromParameter);
                        request.getSession().setAttribute("admin",check);    //'on' for admin
                        response.setStatus(200);
                        if(adminFromParameter){
                            response.getOutputStream().println(ADMIN_URL);
                        }
                        else{
                            response.getOutputStream().println(BROKER_URL);

                        }

                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(200);
            boolean adminFromSession = SessionUtils.getUserType(request);
            if (adminFromSession){
                response.getOutputStream().println(ADMIN_URL);
            }
            else{
                response.getOutputStream().println(BROKER_URL);
            }

        }
    }
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


