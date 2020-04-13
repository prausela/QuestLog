package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Set;

public class Game
{
	private final long game;
	private String title;
	private String cover;
	private String description;
	private Set<Platform> platforms;
	private Set<Developer> developers;
	private Set<Publisher> publishers;
	private Set<Genre> genres;
	private Set<Release> releaseDates;
	private boolean inBacklog;
	
	public Game(long game, String title, String cover, String description)
	{
		this.game = game;
		this.title = title;
		this.cover = cover;
		this.description = description;
		this.platforms = new HashSet<Platform>();
		this.developers = new HashSet<Developer>();
		this.publishers = new HashSet<Publisher>();
		this.genres = new HashSet<Genre>();
		this.releaseDates = new HashSet<Release>();
		this.inBacklog = false;
	}

	public long getId()
	{
		return game;
	}

	public void setTitle(String s)
	{
		title = s;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setCover(String link)
	{
		cover = link;
	}
	
	public String getCover()
	{
		return cover;
	}
	
	public void setDescription(String s)
	{
		description = s;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void addPlatform(Platform p)
	{
		platforms.add(p);
	}
	
	public void removePlatform(Platform p)
	{
		platforms.remove(p);
	}
	
	public Set<Platform> getPlatforms()
	{
		return platforms;
	}

	public void addPublisher(Publisher pub)
	{
		publishers.add(pub);
	}
	
	public void removePublisher(Publisher pub)
	{
		publishers.remove(pub);
	}
	
	public Set<Publisher> getPublishers()
	{
		return publishers;
	}
	public void addDeveloper(Developer d)
	{
		developers.add(d);
	}
	
	public void removeDeveloper(Developer d)
	{
		developers.remove(d);
	}
	
	public Set<Developer> getDevelopers()
	{
		return developers;
	}

	public void addGenre(Genre g)
	{
		genres.add(g);
	}
	
	public void removeGenre(Genre g)
	{
		genres.remove(g);
	}
	
	public Set<Genre> getGenres()
	{
		return genres;
	}
	
	public void addReleaseDate(Release r)
	{
		releaseDates.add(r);
	}
	
	public void removeReleaseDate(Release r)
	{
		releaseDates.remove(r);
	}
	
	public Set<Release> getReleaseDates()
	{
		return releaseDates;
	}
	
	public boolean getInBacklog()
	{
		return inBacklog;
	}
	
	public void setInBacklog(boolean val)
	{
		inBacklog = val;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + game);
		hashCode = 31 * hashCode + title.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Game)
		{
			Game toCompare = (Game) o;
			return this.game == toCompare.getId() && this.title.equals(toCompare.getTitle());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return title;
	}
}
