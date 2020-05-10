package ar.edu.itba.paw.webapp.controller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;

@Controller
public class MappingController
{
	@Autowired
	private GameService gs;

	@Autowired
	private UserService us;
	
	@Autowired
	private PlatformService ps;
	
	@Autowired
	private DeveloperService ds;
	
	@Autowired
	private GenreService gens;
	
	@Autowired
	private PublisherService pubs;
	
	@Autowired
	private ReviewService revs;
	
	@Autowired
	private RunService runs;
	
	@Autowired
	private ScoreService scors;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MappingController.class);
	
	@RequestMapping("/")
	public ModelAndView index(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("cookieBacklog", backlog);
		
		User u = us.getLoggedUser();
		if(u == null)
		{
			mav.addObject("backlogGames", getGamesInBacklog(backlog));
			mav.addObject("upcomingGames", getUpcomingGames(backlog));
			mav.addObject("popularGames", getPopularGames(backlog));
		}
		else
		{
			mav.addObject("backlogGames", gs.getGamesInBacklog());
			mav.addObject("recommendedGames", gs.getRecommendedGames());
			mav.addObject("popularGames", gs.getPopularGames());
			mav.addObject("upcomingGames", gs.getUpcomingGames());
		}
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowIndex(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String search, @CookieValue(value="backlog", defaultValue="") String backlog,
			@RequestParam(required = false, defaultValue = "1", value = "currentPage") int currentPage)
	{
		int page = currentPage;
		int pageSize = 15;
		int countResults = gs.countSearchResults(search);
		int totalPages = countResults/15 + 1;
		
		final ModelAndView mav = new ModelAndView("gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		mav.addObject("search", search);
		mav.addObject("pages",totalPages);
		mav.addObject("current",page);
		if(countResults == 0) {
			mav.addObject("games", null);
			return mav;
		}
		User u = us.getLoggedUser();
		List<Game> searchResults = gs.searchByTitle(search, page, pageSize);
		
		if(u == null)
		{
			updateWithBacklogDetails(searchResults, backlog);
			mav.addObject("games",searchResults);
		}
		else
		{
			mav.addObject("games", searchResults);
		}
		return mav;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndContinueSearch(@RequestParam String search, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog,
			@RequestParam(required = false, defaultValue = "1", value = "?page") int currentPage)
	{
		int page = currentPage;
		int pageSize = 15;
		int countResults = gs.countSearchResults(search);
		int totalPages = countResults/15 + 1;
		backlog = toggleBacklog(gameId, response, backlog);
		final ModelAndView mav = new ModelAndView("gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		mav.addObject("pages",totalPages);
		mav.addObject("current",page);
		if(countResults == 0) {
			mav.addObject("games", null);
			return mav;
		}
		
		User u = us.getLoggedUser();
		List<Game> searchResults = gs.searchByTitle(search, page, pageSize);
		if(u == null)
		{
			updateWithBacklogDetails(searchResults, backlog);
			mav.addObject("search", search);
			mav.addObject("games", searchResults);
		}
		else
		{
			mav.addObject("search", search);
			mav.addObject("games", searchResults);
		}
		return mav;
	}
	

	@RequestMapping(value = "/searchFilter", method = RequestMethod.GET)
	public ModelAndView filteredSearch(@RequestParam(required = false, defaultValue = "9999", value ="hoursRight") int hoursRight, @RequestParam(required = false, defaultValue = "59", value ="minsRight") int minsRight, 
			@RequestParam(required = false, defaultValue = "59", value ="secsRight") int secsRight,
			@RequestParam(required = false, defaultValue = "0", value ="hoursLeft") int hoursLeft, @RequestParam(required = false, defaultValue = "0", value ="minsLeft") int minsLeft,
			@RequestParam(required = false, defaultValue = "0", value ="secsLeft") int secsLeft,
			@RequestParam(required = false, defaultValue = "100", value ="scoreRight") int scoreRight, @RequestParam(required = false, defaultValue = "0", value ="scoreLeft") int scoreLeft,
			@RequestParam(required = false, defaultValue = "", value = "platforms") List<String> platforms, 
			@RequestParam(required = false, defaultValue = "", value = "genres") List<String> genres, @RequestParam String search,
			@CookieValue(value="backlog", defaultValue="") String backlog, @RequestParam int page)
	{
		final ModelAndView mav = new ModelAndView("gameSearch");
		int pageSize = 15;
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		int timeLeft = hoursLeft*3600 + minsLeft*60 + secsLeft;
		int timeRight = hoursRight*3600 + minsRight*60 + secsRight;
		int countResults = gs.countSearchResultsFiltered(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight);
		int totalPages = (countResults + pageSize - 1)/pageSize; 
		mav.addObject("pages",totalPages);
		mav.addObject("current",page);
		
		mav.addObject("hoursLeft", hoursLeft);
		mav.addObject("minsLeft", minsLeft);
		mav.addObject("secsLeft", secsLeft);
		mav.addObject("hoursRight", hoursRight);
		mav.addObject("minsRight", minsRight);
		mav.addObject("secsRight", secsRight);
		mav.addObject("scoreLeft", scoreLeft);
		mav.addObject("scoreRight", scoreRight);
		mav.addObject("currentPlats", String.join(", ", platforms));
		mav.addObject("currentGens", String.join(", ", genres));
		
		System.out.println("");
		System.out.println("COUNTS");
		System.out.println(countResults);
		System.out.println("");
		System.out.println("");
		System.out.println(totalPages);
		System.out.println("");
		if(countResults == 0) {
			mav.addObject("games", null);
			return mav;
		}
		List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, pageSize);
		
		if(u == null)
		{	
			updateWithBacklogDetails(filteredResults, backlog);
			mav.addObject("searchTerm", search);
			mav.addObject("games", filteredResults);
		}
		else
		{
			mav.addObject("searchTerm", search);
			mav.addObject("games", filteredResults);
		}
		return mav;
	}
	
	@RequestMapping(value = "/searchFilter", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndContinueFilteredSearch(@RequestParam(required = false, defaultValue = "9999", value ="hoursRight") int hoursRight, @RequestParam(required = false, defaultValue = "59", value ="minsRight") int minsRight, 
			@RequestParam(required = false, defaultValue = "59", value ="secsRight") int secsRight,
			@RequestParam(required = false, defaultValue = "0", value ="hoursLeft") int hoursLeft, @RequestParam(required = false, defaultValue = "0", value ="minsLeft") int minsLeft, 
			@RequestParam(required = false, defaultValue = "0", value ="secsLeft") int secsLeft,
			@RequestParam(required = false, defaultValue = "100", value ="scoreRight") int scoreRight, @RequestParam(required = false, defaultValue = "0", value ="scoreLeft") int scoreLeft,
			@RequestParam(required = false, defaultValue = "", value = "platforms") List<String> platforms, 
			@RequestParam(required = false, defaultValue = "", value = "genres") List<String> genres, @RequestParam String search,
			HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog, @RequestParam long gameId, @RequestParam int page)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		final ModelAndView mav = new ModelAndView("gameSearch");
		int pageSize = 15;
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		int timeLeft = hoursLeft*3600 + minsLeft*60 + secsLeft;
		int timeRight = hoursRight*3600 + minsRight*60 + secsRight;
		int countResults = gs.countSearchResultsFiltered(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight);
		int totalPages = (countResults + pageSize - 1)/pageSize; 
		mav.addObject("pages",totalPages);
		mav.addObject("current",page);
		
		mav.addObject("hoursLeft", hoursLeft);
		mav.addObject("minsLeft", minsLeft);
		mav.addObject("secsLeft", secsLeft);
		mav.addObject("hoursRight", hoursRight);
		mav.addObject("minsRight", minsRight);
		mav.addObject("secsRight", secsRight);
		mav.addObject("scoreLeft", scoreLeft);
		mav.addObject("scoreRight", scoreRight);
		mav.addObject("currentPlats", String.join(", ", platforms));
		mav.addObject("currentGens", String.join(", ", genres));

		if(countResults == 0) {
			mav.addObject("games", null);
			return mav;
		}
		List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, pageSize);
		
		if(u == null)
		{	
			updateWithBacklogDetails(filteredResults, backlog);
			mav.addObject("search", search);
			mav.addObject("games", filteredResults);
		}
		else
		{
			mav.addObject("search", search);
			mav.addObject("games", filteredResults);
		}
		return mav;
	}
	
	@RequestMapping("/games")
	public ModelAndView gamesList(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("allGames");
		User u = us.getLoggedUser();
		if(u == null)
		{
			List<Game> games = gs.getAllGames();
			updateWithBacklogDetails(games, backlog);
			mav.addObject("games", games);
		}
		else
		{
			mav.addObject("games", gs.getAllGames());
		}
		return mav;
	}
	
	@RequestMapping(value = "/games/scores/{gameId}", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam("score") int scoreInput, @RequestParam("game") long gameId) 
	{
		User user = us.getLoggedUser();
		if(user == null)
			return new ModelAndView("redirect:/games/{gameId}");
		Optional<Game> game = gs.findByIdWithDetails(gameId);
		Optional<Score> score = scors.findScore(user, game.get());
		if (score.isPresent())
			scors.changeScore(scoreInput, user, game.get());
		else
			scors.register(user, game.get(), scoreInput);
		return new ModelAndView("redirect:/games/{gameId}");
	}
	
	@RequestMapping(value = "/createRun/run/{gameId}", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam("hours") int hours, @RequestParam("mins") int mins, @RequestParam("secs") int secs,
			@RequestParam("game") long gameId, @RequestParam("platforms") String platform, @RequestParam("playstyles") String playst) 
	{
		User user = us.getLoggedUser();
		if(user == null)
			return new ModelAndView("redirect:/games/{gameId}");
		Optional <Game> game = gs.findByIdWithDetails(gameId);
		Optional <Platform> plat = ps.findByName(platform);
		long time = hours*3600 + mins*60 + secs;
		Optional <Playstyle> play = runs.findPlaystyleByName(playst);
		if(game.isPresent() && plat.isPresent() && play.isPresent())
			runs.register(user, game.get(), plat.get(), play.get(), time);
		return new ModelAndView("redirect:/games/{gameId}");
	}	
	
	@RequestMapping("/createRun/{gameId}")
	public ModelAndView createRun(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		User u = us.getLoggedUser();
		if(u == null)
			return new ModelAndView("redirect:/games/{gameId}");
		final ModelAndView mav = new ModelAndView("createRun");
		Game g = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
		mav.addObject("game",g);
		mav.addObject("playstyles",runs.getAllPlaystyles());
		return mav;
	}
	
	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ModelAndView gamesList(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/games");
	}
	
	@RequestMapping("/games/{gameId}")
	public ModelAndView gameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("game");
		User u = us.getLoggedUser();
		Game g = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
		mav.addObject("playAverage", runs.getAverageAllPlayStyles(g));
		mav.addObject("averageScore", scors.findAverageScore(g));
		if(u == null)
		{	
			g.setInBacklog(gameInBacklog(gameId, backlog));
			mav.addObject("game", g);
		}
		else
		{	
			mav.addObject("game", g);
			Optional<Score> sc = scors.findScore(u,g);
			if(sc.isPresent())
				mav.addObject("user_score",sc.get());
			else
				mav.addObject("user_score",null);
			mav.addObject("user_runs", runs.findGameRuns(g, u));
		}
		return mav;
	}

	@RequestMapping(value = "/games/{gameId}", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowGameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/games/{gameId}");
	}
	

	@RequestMapping("/platforms")
	public ModelAndView platformsList()
	{
		final ModelAndView mav = new ModelAndView("allPlatforms");
		List<Platform> list = ps.getAllPlatforms();
		mav.addObject("platforms", list);
		mav.addObject("listSize", list.size());
		return mav;
	}
	
	@RequestMapping("/platforms/{platformId}")
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("platform");
		User u = us.getLoggedUser();
		Platform p = ps.findById(platformId).orElseThrow(PlatformNotFoundException::new);
		if(u == null)
			updateWithBacklogDetails(p.getGames(), backlog);
		mav.addObject("platform", p);
		return mav;
	}
	
	@RequestMapping(value = "/platforms/{platformId}", method = RequestMethod.POST)
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/platforms/{platformId}");
	}
	
	@RequestMapping("/developers")
	public ModelAndView developersList()
	{
		final ModelAndView mav = new ModelAndView("allDevelopers");
		List<Developer> list = ds.getAllDevelopers();
		mav.addObject("developers", list);
		mav.addObject("listSize", list.size());
		return mav;
	}
	
	@RequestMapping("/developers/{devId}")
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("developer");
		Developer d = ds.findById(devId).orElseThrow(DeveloperNotFoundException::new);
		User u = us.getLoggedUser();
		if(u == null)
			updateWithBacklogDetails(d.getGames(), backlog);
		mav.addObject("developer", d);
		return mav;
	}
	
	@RequestMapping(value = "/developers/{devId}", method = RequestMethod.POST)
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/developers/{devId}");
	}
	
	@RequestMapping("/publishers")
	public ModelAndView publishersList()
	{
		final ModelAndView mav = new ModelAndView("allPublishers");
		List<Publisher> list = pubs.getAllPublishers();
		mav.addObject("publishers", list);
		mav.addObject("listSize", list.size());
		return mav;
	}
	
	@RequestMapping("/publishers/{pubId}")
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("publisher");
		User u = us.getLoggedUser();
		Publisher p = pubs.findById(pubId).orElseThrow(PublisherNotFoundException::new);
		if(u == null)
			updateWithBacklogDetails(p.getGames(), backlog);
		mav.addObject("publisher", p);
		return mav;
	}
	
	@RequestMapping(value = "/publishers/{pubId}", method = RequestMethod.POST)
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/publishers/{pubId}");
	}
	
	@RequestMapping("/genres")
	public ModelAndView genresList()
	{
		final ModelAndView mav = new ModelAndView("allGenres");
		mav.addObject("genres", gens.getAllGenres());
		return mav;
	}
	
	@RequestMapping("/genres/{genreId}")
	public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("genre");
		User u = us.getLoggedUser();
		Genre g = gens.findById(genreId).orElseThrow(GenreNotFoundException::new);
		if(u == null)
			updateWithBacklogDetails(g.getGames(), backlog);
		mav.addObject("genre", g);
		return mav;
	}
	
	@RequestMapping(value = "/genres/{genreId}", method = RequestMethod.POST)
	public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/genres/{genreId}");
	}
	
	@RequestMapping("/explore")
	public ModelAndView explore()
	{
		final ModelAndView mav = new ModelAndView("explore");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("developers", ds.getAllDevelopers());
		mav.addObject("publishers", pubs.getAllPublishers());
		mav.addObject("genres", gens.getAllGenres());
		return mav;
	}
	
	@RequestMapping("/transfer_backlog")
	public ModelAndView transferBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		transferBacklog(response, backlog, us.getLoggedUser());
		clearAnonBacklog(response);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/clear_backlog")
	public ModelAndView clearBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		clearAnonBacklog(response);
		return new ModelAndView("redirect:/");
	}
	
///////////////////////////////////////////////////////////////////////
	
	private String toggleBacklog(long gameId, HttpServletResponse response, String backlog)
	{
		User u = us.getLoggedUser();
		if(u == null)
		{
			if(gameInBacklog(gameId, backlog))
				backlog = removeFromBacklog(gameId, backlog);
			else
				backlog = addToBacklog(gameId, backlog);
			updateBacklogCookie(response, backlog);
		}
		else
		{
			gs.toggleBacklog(gameId);
		}
		return backlog;
	}
		
	private void clearAnonBacklog(HttpServletResponse response)
	{
		updateBacklogCookie(response, "");
	}
	
	private void updateBacklogCookie(HttpServletResponse response, String backlog)
	{
		Cookie cookie = new Cookie("backlog", backlog);
		cookie.setPath("/");
		cookie.setMaxAge(600000);
		response.addCookie(cookie);
	}

	
	private List<Game> getGamesInBacklog(String backlog)
	{
		List<Game> list = new ArrayList<Game>();
		String[] ids = backlog.split("-");
		for(String id : ids)
		{
			if(!id.isEmpty())
			{
				Optional<Game> g = gs.findById(Long.parseLong(id));
				if(g.isPresent())
				{
					list.add(g.get());
					g.get().setInBacklog(true);
				}
			}
		}
		return list;
	}
	
	private String addToBacklog(long gameId, String backlog)
	{
		if(gameInBacklog(gameId, backlog))
			return backlog;
		return backlog +"-" +gameId +"-";
	}
	
	private String removeFromBacklog(long gameId, String backlog)
	{
		return backlog.replaceAll("-"+gameId+"-", "");
	}
	
	private void transferBacklog(HttpServletResponse response, String backlog, User u)
	{
		List<Game> anonGames = getGamesInBacklog(backlog);
		for(Game g : anonGames)
			gs.addToBacklog(g.getId());
		clearAnonBacklog(response);
	}
	
	public List<Game> getUpcomingGames(String backlog)
	{
		List<Game> list = gs.getUpcomingGames();
		updateWithBacklogDetails(list, backlog);
		return list;
	}
	
	public List<Game> getPopularGames(String backlog)
	{
		List<Game> list = gs.getPopularGames();
		updateWithBacklogDetails(list, backlog);
		return list;
	}
	
	private void updateWithBacklogDetails(Collection<Game> games, String backlog)
	{
		for(Game g : games)
			g.setInBacklog(gameInBacklog(g.getId(), backlog));
	}
	
	private boolean gameInBacklog(long gameId, String backlog)
	{
		return backlog.contains("-" +gameId +"-");
	}
}