package ajbc.doodle.calendar.services;

import ajbc.doodle.calendar.dao.DaoException;
import ajbc.doodle.calendar.dao.EventDao;
import ajbc.doodle.calendar.dao.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EventDao eventDao;

	public List<User> getAllUsers() throws DaoException {
		return userDao.getAllUsers();
	}
	
	public void addUser(User user) throws DaoException {
		userDao.addUser(user);
	}
	
	public void updateUser(User user) throws DaoException {
		userDao.updateUser(user);
	}

	public User getUserById(Integer userId) throws DaoException {
		return userDao.getUserById(userId);
	}

	public User getUserByEmail(String email) throws DaoException {
		return userDao.getUserByEmail(email);
	}
	
	public void deleteUserSoftly(User user) throws DaoException {
		userDao.deleteUserSoftly(user);	
	}
	
	public void deleteUserHardly(User user) throws DaoException {
		userDao.deleteUserHardly(user);	
	}
	
	@Transactional
	public List<User> getEventUsers(Integer eventId) throws DaoException {
		Event event = eventDao.getEventById(eventId);
		return event.getEventGuests().stream().collect(Collectors.toList());
	}
	
}