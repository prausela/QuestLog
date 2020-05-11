package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.DeveloperDao;
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.model.Developer;

@Service
public class DeveloperServiceImpl implements DeveloperService{
	
	@Autowired
	private DeveloperDao developerDao;
	
	@Override
	public Optional<Developer> findById(long id)
	{
		Optional<Developer> d = developerDao.findById(id);
		return d;
	}
	
	@Override
	public Optional<Developer> findByName(String name)
	{
		Optional<Developer> d = developerDao.findByName(name);
		return d;
	}

	@Override
	public Optional<Developer> changeName(long id, String new_name)
	{
		return developerDao.changeName(id, new_name);
	}

	@Override
	public Optional<Developer> changeLogo(long id, String new_logo)
	{
		return developerDao.changeLogo(id, new_logo);
	}

	@Override
	public Developer register(String title, String logo)
	{
		return developerDao.register(title, logo);
	}
	
	@Override
	public List<Developer> getAllDevelopers()
	{
		return developerDao.getAllDevelopers();
	}
}