package ar.edu.itba.paw.interfaces.dao;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;

public interface ScoreDao {
	/**
	 * Find the score given by a user to a certain game.
	 * @param user	The user
	 * @param game	The game
	 * @return The score.
	 */
	Optional<Score> findScore(User user, Game game);
	
	/**
	 * Change the score given by a user to a certain game.
	 * @param user	The user
	 * @param game	The game
	 * @return The new score.
	 */
	Optional<Score> changeScore(int new_score, User user, Game game);
	
	/**
	 * Get average score given to a game by all users.
	 * @param game	The game
	 * @return The average score.
	 */
	
	Integer findAverageScore(Game game);
	
	/**
	 * Get a list of all scores
	 * @return	A list of all scores
	 */
	
	List<Score> getAllScores();
	
	/**
	 * Register a new score.
	 * @param user	The user	
	 * @param game	The game
	 * @return The score.
	 */
	Score register(User user, Game game, int score);
	
	/**
	 * Get a game given its id
	 * @param id	Game id
	 * @return	 game
	 */
	Optional<Game> getGame(long id);
	
	/**
	 * Get a user given its id
	 * @param id	User id
	 * @return	 user
	 */

	Optional<User> getUser(long id);

}