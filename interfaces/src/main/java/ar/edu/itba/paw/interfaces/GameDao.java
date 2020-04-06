package ar.edu.itba.paw.interfaces;
import java.util.List;
import java.util.Optional;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;

public interface GameDao
{
	/**
	 * Finds a game given its ID
	 * @param id 	The unique ID for the game.
	 * @return 		The matched game, or null otherwise.
	 */
	Optional<Game> findById(long id);
	
	/**
	 * Finds a game or several games with a given title
	 * @param title The title for the game.
	 * @return 		List of games that share that title.
	 */
	List<Game> findByTitle(String title);
	
	/**
	 * Change a game's title
	 * @param id		The id of the game
	 * @param new_title The new title for the game.
	 * @return 			The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeTitle(long id, String new_title);
	
	/**
	 * Change a game's cover
	 * @param id		The id of the game
	 * @param new_cover The new cover for the game.
	 * @return 			The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeCover(long id, String new_cover);
	
	/**
	 * Change a game's description
	 * @param id		The id of the game
	 * @param new_desc 	The new description for the game.
	 * @return 			The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeDescription(long id, String new_desc);
	
	/**
	 * Create a new game.
	 * @param title 	The title of the game.
	 * @return 			The registered game.
	 */
	Game register(String title, String cover, String description);
	
	/**
	 * Get a list of all available games.
	 * @return 	The list of all games.
	 */
	List<Game> getAllGames();
	
	/**
	 * Links a game to a specified platform
	 * @param g		The game
	 * @param p		The platform to link the game to
	 * @return		The updated game, now linked to the platform
	 */
	Optional<Game> addPlatform(Game g, Platform p);
	
	/**
	 * Unlinks a game to a specified platform
	 * @param g		The game
	 * @param p		The platform to unlink the game from
	 * @return		The updated game, now unlinked from the platform
	 */
	Optional<Game> removePlatform(Game g, Platform p);
	
	/**
	 * Get a list of all platforms the game is available on
	 * @param g		The game
	 * @return		The list of platforms
	 */
	List<Platform> getAllPlatforms(Game g);
	
	/**
	 * Categorize a game as part of a certain genre
	 * @param game		The game
	 * @param genre		The genre
	 * @return			The updated game, now marked as being of that genre
	 */
	Optional<Game> addGenre(Game game, Genre genre);
	
	/**
	 * Stop categorizing a game as being part of a genre
	 * @param game		The game
	 * @param genre		The genre
	 * @return			The updated game, now marked as not being of that genre
	 */
	Optional<Game> removeGenre(Game game, Genre genre);
	
	/**
	 * Get a list of all genres the game is classified as
	 * @param g		The game
	 * @return		The list of genres
	 */
	List<Genre> getAllGenres(Game g);
}
