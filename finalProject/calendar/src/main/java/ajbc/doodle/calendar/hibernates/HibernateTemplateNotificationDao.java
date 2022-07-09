package ajbc.doodle.calendar.hibernates;

import ajbc.doodle.calendar.dao.DaoException;
import ajbc.doodle.calendar.dao.NotificationDao;
import ajbc.doodle.calendar.entities.Notification;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "HNT_notification")
@SuppressWarnings("unchecked")
public class HibernateTemplateNotificationDao implements NotificationDao {

    @Autowired
    private HibernateTemplate template;


    @Override
    public List<Notification> getAllNotifications() throws DaoException {
        DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);

        DetachedCriteria resultTransformer = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Notification> resultList = (List<Notification>) template.findByCriteria(resultTransformer);
        return resultList;
    }

    @Override
    public void addNotification(Notification notification) throws DaoException {
        template.persist(notification);
    }

    @Override
    public void updateNotification(Notification notification) throws DaoException {
        template.merge(notification);
    }


    @Override
    public Notification getNotificationById(Integer notificationId) throws DaoException {
        Notification notification = template.get(Notification.class, notificationId);
        if (notification == null)
            throw new DaoException("There is no such notification in 'notifications' DB with id: ");
        return notification;
    }

}