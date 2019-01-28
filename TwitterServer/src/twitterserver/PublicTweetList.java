/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterserver;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
/**
 *
 * @author kyunseo17
 */
public class PublicTweetList {
    private HashMap<String, ArrayList<PublicTweet>> publicTweetList;
    
    
    public PublicTweetList () {
        this.publicTweetList = new HashMap<String, ArrayList<PublicTweet>>();
    }
    
    public ArrayList<PublicTweet> getTweet(String subject){
        ArrayList<PublicTweet> pt = publicTweetList.get(subject);
        return pt;
    }
     public String[] getArrayofSubjectsandUsers() {
        ArrayList<String> listOfSubjects = new ArrayList();
        for (String key: publicTweetList.keySet()) {
            String tempname = key;
            ArrayList<PublicTweet> tempAL = this.getTweet(key);
            for (int j = 0; j < tempAL.size(); j++) {
                PublicTweet tempPT = tempAL.get(j);
                String username = tempPT.getUser();
                String tempSubjectandUser = (key+ " " +username);
                listOfSubjects.add(tempSubjectandUser);
            
            }
        }
        String[] listOfSubUser = listOfSubjects.toArray(new String[listOfSubjects.size()]);
        return listOfSubUser;
     }
    
    public Boolean addTweet(String subject, PublicTweet pt) {
        if (publicTweetList.containsKey(subject)) {
           ArrayList<PublicTweet> tempList = publicTweetList.get(subject);
           tempList.add(pt);
        }
        else {
           ArrayList<PublicTweet> tempList = new ArrayList<>();
           tempList.add(pt);
           publicTweetList.put(subject, tempList);
        }
        return true;
    }
    
    public Boolean deleteTweet(PublicTweet pt) {
        String subject = pt.getSubject();
        ArrayList<PublicTweet> tempAL = publicTweetList.get(subject);
        Iterator<PublicTweet> itr = tempAL.iterator();
        while (itr.hasNext()) {
            PublicTweet tempPt = itr.next();
            if (tempPt.getMessage().equals(pt.getMessage())) {
                itr.remove();
            }
        }
        return true;
    }
     

}
