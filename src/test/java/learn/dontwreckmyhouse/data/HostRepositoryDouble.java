package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {
    //id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate
    private final ArrayList<Host> hosts = new ArrayList<>();
    public HostRepositoryDouble() {
        hosts.addAll(makeHosts());
    }
    @Override
    public List<Host> findAll() throws DataAccessException {
        return new ArrayList<>(hosts);
    }

    @Override
    public Host findById(String id) throws DataAccessException {
        List<Host> allHosts = findAll();
        for (Host host : allHosts) {
            if (host.getHostId().equals(id)) {
                return host;
            }
        }
        return null;
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        List<Host> allHosts = findAll();
        for (Host host : allHosts) {
            if (host.getHostEmail().equals(email)) {
                return host;
            }
        }
        return null;
    }

    private static List<Host> makeHosts() {
        Host host1 = new Host("7e4b1d91-ec1a-42c3-b6bc-2c0d4bba9b7d", "Test",
                "test@yahoo.com", "(601) 333 3212", "12 Tester Road", "Boulder",
                "Colorado", "88891", BigDecimal.valueOf(1200), BigDecimal.valueOf(2400));

        Host host2 = new Host("d2b38178-4cbb-4349-ae1c-8a7652bdcf23", "McTest",
                "mctest@gmail.com", "(432) 354 6542", "1 Testing Street", "Canton",
                "Michigan", "112233", BigDecimal.valueOf(600), BigDecimal.valueOf(800));

        Host host3 = new Host("f861e1e2-7824-4c33-aa02-f1262dcf5bf7", "The-Tester",
                "thetester@aol.com", "(231) 435 6341", "17 Quiz Way", "Bangor",
                "Maine", "53467", BigDecimal.valueOf(200), BigDecimal.valueOf(250));

        ArrayList<Host> hosts = new ArrayList<>();
        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);

        return hosts;
    }
}
