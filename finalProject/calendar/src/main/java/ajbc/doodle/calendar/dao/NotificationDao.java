package ajbc.doodle.calendar.dao;

import ajbc.doodle.calendar.entities.Notification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface NotificationDao {

	public default List<Notification> getAllNotifications() throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void addNotification(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void updateNotification(Notification notification) throws DaoException {
		throw new DaoException("Method not implemented");
	}


	public default Notification getNotificationById(Integer id) throws DaoException {
		throw new DaoException("Method not implemented");
	}


}