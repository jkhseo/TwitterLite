/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterserver;

/**
 *
 * @author kyunseo17
 */
import java.util.HashMap;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
public class Menu {
    
    private UserList allUsers;
    private User currentUser;
    private UserList onlineUsers;
    private PublicTweetList allPublicTweets;
    
    public Menu(UserList allUsers, UserList onlineUsers, PublicTweetList ptl) {
        this.allUsers = allUsers; 
        this.onlineUsers = onlineUsers;
        this.allPublicTweets = ptl;
    }
    
    public String logOn(String username, String password, String ip) {
        Boolean b = allUsers.containsUser(username);
        if (b == false) return "NOUSER";
        User u = allUsers.getUser(username);
        if (u.getOnline() == true) {
            return "USERLOGGED";
        }
        else {
            String pw = u.getPassword();
            if (password.equals(pw)) {
                u.updateOnline(true);
                currentUser = u;
                onlineUsers.addUser(username, u);
                u.updateIp(ip);
                return "SUCCESS";
            }
            else {
                return "WRONGPASS";
            }
            
        }
    }
    
    public String logOut() {
        currentUser.updateOnline(false);
        onlineUsers.deleteUser(currentUser.getUsername());
        currentUser.updateIp(null);
        currentUser = null;
        return "Logout success";
        
    }
    
    public String postPublicTweet(String subject, String tweet) {
        if (currentUser != null) {
            PublicTweet pt = new PublicTweet(subject, tweet, currentUser.getUsername());
            allPublicTweets.addTweet(subject, pt);
            UserList followerList = currentUser.getListofFollowers();
            String[] fl = followerList.getArrayofUsers();
            PublicTweetList myPt = currentUser.getMyTweets();
            myPt.addTweet(subject, pt);
            for (int i = 0; i< fl.length ; i++){
            String tempname = fl[i];
            User tempuser = followerList.getUser(tempname);
            PublicTweetList ptl = tempuser.getListofUnreadPublicTweets();
            ptl.addTweet(subject, pt);
           
            }
            return "SUCCESS";
        }  
      
        return "No user logged on";  
    }
    

    public String[] retrieveUnreadTweets() {
        PublicTweetList unreadPTL = this.currentUser.getListofUnreadPublicTweets();
        String[] arrayofSubjectsandUsers = unreadPTL.getArrayofSubjectsandUsers();
        return arrayofSubjectsandUsers;
    }
    public String[] retrieveOwnTweets() {
        PublicTweetList myTweets = this.currentUser.getMyTweets();
        String[] arrayofSubjectsandUsers = myTweets.getArrayofSubjectsandUsers();
        return arrayofSubjectsandUsers;
    }
    
    public String[] retrieveOnlineUsers() {
        UserList tempUL = this.currentUser.getListofFollowers();
        String[] users = tempUL.getArrayofUsers();
        ArrayList<String> tempAL = new ArrayList();
        for (String user : users) {
            User u = tempUL.getUser(user);
            if (u.getOnline() == true) {
                tempAL.add(user);
            }
        }
        String[] onlineUsers = tempAL.toArray((new String[tempAL.size()]));
        return onlineUsers;
        
        
    }
    
    public ArrayList<String> readTweet(String subject, String username) {
        ArrayList<PublicTweet> tempAL = this.allPublicTweets.getTweet(subject);
        PublicTweet pt = new PublicTweet(null, null, null);
        for (PublicTweet tempPT : tempAL) {
            String tempUser = tempPT.getUser();
            if (tempUser.equals(username)) {
                pt = tempPT;
            }
        }
        String tweet = pt.getMessage();
        String date = pt.getDate();
        ArrayList<String> al = new ArrayList();
        al.add(tweet);
        al.add(date);
        return al;
    }
    public ArrayList<String> readUnreadTweet(String subject, String username) {
        ArrayList<PublicTweet> tempAL = this.allPublicTweets.getTweet(subject);
        PublicTweetList tempPTL = currentUser.getListofUnreadPublicTweets();
        PublicTweet pt = new PublicTweet(null, null, null);
        for (PublicTweet tempPT : tempAL) {
            String tempUser = tempPT.getUser();
            if (tempUser.equals(username)) {
                pt = tempPT;
                tempPTL.deleteTweet(tempPT);
            }
        }
        tempPTL = currentUser.getListofReadPublicTweets();
        tempPTL.addTweet(subject, pt);
        String tweet = pt.getMessage();
        String date = pt.getDate();
        ArrayList<String> al = new ArrayList();
        al.add(tweet);
        al.add(date);
        return al;
    }

    public String[] searchTweet(String subject) {
        ArrayList<PublicTweet> tempAl = this.allPublicTweets.getTweet(subject);
        if (tempAl == null){
            return null;
        }
        ArrayList<String> al = new ArrayList();
        for (PublicTweet pt : tempAl) {
            String user = pt.getUser();
            al.add(subject + " " + user);
        }
        String[] listOfTweets = al.toArray(new String[al.size()]);
        return listOfTweets;
    }

    
    public String followUser(String u) {
        if (currentUser == null) return "No user logged in";
        Boolean b = allUsers.containsUser(u);
        if (b == false) {
            return "DNE";
        }
        else {
            UserList templist = currentUser.getListofFollowing();
            Boolean following = templist.containsUser(u);
            User user = allUsers.getUser(u);
            if (following == true) {
                return "ALREADYFOLLOWING";  
            }
            else  {
                currentUser.updateFollowing(u, user);
                user.updateFollower(currentUser.getUsername(), currentUser);
                return "SUCCESS";                  
            }
        }

        
    }
    
    public boolean unfollowUser(String username) {
        UserList ul = currentUser.getListofFollowing();
        ul.deleteUser(username);
        return true;
        
    }
    
    
    
    
}
