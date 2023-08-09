package utils;

import constants.Constants;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    public static boolean getUserType(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("admin") : null;
        if (sessionAttribute != null){
            if (sessionAttribute.toString().equals("on"))
                return true;
        }
        return false;
    }
    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}