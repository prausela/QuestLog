package ar.edu.itba.paw.persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.PlatformDao;
import ar.edu.itba.paw.model.Platform;

@Repository
public class PlatformJdbcDao implements PlatformDao
{
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Platform> PLATFORM_MAPPER = new RowMapper<Platform>()
	{
		@Override
		public Platform mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Platform(rs.getInt("platform"), rs.getString("platform_name"), rs.getString("platform_name_short"), rs.getString("platform_logo"));
		}
	};
	
	@Autowired
	public PlatformJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("platforms").usingGeneratedKeyColumns("platform");
	}
	
	@Override
	public Optional<Platform> findById(long id)
	{
		Optional<Platform> p = jdbcTemplate.query("SELECT * FROM platforms WHERE platform = ?", PLATFORM_MAPPER, id).stream().findFirst();
		return p;
	}

	@Override
	public Optional<Platform> findByName(String name)
	{
		Optional<Platform> p = jdbcTemplate.query("SELECT * FROM platforms WHERE platform_name LIKE ?", PLATFORM_MAPPER, name).stream().findFirst();
		return p;
	}

	@Override
	public Optional<Platform> changeName(long id, String new_name)
	{
		jdbcTemplate.update("UPDATE platforms SET platform_name = ? WHERE platform = ?", new_name, id);
		return findById(id);
	}
	
	@Override
	public Optional<Platform> changeShortName(long id, String new_shortName)
	{
		jdbcTemplate.update("UPDATE platforms SET platform_name_short = ? WHERE platform = ?", new_shortName, id);
		return findById(id);
	}

	@Override
	public Optional<Platform> changeLogo(long id, String new_logo)
	{
		jdbcTemplate.update("UPDATE platforms SET platform_logo = ? WHERE platform = ?", new_logo, id);
		return findById(id);
	}

	@Override
	public Platform register(String name, String shortName, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("shortName", shortName);
		args.put("logo", logo);
		final Number platformId = jdbcInsert.executeAndReturnKey(args);
		return new Platform(platformId.longValue(), name, shortName, logo);
	}

	@Override
	public List<Platform> getAllPlatforms()
	{
		return jdbcTemplate.query("SELECT * FROM platforms ORDER BY platform_name", PLATFORM_MAPPER);
	}
	
	@Override
	public List<Platform> getBiggestPlatforms(int amount)
	{
		return jdbcTemplate.query("SELECT platform, platform_name, platform_name_short, platform_logo FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms ORDER BY g DESC LIMIT ?", PLATFORM_MAPPER, amount);
	}

	@Override
	public List<Platform> getPlatforms(int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT * FROM platforms ORDER BY platform_name LIMIT ? OFFSET ?", PLATFORM_MAPPER, pageSize, (page-1)*pageSize);
	}

	@Override
	public int countPlatforms()
	{
		return jdbcTemplate.queryForObject("SELECT count(*) FROM platforms", Integer.class);
	}
	
	@Override
	public List<Platform> getAllPlatformsWithGames()
	{
		return jdbcTemplate.query("SELECT platform, platform_name, platform_name_short, platform_logo FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms ORDER BY g DESC", PLATFORM_MAPPER);
	}
	
	@Override
	public List<Platform> getPlatformsWithGames(int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT platform, platform_name, platform_name_short, platform_logo FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms ORDER BY g DESC LIMIT ? OFFSET ?", PLATFORM_MAPPER, pageSize, (page-1)*pageSize);
	}

	@Override
	public int countPlatformsWithGames()
	{
		return jdbcTemplate.queryForObject("SELECT count(*) FROM (SELECT platform, platform_name, platform_name_short, platform_logo FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms ORDER BY g DESC) AS p", Integer.class);
	}
}
