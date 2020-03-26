package ar.edu.itba.paw.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public Optional<User> findById(long id)
	{
		return userDao.findById(id);
	}

	@Override
	public User register(String username)
	{
		return userDao.create(username);
	}

}
