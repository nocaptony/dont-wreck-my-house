package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataAccessException;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {
    HostService service;
    @BeforeEach
    void setup() {
        HostRepositoryDouble hostRepositoryDouble = new HostRepositoryDouble();
        service = new HostService(hostRepositoryDouble);
    }

    @Test
    void shouldFindAllHosts() throws DataAccessException {
        assertNotNull(service.findAll());
        assertEquals(3, service.findAll().size());
    }

    @Test
    void shouldFindById() throws DataAccessException {
        assertNotNull(service.findById("d2b38178-4cbb-4349-ae1c-8a7652bdcf23"));
        assertEquals("McTest", service.findById("d2b38178-4cbb-4349-ae1c-8a7652bdcf23").getLastName());
    }

    @Test
    void shouldNotFindNonExistingId() throws DataAccessException {
        assertNull(service.findById("thisisatestidthatshouldbenull"));
    }

    @Test
    void shouldFindByEmail() throws DataAccessException {
        assertNotNull(service.findByEmail("thetester@aol.com"));
        assertEquals("The-Tester", service.findByEmail("thetester@aol.com").getLastName());
    }

    @Test
    void shouldNotFindNonExistingEmail() throws DataAccessException {
        assertNull(service.findByEmail("thisisatestemailthatshouldbenull"));
    }

}