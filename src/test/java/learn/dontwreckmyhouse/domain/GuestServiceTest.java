package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataAccessException;
import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {
    GuestService service;
    @BeforeEach
    void setUp() {
        GuestRepositoryDouble guestRepositoryDouble = new GuestRepositoryDouble();
        service = new GuestService(guestRepositoryDouble);
    }

    @Test
    void shouldFindAllGuests() throws DataAccessException {
        assertNotNull(service.findAll());
        assertEquals(3, service.findAll().size());
    }

    @Test
    void shouldFindById() throws DataAccessException {
        assertNotNull(service.findById(1));
        assertEquals("Test", service.findById(1).getFirstName());
        assertEquals("McTest", service.findById(1).getLastName());
    }

    @Test
    void shouldNotFindNonExistingId() throws DataAccessException {
        assertNull(service.findById(999));
    }

    @Test
    void shouldFindByEmail() throws DataAccessException {
        assertNotNull(service.findByEmail("the-tester@outlook.com"));
        assertEquals("Testy", service.findByEmail("the-tester@outlook.com").getFirstName());
        assertEquals("The-Tester", service.findByEmail("the-tester@outlook.com").getLastName());
    }

    @Test
    void shouldNotFindNonExistentEmail() throws DataAccessException {
        assertNull(service.findByEmail("thisisaninvalidemail@aol.com"));
    }

}