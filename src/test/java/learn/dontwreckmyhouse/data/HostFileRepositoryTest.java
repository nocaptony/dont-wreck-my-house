package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {
    static final String SEED_FILE_PATH = "data/test-data/hosts-seed.csv";
    static final String TEST_FILE_PATH = "data/test-data/hosts-test.csv";
    HostFileRepository repository = new HostFileRepository(SEED_FILE_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(
                Paths.get(SEED_FILE_PATH),
                Paths.get(TEST_FILE_PATH),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() throws DataAccessException {
        List<Host> all = repository.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldFindByEmail() throws DataAccessException {
        Host host = repository.findByEmail("eyearnes0@sfgate.com");
        System.out.println(host.getHostId());
    }

    @Test
    void shouldNotFindNonExistingEmail() throws DataAccessException {
        Host host = repository.findByEmail("thisemaildoesnotexist@gmail.com");
        assertNull(host);
    }
}