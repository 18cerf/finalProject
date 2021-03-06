package ajbc.doodle.calendar.hibernates;

import ajbc.doodle.calendar.dao.DaoException;
import ajbc.doodle.calendar.dao.EventDao;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.enums.Category;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "HNT_event")
@SuppressWarnings("unchecked")
public class HibernateTemplateEventDao implements EventDao {

	@Autowired
	private HibernateTemplate template;


	@Override
	public List<Event> getAllEvents() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);

		DetachedCriteria resultTransformer = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Event> resultList = (List<Event>) template.findByCriteria(resultTransformer);
		return resultList;
	}
	
	@Override
	public void addEvent(Event event) throws DaoException {
		template.persist(event);
	}
	
	@Override
	public void updateEvent(Event event) throws DaoException {
		template.merge(event);
	}



	@Override
	public Event getEventById(Integer eventId) throws DaoException {
		Event event = template.get(Event.class, eventId);
		if(event == null)
			throw new DaoException("There is no such event in 'events' DB with id: "+eventId);
		return event;
	}
	
	@Override
	public List<Event> getEventsByCategoryId(Integer categoryId) throws DaoException  {
		List<Event> allEvents = getAllEvents();
		List<Event> eventsInCategory = new ArrayList<Event>();
		for(Event e : allEvents)
			if(e.getCategory() == Category.values()[categoryId-1])
				eventsInCategory.add(template.get(Event.class, e.getEventId()));

		if (eventsInCategory.isEmpty())
			throw new DaoException("There is no such events in category id: "+categoryId);
		return eventsInCategory;
	}
	
	@Override
	public List<Event> getEventsByCategoryName(String categoryName) throws DaoException  {
		List<Event> allEvents = getAllEvents();
		List<Event> eventsInCategory = new ArrayList<Event>();
		for(Event e : allEvents)
			if(e.getCategory() == Category.valueOf(categoryName))
				eventsInCategory.add(template.get(Event.class, e.getEventId()));

		if (eventsInCategory.isEmpty())
			throw new DaoException("There is no such events in "+categoryName+" category");
		return eventsInCategory;
	}

}