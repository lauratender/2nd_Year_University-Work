package model;

import java.util.List;
public class User {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String userAddress;

    public User(String n, String e, String ps, String pn, String ad) {
        name = n;
        email = e;
        password = ps;
        phoneNumber = pn;
        userAddress = ad;
    }

    public void giveRating(List <Rating> l, Restaurant r, int value){
        if(value <1 || value >5){
            System.out.println("Ratingul este de la 1 la 5.");
            return;
        }
        boolean found = false;
        // if a rating for a restaurant from the same user already existed
        // we update it
        for (Rating rating: l){
            if(rating.getUser() == this && rating.getRestaurant() == r) {
                rating.setValue(value);
                found = true;
            }
        }
        // if a previous rating does not exist we add a new one to the ratings' list
        if(!found){
            Rating newR = new Rating(r, this, value);
            l.add(newR);
        }
        r.updateRating(l);
    }
}
