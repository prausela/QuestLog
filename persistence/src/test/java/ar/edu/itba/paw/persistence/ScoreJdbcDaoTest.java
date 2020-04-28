package ar.edu.itba.paw.persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})

public class ScoreJdbcDaoTest {
	
	private static final String SCORE_TABLE = "scores";
	private static final String GAME_TABLE = "games";
	private	static final String USER_TABLE = "users";
	private static final int SCORE = 87;
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
	private	static final String USERNAME = "Username";
	private	static final String PASSWORD = "password";
	private	static final String EMAIL = "email@example.com";
	
	@Autowired
	private DataSource ds;
	

	private JdbcTemplate jdbcGameTemplate;
	private SimpleJdbcInsert gameInsert;
	
	

	private JdbcTemplate jdbcUserTemplate;
	private SimpleJdbcInsert userInsert;
	
	@Autowired
	private ScoreJdbcDao scoreDao;
	private SimpleJdbcInsert scoreInsert;
	private JdbcTemplate jdbcScoreTemplate;
	
	@Before
	public void	setUp()
	{
		jdbcGameTemplate = new JdbcTemplate(ds);
		gameInsert = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		JdbcTestUtils.deleteFromTables(jdbcGameTemplate, GAME_TABLE);
		
		scoreDao = new ScoreJdbcDao(ds);
		jdbcScoreTemplate = new JdbcTemplate(ds);
		scoreInsert = new SimpleJdbcInsert(ds).withTableName(SCORE_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcScoreTemplate, SCORE_TABLE);
		
		jdbcUserTemplate = new JdbcTemplate(ds);
		userInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("users");
		JdbcTestUtils.deleteFromTables(jdbcUserTemplate, USER_TABLE);
		
	}
	
	@Test
	public void testRegisterScore()
	{	Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, userInsert);
		final Score s = scoreDao.register(u, g, SCORE);
		Assert.assertNotNull(s);
		Assert.assertEquals(u.getId(), s.getUser().getId());
		Assert.assertEquals(g.getId(), s.getGame().getId());
		Assert.assertEquals(SCORE, s.getScore());
	}
	
	@Test
	public void	testFindRunByIdExists()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User user = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, userInsert);
		Score s = TestMethods.addScore(user, game, SCORE, scoreInsert);
		Optional<Score> maybeScore = scoreDao.findScore(user, game);
		Assert.assertTrue(maybeScore.isPresent());
		Assert.assertEquals(game.getId(), maybeScore.get().getGame().getId());
		Assert.assertEquals(user.getId(), maybeScore.get().getUser().getId());
		Assert.assertEquals(s.getScore(), maybeScore.get().getScore());
	}
	
	@Test
	public void testFindAverageScore()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User user1 = TestMethods.addUser("user 1", PASSWORD, EMAIL, userInsert);
		User user2 = TestMethods.addUser("user 2", PASSWORD, EMAIL+"1", userInsert);
		User user3 = TestMethods.addUser("user 3", PASSWORD, EMAIL+"2", userInsert);
		Score s1 = TestMethods.addScore(user1, game, 55, scoreInsert);
		Score s2 = TestMethods.addScore(user2, game, 23, scoreInsert);
		Score s3 = TestMethods.addScore(user3, game, 30, scoreInsert);
		int avScore = scoreDao.findAverageScore(game);
		Assert.assertEquals(avScore, (55 + 23 + 30)/3);
	}
	
	@Test
	public void testGetAllScores() 
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game game2 = TestMethods.addGame("Zelda 27", GAME_COVER, GAME_DESC, gameInsert);		
		User user2 = TestMethods.addUser("pepito", PASSWORD, EMAIL, userInsert);
		User user3 = TestMethods.addUser("miguelito", PASSWORD, EMAIL+"1", userInsert);
		Score s2 = TestMethods.addScore(user2, game, 23, scoreInsert);
		Score s3 = TestMethods.addScore(user3, game, 30, scoreInsert);
		Score s4 = TestMethods.addScore(user2, game2, 30, scoreInsert);
		List<Score> scores = scoreDao.getAllScores();
		List<Score> myList = new ArrayList<Score>();
		myList.add(s2);
		myList.add(s3);
		myList.add(s4);
		
		Assert.assertFalse(scores.isEmpty());
		Assert.assertEquals(3, scores.size());
		Assert.assertEquals(scores.get(0).getUser().getId(), myList.get(0).getUser().getId());
		Assert.assertEquals(scores.get(0).getGame().getTitle(), myList.get(0).getGame().getTitle());
		Assert.assertEquals(scores.get(2).getGame().getTitle(), myList.get(2).getGame().getTitle());
		Assert.assertNotEquals(scores.get(0).getGame().getTitle(), myList.get(2).getGame().getTitle());
		Assert.assertEquals(scores.get(2).getUser().getUsername(), myList.get(2).getUser().getUsername());

	}


}
