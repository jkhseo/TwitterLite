 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterserver;

import java.util.ArrayList;
/**
 *
 * @author kyunseo17
 */
public class User {
    private String username;
    private String password;
    private UserList followers;
    private UserList following;
    private Boolean online;
    private PublicTweetList readPublicTweets;
    private PublicTweetList unReadPublicTweets;
    private PublicTweetList myTweets;
    private String ipAddress;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password; 
        followers = new UserList();
        following = new UserList();
        unReadPublicTweets = new PublicTweetList();
        readPublicTweets = new PublicTweetList();
        myTweets = new PublicTweetList();
        online = false;
       
        
    }
    
    public Boolean updateIp(String ip) {
        this.ipAddress = ip;
        return true;
    }
    
    public String getIp() {
        String ip = this.ipAddress;
        return ip;
    }
    
    public Boolean updateFollowing(String username, User u) {
        following.addUser(username, u);
        return true;
    }
    
    public Boolean updateFollower(String username, User u) {
        followers.addUser(username, u);
        return true;
    }
    
    public Boolean updatePrivateMessages() {
        return true;
    }
    
    public Boolean updateOnline(Boolean state) {
        if (state == true) { 
            online = true; 
        }
        else { 
            online = false;
        }
        return true;
    }
    
    public Boolean getOnline() {
        return online;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public UserList getListofFollowers() {
        return followers;
    }
    
    public UserList getListofFollowing() { 
        return following;
    }
    
    public ArrayList<String> getListofOnlineFollowersIp() {
        ArrayList<String> ou = new ArrayList();
        UserList ul = getListofFollowers ();
        String[] aul = ul.getArrayofUsers();
        for (String u : aul) {
            User user = ul.getUser(u);
            if (user.getOnline() == true) {
                ou.add(user.getIp());
            }
        }
        
        return ou;
    }
    
    public PublicTweetList getListofUnreadPublicTweets() { 
        return unReadPublicTweets;
    }
    
    public PublicTweetList getListofReadPublicTweets() { 
        return readPublicTweets;
    }
        
    public PublicTweetList getMyTweets() {
        return myTweets;
    }
}
