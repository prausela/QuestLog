package ar.edu.itba.paw.interfaces.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;


public interface ReviewService {
	
	/**
	 * Find a review by 
	 * @param review	The review id
	 * @return The review if it exists
	 */
	
	Optional<Review> findReviewById(long review);
	
	/**
	 * Find the reviews for a certain game.
	 * @param game	The game 
	 * @return List of reviews
	 */
	
	List<Review> findGameReviews(Game game);
	
	/**
	 * Find the reviews for a certain game and platform.
	 * @param game	The game 
	 * @param platform	The platform 
	 * @return List of reviews
	 */
	
	List<Review> findGameAndPlatformReviews(Game game, Platform platform);
	
	/**
	 * Find the reviews written by a certain user.
	 * @param user	The user 
	 * @return List of reviews
	 */
	
	List<Review> findUserReviews(User user);
	
	/**
	 * Find the reviews for a certain game written by a certain user.
	 * @param game	The game 
	 * @param user	The user 
	 * @return List of reviews
	 */
	
	List<Review> findUserAndGameReviews(User user, Game game);
	
	/**
	 * Change the body of a review
	 * @param review	The review 
	 * @param body	The new body
	 * @return New review
	 */
	
	Optional<Review> changeReviewBody(long review, String body);
	
	/**
	 * Get a list of all review
	 * @return	A list of all reviews
	 */
	
	List<Review> getAllReviews();
	
	/**
	 * Register a new review.
	 * @param user	The user 	
	 * @param game	The game 
	 * @param platform	The platform 
	 * @param score		The score given by user at the time the review is made
	 * @param body 	The written review
	 * @param date	The date the review was written
	 * @return The review.
	 */
	
	Review register(User user, Game game, Platform platform, int score, String body, Date date);
}