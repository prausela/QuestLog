package ar.edu.itba.paw.interfaces.service;
import java.util.List;
import java.util.Optional;
import ar.edu.itba.paw.model.User;

public interface UserService
{
	/**
	 * Finds a user given its ID
	 * @param id The unique ID for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findById(long id);
	Optional<User> findByIdWithDetails(long id);
	
	/**
	 * Finds a user given its usernme
	 * @param username The unique username for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameWithDetails(String username);
	
	/**
	 * Finds a user given its email
	 * @param username The unique email for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailWithDetails(String email);
	
	/**
	 * Create a new user.
	 * @param username The name of the user.
	 * @return The created user.
	 */
	User register(String username, String password, String email);

	User getLoggedUser();
	List<User> getAllUsers();
	void createPasswordResetTokenForUser(User user, String token);
	String validatePasswordResetToken(String token);
	Optional<User> getUserByPasswordResetToken(String token);
	void changeUserPassword(User user, String password);
}
