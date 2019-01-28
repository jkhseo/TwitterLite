/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterserver;


import java.util.HashMap;

/**
 *
 * @author kyunseo17
 */
public class MenuList {
    private HashMap<String, Menu> menuList;
    
    
    public MenuList () {
        this.menuList = new HashMap<>();
    }
    
    public Menu getMenu(String username){
        Menu menu  = menuList.get(username);
        return menu;
    }
    
    public Boolean addMenu(String username, Menu menu) {

           menuList.put(username, menu);
        
        return true;
    }
     

}

