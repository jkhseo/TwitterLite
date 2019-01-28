/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterserver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PublicTweet {
    
    private String subject;
    private String message;
    private Date date_time;
    private String tweeter;
    
    public PublicTweet(String subject, String message, String tweeter) {
        this.subject = subject;
        this.message = message; 
        date_time = new Date();
        this.tweeter = tweeter;
        
        
    }
    
    public String getMessage() {
        return message; 
    }
   
    public String getDate() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = df.format(date_time);
        return date;
    }
    
    public String getUser() {
        return tweeter;
    }
    
    public String getSubject() {
        return subject;
    }
      
            
    
}
