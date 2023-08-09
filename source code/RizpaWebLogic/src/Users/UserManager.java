package Users;

import RizpaDTO.DTOUser;
import RizpaDTO.DTOUsers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final DTOUsers usersSet;

    public UserManager() {
        usersSet = new DTOUsers();
    }

    public synchronized void addUser(String username, String type) {

        if ("Admin".equals(type)) {
            usersSet.add(new DTOUser(username, true));
        }
        else
            usersSet.add(new DTOUser(username,false));
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        Set<String> strCollection = new HashSet<>();
        for(DTOUser user : usersSet.getUserMap().values()){
            strCollection.add(user.toSimpleString());
        }
        return Collections.unmodifiableSet(strCollection);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
    public DTOUser getUser(String username){
        return usersSet.getUserMap().get(username);
    }
}
