package ajbc.doodle.calendar.dao;

import ajbc.doodle.calendar.entities.Address;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Transactional(rollbackFor = { DaoException.class }, readOnly = true)
public interface AddressDao {


	public default List<Address> getAllAddresses() throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	@Transactional(readOnly = false)
	public default void addAddress(Address address) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	@Transactional(readOnly = false)
	public default void updateAddress(Address address) throws DaoException {
		throw new DaoException("Method not implemented");
	}


	public default Address getAddressById(int id) throws DaoException {
		throw new DaoException("Method not implemented");
	}

	public default List<Address> getAddressByCountry(String country) throws DaoException {
		throw new DaoException("Method not implemented");
	}
	
	public default List<Address> getAddressByCity(String city) throws DaoException {
		throw new DaoException("Method not implemented");
	}

}