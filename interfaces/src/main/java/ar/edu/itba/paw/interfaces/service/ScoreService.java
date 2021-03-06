package ar.edu.itba.paw.interfaces.service;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;

public interface ScoreService {
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

	List<Score> findAllUserScores(User user);
	List<Score> findAllUserScores(User user, int page, int pageSize);
	int countAllUserScores(User user);
	
	List<Score> findAllGameScores(Game game);
	List<Score> findAllGameScores(Game game, int page, int pageSize);
	int countAllGameScores(Game game);
}
