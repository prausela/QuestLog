package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PublisherDao;
import ar.edu.itba.paw.interfaces.PublisherService;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Game;

@Service
public class PublisherServiceImpl implements PublisherService
{
	
	@Autowired
	private PublisherDao publisherDao;
	
	@Autowired
	private GameService gs;
	
	@Override
	public Optional<Publisher> findById(long id)
	{
		Optional<Publisher> p = publisherDao.findById(id);
		return p;
	}
	
	@Override
	public Optional<Publisher> findById(long id, String backlog, User u)
	{
		Optional<Publisher> p = publisherDao.findById(id);
		if(p.isPresent())
		{
			gs.updateBacklogDetails(p.get().getGames(), backlog, u);
		}
		return p;
	}

	@Override
	public Optional<Publisher> findByName(String name)
	{
		return publisherDao.findByName(name);
	}
	
	@Override
	public Optional<Publisher> findByName(String name, String backlog, User u)
	{
		Optional<Publisher> p = publisherDao.findByName(name);
		if(p.isPresent())
		{
			gs.updateBacklogDetails(p.get().getGames(), backlog, u);
		}
		return p;
	}

	@Override
	public Optional<Publisher> changeName(long id, String new_name)
	{
		return publisherDao.changeName(id, new_name);
	}

	@Override
	public Optional<Publisher> changeLogo(long id, String new_logo)
	{
		return publisherDao.changeLogo(id, new_logo);
	}

	@Override
	public Publisher register(String title, String logo)
	{
		return publisherDao.register(title, logo);
	}

	@Override
	public List<Publisher> getAllPublishers()
	{
		return publisherDao.getAllPublishers();
	}
	
	@Override
	public List<Publisher> getAllPublishersWithGames()
	{
		return publisherDao.getAllPublishersWithGames();
	}
	
	@Override
	public List<Game> getAllGames(Publisher pub)
	{
		return publisherDao.getAllGames(pub);
	}
}
