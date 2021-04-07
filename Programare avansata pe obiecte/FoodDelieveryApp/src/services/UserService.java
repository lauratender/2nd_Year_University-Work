package services;

import model.User;

public interface UserService {
    void addUser(String n, String e, String ps, String pn, String ad);
    String login(String email);
}
