package RizpaDTO;

import RizpaDTO.DTOUser;
import RizpaEngine.User;
import RizpaEngine.Users;

import java.util.HashMap;
import java.util.Map;

public class DTOUsers {
    private Map<String, DTOUser> userMap;

    public DTOUsers(Users users){
        userMap = new HashMap<>();
        for(User user : users.getUserMap().values()){
            DTOUser DTOUser = new DTOUser(user);
            userMap.put(DTOUser.getName(),DTOUser);
        }
    }

    public Map<String, DTOUser> getUserMap() {
        return userMap;
    }
}
