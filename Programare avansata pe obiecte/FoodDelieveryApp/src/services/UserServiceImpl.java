package services;

import model.User;

import java.util.HashMap;

public class UserServiceImpl implements UserService{
    private static UserServiceImpl instance = null;
    private static HashMap<String, User> users;

    private UserServiceImpl(){
        users = new HashMap<>();
    }

    public static UserServiceImpl getInstance(){
        if (instance == null)
            instance = new UserServiceImpl();
        return instance;
    }

    @Override
    public void addUser(String n, String e, String ps, String pn, String ad){
        User u = new User(n, e, ps, pn, ad);
        users.put(e, u);
    }

    @Override
    public String login(String email){
        boolean found = false;
        for(String e: users.keySet())
            if(e.equals(email))
                found = true;
        if(!found)
            email = null;
        return email;
    }

    public static HashMap<String, User> getUsers(){
        return users;
    }
}
