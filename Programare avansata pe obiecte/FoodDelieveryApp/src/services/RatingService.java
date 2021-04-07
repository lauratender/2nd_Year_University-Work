package services;

import model.Rating;

public interface RatingService {
    //Rating getRating();
    void seeRating(String restName);
    void giveRating(String userEmail, String restName, int value);
}
