/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
/**
 *
 * @author kyunseo17
 */
public class UserList {
     private HashMap<String, User> UserList;
     
     public UserList() {
         UserList = new HashMap<String, User>(); 
     }
     public User getUser(String username){
         User u = UserList.get(username);
         return u;
     }
     
     public Boolean containsUser(String username) {
         if (UserList.containsKey(username)) { return true; }
         else { return false; }
         
     }
     
     public Boolean addUser(String username, User user) {
         UserList.put(username, user);
         return true;
     }
     
     public Boolean deleteUser(String username) {
         UserList.remove(username);
         return true;
     }
     
     public String[] getArrayofUsers() {
        String[] listOfUsers = new String[UserList.size()];
        int i = 0;
        for (String key: UserList.keySet()) {
            String tempname = key;
            listOfUsers[i++] = tempname;
        }
        return listOfUsers;
     }
     
     
             
        
}
