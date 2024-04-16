package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {
    private final String filePath;
    public HostFileRepository(@Value("./data/hosts.csv") String filePath) {
        this.filePath = filePath;
    }
    @Override
    public List<Host> findAll() throws DataAccessException {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            //
        }
        return result;
    }

    @Override
    public Host findById(String id) throws DataAccessException {
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getHostId().equalsIgnoreCase(id)) {
                return host;
            }
        }
        return null;
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getHostEmail().equalsIgnoreCase(email)) {
                return host;
            }
        }
        return null;
    }

    private Host deserialize(String[] fields) {
        Host host = new Host();
        host.setHostId(fields[0]);
        host.setLastName(fields[1]);
        host.setHostEmail(fields[2]);
        host.setPhoneNumber(fields[3]);
        host.setAddress(fields[4]);
        host.setCity(fields[5]);
        host.setState(fields[6]);
        host.setPostalCode((fields[7]));
        host.setStandardRate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        host.setWeekendRate(BigDecimal.valueOf(Double.parseDouble(fields[9])));
        return host;
    }
}
