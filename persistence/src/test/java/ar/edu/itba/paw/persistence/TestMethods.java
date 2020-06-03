package ar.edu.itba.paw.persistence;
import java.sql.Date;
import java.time.LocalDate;

import ar.edu.itba.paw.model.*;

import javax.persistence.EntityManager;
public class TestMethods
{
	public static Game addGame(String title, String cover, String desc, EntityManager em)
	{
		Game g = new Game(title, cover, desc);
		em.persist(g);
		return g;
	}
	
	public static Developer addDeveloper(String name, String logo, EntityManager em)
	{
		Developer d = new Developer(name, logo);
		em.persist(d);
		return d;
	}
	
	public static Publisher addPublisher(String name, String logo, EntityManager em)
	{
		Publisher p = new Publisher(name, logo);
		em.persist(p);
		return p;
	}
	
	public static Genre addGenre(String name, String logo, EntityManager em)
	{
		Genre g = new Genre(name, logo);
		em.persist(g);
		return g;
	}
	
	public static Platform addPlatform(String name, String shortName, String logo, EntityManager em)
	{
		Platform p = new Platform(name, shortName, logo);
		em.persist(p);
		return p;
	}
	
	public static Region addRegion(String name, String shortName, EntityManager em)
	{
		Region r = new Region(name, shortName);
		em.persist(r);
		return r;
	}
	
	public static void addRelease(Game g, Region r, LocalDate d, EntityManager em)
	{
		Release realease = new Release(g, r, d);
		em.persist(realease);
	}
	
	public static User addUser(String username, String password, String email, String locale, EntityManager em)
	{
		User u = new User(username, password, email, locale);
		em.persist(u);
		return u;
	}
	
	public static Score addScore(User user, Game game, int score, EntityManager em) {
		Score s = new Score(user, game, score);
		em.persist(s);
		return s;
	}
	
	public static Review addReview(User user, Game game, Platform platform, int score, String body, LocalDate date, EntityManager em) {
		Review r = new Review(user, game, platform, score, body, date);
		em.persist(r);
		return r;
	}
	
	public static Run addRun(User user, Game game, Platform platform, Playstyle playstyle, long time, EntityManager em) {
		Run r = new Run(user, game, platform, playstyle, time);
		em.persist(r);
		return r;
	}
	
	public static Playstyle addPlaystyle(String name, EntityManager em)
	{
		Playstyle p = new Playstyle(name);
		em.persist(p);
		return p;
	}
	
	public static Image addImage(String name, byte[] data, EntityManager em) {
		Image i = new Image(name, data);
		em.persist(i);
		return i;
	}
	
	public static int addRole(String roleName, EntityManager em)
	{
		Role r = new Role(roleName);
		em.persist(r);
		return r.getRole().intValue();
	}
	
	public static void connectDev(Game g, Developer d, EntityManager em)
	{
		g.addDeveloper(d);
		d.addGame(g);
	}
	
	public static void connectPub(Game g, Publisher p, EntityManager em)
	{
		g.addPublisher(p);
		p.addGame(g);
	}
	
	public static void connectGenre(Game g, Genre genre, EntityManager em)
	{
		g.addGenre(genre);
		genre.addGame(g);
	}
	
	public static void connectPlatform(Game g, Platform p, EntityManager em)
	{
		g.addPlatform(p);
		p.addGame(g);
	}

	public static void addBacklog(Game g, User u, EntityManager em)
	{
		u.addToBacklog(g);
	}
	
	public static void connectRoles(User u, int roleId, EntityManager em)
	{
		u.addRole(new Role(roleId));
	}
	
	public static PasswordResetToken addToken(User u, String token, LocalDate date, EntityManager em)
	{
		PasswordResetToken prt = new PasswordResetToken(token, u, date);
		em.persist(prt);
		return prt;
	}
}
