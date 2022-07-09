package ajbc.doodle.calendar.dao;

import ajbc.doodle.calendar.entities.Event;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface EventDao {


	public default List<Event> getAllEvents() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void addEvent(Event event) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void updateEvent(Event event) throws DaoException{
		throw new DaoException("Method not implemented");
	}


	public default Event getEventById(Integer eventId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Event> getEventsByCategoryId(Integer categoryId) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Event> getEventsByCategoryName(String categoryName) throws DaoException {
		throw new DaoException("Method not implemented");
	}

}