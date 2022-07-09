package ajbc.doodle.calendar.controller;

import ajbc.doodle.calendar.dao.DaoException;
import ajbc.doodle.calendar.entities.ErrorMessage;
import ajbc.doodle.calendar.entities.Event;
import ajbc.doodle.calendar.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/events")
@RestController
public class EventController {

	@Autowired
	private EventService eventService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Event>> getAllEvents() throws DaoException {
		List<Event> events = eventService.getAllEvents();
		return ResponseEntity.ok(events);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/{userId}")
	public ResponseEntity<?> addEvent(@RequestBody Event event, @PathVariable Integer userId) {
		try {
			eventService.addEvent(event,userId);
			event = eventService.getEventById(event.getEventId());
			return ResponseEntity.status(HttpStatus.CREATED).body(event);
			
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("Failed to add event to database");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateEvent(@RequestBody Event event, @RequestParam Integer userId ,@RequestParam Integer eventId) {
		try {
			if(eventService.checkIfUserIsTheOwner(eventId,userId)==false)
				throw new DaoException("Only the event owner can edit event.");
			event.setEventId(eventId);
			eventService.updateEvent(event);
			event = eventService.getEventById(eventId);
			return ResponseEntity.status(HttpStatus.OK).body(event);
			
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("Failed to update event in database");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMsg);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/byId/{id}")
	public ResponseEntity<?> getEventById(@PathVariable Integer id) {
		try {
			Event event = eventService.getEventById(id);
			return ResponseEntity.ok(event);
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("Failed to get event by this Id");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
		}

	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/byUserId/{userId}")
	public ResponseEntity<?> getUserEvents(@PathVariable Integer userId) {
		try {
			List<Event> allUserEvents = eventService.getUserEvents(userId);
			return ResponseEntity.ok(allUserEvents);
			
		} catch (DaoException e) {
			ErrorMessage errorMsg = new ErrorMessage();
			errorMsg.setData(e.getMessage());
			errorMsg.setMessage("Failed to get user events");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMsg);
			//TODO fix null list return
		}

	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/byCategoryId/{categoryId}")
	public ResponseEntity<?> getEventsByCategoryId(@PathVariable Integer categoryId) {
		
		List<Event> eventsOfCategory;
		try {
			eventsOfCategory = eventService.getEventsByCategoryId(categoryId);
			return ResponseEntity.ok(eventsOfCategory);
			
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("Failed to get events By this category id");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
			//TODO fix null list return
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/byCategoryName/{categoryName}")
	public ResponseEntity<?> getEventsByCategoryName(@PathVariable String categoryName) {
		
		List<Event> eventsOfCategory;
		try {
			eventsOfCategory = eventService.getEventsByCategoryName(categoryName.toUpperCase());
			return ResponseEntity.ok(eventsOfCategory);
			
		} catch (DaoException e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setData(e.getMessage());
			errorMessage.setMessage("Failed to get events By this category");
			return ResponseEntity.status(HttpStatus.valueOf(500)).body(errorMessage);
			//TODO fix null list return
		}
	}
	
}