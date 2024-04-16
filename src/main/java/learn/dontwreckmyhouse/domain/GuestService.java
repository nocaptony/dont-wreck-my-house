package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataAccessException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.models.Guest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    private final GuestRepository guestRepository;
    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }
    public List<Guest> findAll() throws DataAccessException {
        return guestRepository.findAll();
    }
    public Guest findById(int id) throws DataAccessException {
        return guestRepository.findById(id);
    }
    public Guest findByEmail(String email) throws DataAccessException {
        return guestRepository.findByEmail(email);
    }

}
