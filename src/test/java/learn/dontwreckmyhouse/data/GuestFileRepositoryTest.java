package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class GuestFileRepositoryTest {
    static final String SEED_FILE_PATH = "data/test-data/guests-seed.csv";
    static final String TEST_FILE_PATH = "data/test-data/guests-test.csv";
    GuestFileRepository repository = new GuestFileRepository(SEED_FILE_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(
                Paths.get(SEED_FILE_PATH),
                Paths.get(TEST_FILE_PATH),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() throws DataAccessException {
        List<Guest> all = repository.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldFindById() throws DataAccessException {
        Guest guest = repository.findById(2);
        assertEquals("Olympie", guest.getFirstName());
        assertEquals("Gecks", guest.getLastName());
    }

    @Test
    void shouldNotFindNonExistingId() throws DataAccessException {
        Guest guest = repository.findById(9999);
        assertNull(guest);
    }

    @Test
    void shouldFindByEmail() throws DataAccessException {
        Guest guest = repository.findByEmail("dpepona@europa.eu");
        assertEquals("Devina", guest.getFirstName());
        assertEquals("Pepon", guest.getLastName());
    }

    @Test
    void shouldNotFindNonExistingEmail() throws DataAccessException {
        Guest guest = repository.findByEmail("testnametestname12@yahoo.com");
        assertNull(guest);
    }
}