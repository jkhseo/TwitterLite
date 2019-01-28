    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author kyunseo17
 */
public class ServerDriver {


    private UserList onlineUsers;
    private UserList allUsers;
    private PublicTweetList allPublicTweets;
    private MenuList allMenus;
    
    public ServerDriver() {
        if (onlineUsers == null) onlineUsers = new UserList();
        if (allUsers == null) allUsers = new UserList();
        if (allPublicTweets == null) allPublicTweets = new PublicTweetList();
        if (allMenus == null) allMenus = new MenuList();
    
    }
    public String registerAccount(String username, String password) {
        
        Boolean b = allUsers.containsUser(username);
        if (b == true) return "Fail";
        else {
        User u = new User(username, password);
        allUsers.addUser(username, u);
        return ("Success");
        }       
        
    }
    
    
            
    
    
    public static void main(String[] args) {
        ServerDriver sd = new ServerDriver();
        UserList au = sd.getAllUsers();
        UserList ou = sd.getOnlineUsers();
        PublicTweetList ptl = sd.getAllPublicTweets();
        MenuList ml = sd.getMenuList();
        sd.registerAccount("jason", "jason");
        sd.registerAccount("asdf", "asdf");
        String response;
                
        try {
            

	    // Create the server socket that will be used to accept
	    // incoming connections

	    ServerSocket listen = new ServerSocket( 2002 ); // Bind to any port

	    // Print the port so we can run a client that will connect to
	    // the server.

	    System.out.println( "Listening on port:  " +
				listen.getLocalPort() );
            System.out.println("Listening on address: " +
                    InetAddress.getLocalHost());

	    // Process clients forever...

	    while( true ) {

		// Wait for a client to connect

		Socket client = listen.accept();

		// use the socket to create IO streams
		PrintWriter out =
		    new PrintWriter( client.getOutputStream(), true );

                BufferedReader in =
                new BufferedReader(
                    new InputStreamReader( client.getInputStream() ) );

		// follow the protocol
                response = in.readLine();
                System.out.println(response);
                if (response.equals(("REGISTER"))) {
                    out.println("INFO?");
                    response = in.readLine();
                    System.out.println(response);
                    String[] tarray = response.split(" ");
                    String username = tarray[0];
                    String password = tarray[1];
                    String sof = sd.registerAccount(username, password);
                    if (sof.equals("Success")) {
                        out.println("SUCCESS");
                        System.out.println("Acccount made");
                    }
                    else if (sof.equals(("Fail"))) {
                        out.println("FAIL");
                        
                    }
                    
                }
                if (response.equals(("LOGON"))) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    System.out.println(response);
                    out.println("PASSWORD?");
                    String password = in.readLine();
                    System.out.println(username + " " + password);
                    out.println("IP?");
                    String ip = in.readLine();
                    System.out.println(ip);
                    Menu tempmenu = new Menu(au,ou,ptl);
                    
                    String sof = tempmenu.logOn(username, password, ip);
                    if (sof.equals(("NOUSER"))) {
                        out.println("NOUSER");
                        System.out.println("NO USER");
                    }
                    else if (sof.equals(("USERLOGGED"))){
                        out.println("USERLOGGED");
                    }
                    else if (sof.equals(("SUCCESS"))) {
                        out.println("SUCCESS");
                        System.out.println("SUCCESS");
                        ml.addMenu(username, tempmenu);
                    }
                    else if (sof.equals(("WRONGPASS"))){
                        System.out.println("NOT WORKING");
                        out.println("WRONGPASS"); 
                    }
                }
                if (response.equals(("LOGOFF"))) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempmenu = ml.getMenu(username);
                    tempmenu.logOut();
                    tempmenu = null;
                    out.println("SUCCESS");
                    
                }
                
                if (response.equals(("POSTTWEET"))) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempmenu = ml.getMenu(username);
                    out.println("TWEET?");
                    String tweet = in.readLine();
                    System.out.println(tweet);
                    out.println("SUBJECT?");
                    String subject = in.readLine();
                    System.out.println(subject);
                    String result = tempmenu.postPublicTweet(subject, tweet);
                    System.out.println(result);
                    if (result.equals("SUCCESS")) {
                        out.println("SUCCESS");
                    }
                    else {
                        out.println("FAIL");
                    }
                }
                if (response.equals("SEARCHUSER")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    out.println("SEARCHED?");
                    String searched = in.readLine();
                    if (au.containsUser(searched)) {
                        out.println("EXIST");
                        response = in.readLine();
                        if (response.equals("CHECKFOLLOWSTATUS")){
                            User tempu = au.getUser(username);
                            UserList tempul = tempu.getListofFollowing();
                            if (tempul.containsUser(searched)) {
                                out.println("ALREADYFOLLOWING");
                            }
                            else {
                            out.println("NOTFOLLOWING");

                            }
                        }
                
                    }
                    else {
                        out.println("DNE");
                    }
                }
                
                
                
                
                
                if (response.equals("FOLLOWUSER")) {
                    out.println("CURRENTUSER?");
                    String currentUser = in.readLine();
                    out.println("FOLLOWUSER?");
                    String followUser = in.readLine();
                    Menu tempmenu = ml.getMenu(currentUser);
                    String result = tempmenu.followUser(followUser);
                    if (result.equals("DNE")) {
                        out.println("DNE");
                        
                    }
                    else if(result.equals("SUCCESS")) {
                        out.println("SUCCESS");
                    }
                    
                    else if(result.equals("ALREADYFOLLOWING")) {
                        out.println("ALREADYFOLLOWING");
                    }
                    
                }
                if (response.equals("RETREIEVEFOLLOWING")) {
                    out.println("FOLLOWINGOF?");
                    String username = in.readLine();
                    User tempu = au.getUser(username);
                    UserList tempul = tempu.getListofFollowing();
                    String[] arrayofUsers = tempul.getArrayofUsers();
                    int numofFollowing = arrayofUsers.length;
                    String num = Integer.toString(numofFollowing);
                    out.println(num);
                    for (int i = 0; i<numofFollowing; i++){
                        in.readLine();
                        out.println(arrayofUsers[i]);
                    }
                    
                }
                if (response.equals("RETREIEVEFOLLOWERS")) {
                    out.println("FOLLOWERSOF?");
                    String username = in.readLine();
                    User tempu = au.getUser(username);
                    UserList tempul = tempu.getListofFollowers();
                    String[] arrayofUsers = tempul.getArrayofUsers();
                    int numofFollowers = arrayofUsers.length;
                    String num = Integer.toString(numofFollowers);
                    out.println(num);
                    for (int i = 0; i<numofFollowers; i++){
                        in.readLine();
                        out.println(arrayofUsers[i]);
                    }
                    
                }                
                
                if (response.equals("RETREIEVETWEETS")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempMenu = ml.getMenu(username);
                    String[] arrayofSubjects = tempMenu.retrieveUnreadTweets();
                    int numofTweets = arrayofSubjects.length;
                    String num = Integer.toString(numofTweets); 
                    out.println(num);
                    for (int i = 0; i < numofTweets; i++){
                        in.readLine();
                        out.println(arrayofSubjects[i]);
                    }
                    
                }
                if (response.equals("UNREADTWEET")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempMenu = ml.getMenu(username);
                    out.println("TWEETER?");
                    String user = in.readLine();
                    System.out.println(user);
                    out.println("SUBJECT?");
                    String subject = in.readLine();
                    System.out.println(subject);
                    ArrayList<String> tweet = tempMenu.readUnreadTweet(subject, user);
                    String msg = tweet.get(0);
                    System.out.println(msg);
                    String date = tweet.get(1);
                    response = in.readLine();
                    if (response.equals("MESSAGE?")) {
                        out.println(msg);
                    }
                    response = in.readLine();
                    if (response.equals("DATE?")){
                        out.println(date);
                    }     
                    
                }
                if (response.equals("READTWEET")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempMenu = ml.getMenu(username);
                    out.println("TWEETER?");
                    String user = in.readLine();
                    System.out.println(user);
                    out.println("SUBJECT?");
                    String subject = in.readLine();
                    System.out.println(subject);
                    ArrayList<String> tweet = tempMenu.readTweet(subject, user);
                    String msg = tweet.get(0);
                    System.out.println(msg);
                    String date = tweet.get(1);
                    response = in.readLine();
                    if (response.equals("MESSAGE?")) {
                        out.println(msg);
                    }
                    response = in.readLine();
                    if (response.equals("DATE?")){
                        out.println(date);
                    }     
                    
                }
                if (response.equals("SEARCHTWEETS")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempMenu = ml.getMenu(username);
                    out.println("SUBJECT?");
                    String subject = in.readLine();
                    String[] listOfTweets = tempMenu.searchTweet(subject);
                    if (listOfTweets == null) {
                        out.println("NOTWEETS"); 
                    }
                    else {
                        int numofTweets = listOfTweets.length;
                        String num = Integer.toString(numofTweets); 
                        out.println(num);
                        for (int i = 0; i < numofTweets; i++){
                            in.readLine();
                            out.println(listOfTweets[i]);
                    }
                    
                    }
                    
                }
                if (response.equals("GETONLINEIP")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    System.out.println(username);
                    UserList ol = sd.getOnlineUsers();
                    User u = ol.getUser(username);
                    System.out.println(u.getUsername());
                    ArrayList<String> oip = u.getListofOnlineFollowersIp();
                    String[] onlineIp = oip.toArray((new String[oip.size()]));
                    for (String i : onlineIp) {
                        System.out.println(i);
                    }
                    if (onlineIp == null) {
                        System.out.println("WTF");
                    }
                    
                    String num = Integer.toString(onlineIp.length);
                    out.println(num);
                    for (int i = 0; i<onlineIp.length; i++) {
                        in.readLine();
                        out.println(onlineIp[i]);
                    }
                }
                if (response.equals("RETREIEVEONLINEFOLLOWERS")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempMenu = ml.getMenu(username);
                    String[] arrayofUsers = tempMenu.retrieveOnlineUsers();
                    int numofTweets = arrayofUsers.length;
                    String num = Integer.toString(numofTweets); 
                    out.println(num);
                    for (int i = 0; i < numofTweets; i++){
                        in.readLine();
                        out.println(arrayofUsers[i]);
                    }
                    
                }                
                if (response.equals("IP?")) {
                    out.println("USERNAME?");
                    String username = in.readLine();
                    User u = ou.getUser(username);
                    String ip = u.getIp();
                    out.println(ip);
                    
                }
                if (response.equals("UNFOLLOWUSER")) {
                    out.println("CURRENTUSER?");
                    String current = in.readLine();
                    Menu tempMenu = ml.getMenu(current);
                    out.println("UNFOLLOWUSER?");
                    String unfollowing = in.readLine();
                    tempMenu.unfollowUser(unfollowing);
                    out.println("SUCCESS");
                    
                    
                }
                if (response.equals("RETREIEVEOWNTWEETS")) {

                    out.println("USERNAME?");
                    String username = in.readLine();
                    Menu tempMenu = ml.getMenu(username);
                    String[] arrayofSubjects = tempMenu.retrieveOwnTweets();
                    int numofTweets = arrayofSubjects.length;
                    String num = Integer.toString(numofTweets);
                    out.println(num);
                    for (int i = 0; i < numofTweets; i++){
                        System.out.println(i);
                        out.println(arrayofSubjects[i]);
                        System.out.println(arrayofSubjects[i]);
                        response = in.readLine();
                        
                    }       
                    
                }
            }
                

            
       
        }
                catch ( UnknownHostException e ) {
            System.err.println( "Add Protocol:  no such host" );
        }
        catch ( IOException e ) {
            System.err.println("IOEXCEPTION");
            System.err.println( e.getMessage() );
        }
     
            
    }

    public UserList getOnlineUsers() {
        return this.onlineUsers;
    }
        
    public UserList getAllUsers() {
        return this.allUsers;
    }
    
    public PublicTweetList getAllPublicTweets() {
        return this.allPublicTweets;
    }
    
    public MenuList getMenuList() { 
        return this.allMenus;
    }
}
