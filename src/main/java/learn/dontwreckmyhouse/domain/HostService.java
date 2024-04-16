package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataAccessException;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {
    private final HostRepository hostRepository;
    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }
    public List<Host> findAll() throws DataAccessException {
        return hostRepository.findAll();
    }
    public Host findById(String id) throws DataAccessException {
        return hostRepository.findById(id);
    }
    public Host findByEmail(String email) throws DataAccessException {
        return hostRepository.findByEmail(email);
    }
}
