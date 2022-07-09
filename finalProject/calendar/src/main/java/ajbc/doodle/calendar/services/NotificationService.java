package ajbc.doodle.calendar.services;

import ajbc.doodle.calendar.dao.DaoException;
import ajbc.doodle.calendar.dao.EventDao;
import ajbc.doodle.calendar.dao.NotificationDao;
import ajbc.doodle.calendar.dao.UserDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.entities.Notification;
import ajbc.doodle.calendar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private EventDao eventDao;

	public List<Notification> getAllNotifications() throws DaoException {
		return notificationDao.getAllNotifications();
	}

	public void addNotificationOfUserEvent(int userId, int eventId, Notification newNotification) throws DaoException {
		if (checkIfUserBelongToEvent(eventId, userId)==false)
			throw new DaoException("The current user doesnt Belong to this Event");

		newNotification.setEventToNotify(eventDao.getEventById(eventId));
		newNotification.setUserToNotify(userDao.getUserById(userId));
		notificationDao.addNotification(newNotification);
	}

	public void updateNotification(Notification notification, Integer userId) throws DaoException {

		if (userId.equals(notification.getUserId())==false)
			throw new DaoException("Owner only can update notification");
		notificationDao.updateNotification(notification);
	}

	// Queries
	public Notification getNotificationById(Integer NotificationId) throws DaoException {
		return notificationDao.getNotificationById(NotificationId);
	}

	@Transactional(rollbackFor = {DaoException.class})
	public List<Notification> getEventNotifications(Integer eventId) throws DaoException {
		Event event = eventDao.getEventById(eventId);
		return event.getNotifications().stream().collect(Collectors.toList());
	}


	private boolean checkIfUserBelongToEvent(int eventId, int userId) throws DaoException {
		Event event = eventDao.getEventById(eventId);
		return event.getEventGuests().stream()
				.map(User::getUserId).anyMatch(i -> i == userId);
	}

}