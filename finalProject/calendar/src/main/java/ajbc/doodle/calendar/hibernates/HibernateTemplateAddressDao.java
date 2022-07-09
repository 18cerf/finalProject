package ajbc.doodle.calendar.hibernates;

import ajbc.doodle.calendar.dao.AddressDao;
import ajbc.doodle.calendar.dao.DaoException;
import ajbc.doodle.calendar.entities.Address;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component(value = "HNT_address")
@SuppressWarnings("unchecked")
public class HibernateTemplateAddressDao implements AddressDao {

	@Autowired
	private HibernateTemplate template;


	@Override
	public List<Address> getAllAddresses() throws DaoException {
		DetachedCriteria criteria = DetachedCriteria.forClass(Address.class);
		return (List<Address>) template.findByCriteria(criteria);
	}
	
	@Override
	public void addAddress(Address address) throws DaoException {
		template.persist(address);
	}
	
	@Override
	public void updateAddress(Address address) throws DaoException {
		template.merge(address);
	}


	@Override
	public Address getAddressById(int addressId) throws DaoException {
		Address address = template.get(Address.class, addressId);
		if (address == null)
			throw new DaoException("There is no such address in 'addresses' DB with id: "+addressId);
		return address;
	}

	@Override
	public List<Address> getAddressByCountry(String country) throws DaoException  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Address.class);
		Criterion criterion = Restrictions.eq("country", country);
		criteria.add(criterion);
		List<Address> addresses = (List<Address>)template.findByCriteria(criteria);
		if(addresses.size() == 0 || addresses == null)
			throw new DaoException("There is no such address in 'addresses' DB at: "+country +" country");
		return addresses;
	}
	
	@Override
	public List<Address> getAddressByCity(String city) throws DaoException  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Address.class);
		Criterion criterion = Restrictions.eq("city", city);
		criteria.add(criterion);
		List<Address> addresses = (List<Address>)template.findByCriteria(criteria);
		if(addresses.size() == 0 || addresses == null)
			throw new DaoException("There is no such address in 'addresses' DB at: "+city +" city");
		return addresses;
	}

}