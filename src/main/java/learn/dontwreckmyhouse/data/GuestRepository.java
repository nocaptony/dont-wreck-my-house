package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll() throws DataAccessException;
    Guest findById(int id) throws DataAccessException;
    Guest findByEmail(String email) throws DataAccessException;

}
