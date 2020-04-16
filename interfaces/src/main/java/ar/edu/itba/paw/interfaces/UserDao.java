package ar.edu.itba.paw.interfaces;
import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserDao
{
	/**
	 * Finds a user given its ID
	 * @param id The unique ID for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findById(long id);
	
	/**
	 * Finds a user given its usernme
	 * @param username The unique username for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findByUsername(String username);
	
	/**
	 * Create a new user.
	 * @param username The name of the user.
	 * @return The created user.
	 */
	User create(String username, String password);
}
