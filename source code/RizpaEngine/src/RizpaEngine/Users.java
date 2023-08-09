package RizpaEngine;

import src.JAXB.RseUser;
import src.JAXB.RseUsers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Users {
    private Map<String, User> userMap;

    public Users() {this.userMap = new HashMap<>();}
    public Users(RseUsers rseUsers, Stocks stocks) throws FileException {
        userMap = new HashMap<>();
        for(RseUser rseUser : rseUsers.getRseUser()){
            User user = new User(rseUser,stocks);
            checkDuplicates(user);
            userMap.put(user.getName(),user);
        }
    }
    public void addUser(User user){
        userMap.put(user.getName(),user);
    }
    private void checkDuplicates(User user) throws FileException {
        if(userMap.isEmpty())
            return;
        if(userMap.containsKey(user.getName()))
            throw new FileException("Double user found.", new IOException());
    }
    public User getUser(String userName){
        return userMap.get(userName);
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }
}
