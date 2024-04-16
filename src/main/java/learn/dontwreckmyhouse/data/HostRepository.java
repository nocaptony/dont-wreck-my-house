package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll() throws DataAccessException;
    Host findById(String id) throws DataAccessException;
    Host findByEmail(String email) throws DataAccessException;
}
