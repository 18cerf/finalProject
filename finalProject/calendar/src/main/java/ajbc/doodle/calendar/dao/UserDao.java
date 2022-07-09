package ajbc.doodle.calendar.dao;

import ajbc.doodle.calendar.entities.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface UserDao {


	public default List<User> getAllUsers() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void addUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void updateUser(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}


	public default User getUserById(int id) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default User getUserByEmail(String email) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void deleteUserSoftly(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void deleteUserHardly(User user) throws DaoException {
		throw new DaoException("Method not implemented");
	}

}