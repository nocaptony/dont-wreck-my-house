package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import learn.dontwreckmyhouse.models.Reservation;

import java.time.LocalDate;

class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH =  "./data/test-data/test-reservations/cc136272-2816-416f-95b8-7122c4856b12-seed.csv";
    static final String TEST_FILE_PATH =  "./data/test-data/test-reservations/cc136272-2816-416f-95b8-7122c4856b12.csv";
    static final String TEST_DIRECTORY_PATH = "./data/test-data/test-reservations";

    private final GuestFileRepository guestRepository = new GuestFileRepository("./data/test-data/guests-test.csv");
    private final HostFileRepository hostRepository = new HostFileRepository("./data/test-data/hosts-test.csv");
    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIRECTORY_PATH, guestRepository, hostRepository);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findReservationsByHostId() throws DataAccessException {
        List<Reservation> actual = repository.findReservationsByHostId("cc136272-2816-416f-95b8-7122c4856b12");
        assertNotNull(actual);
        assertEquals(11, actual.size());
    }

    @Test
    void shouldNotFindNonExistentHostIdReservations() throws DataAccessException {
        List<Reservation> actual = repository.findReservationsByHostId("thisisatestid");
        assertEquals(0, actual.size());
    }

    @Test
    void findReservation() throws DataAccessException {
        Reservation actual = repository.findReservation("cc136272-2816-416f-95b8-7122c4856b12", 1);
        assertNotNull(actual);
        assertEquals(LocalDate.of(2021, 5,23), actual.getEndDate());
    }

    @Test
    void shouldNotFindNonExistentReservation() throws DataAccessException {
        Reservation actual = repository.findReservation("cc136272-2816-416f-95b8-7122c4856b12", 999);
        assertNull(actual);
    }

    @Test
    void shouldAddReservation() throws DataAccessException {
        Guest guest = guestRepository.findById(1);
        Host host = hostRepository.findById("cc136272-2816-416f-95b8-7122c4856b12");
        Reservation reservation = new Reservation(guest, host);
        reservation.setStartDate(LocalDate.of(2024, 4, 11));
        reservation.setEndDate(LocalDate.of(2024, 4, 18));
        reservation.setTotal(BigDecimal.valueOf(1000));
        assertNotNull(repository.add(reservation));
        assertEquals(12, repository.findReservationsByHostId("cc136272-2816-416f-95b8-7122c4856b12").size());
    }

    @Test
    void shouldUpdateReservation() throws DataAccessException {
        Reservation reservation = repository.findReservation("cc136272-2816-416f-95b8-7122c4856b12", 1);
        reservation.setEndDate(LocalDate.of(2021,5,30));
        assertTrue(repository.update(reservation));
    }

    @Test
    void shouldNotUpdateNonExistingReservation() throws DataAccessException {
        Host host = hostRepository.findById("cc136272-2816-416f-95b8-7122c4856b12");
        Guest guest = guestRepository.findById(9);
        Reservation reservation = new Reservation();

        reservation.setReservationId(999);
        reservation.setStartDate(LocalDate.of(2024, 4, 10));
        reservation.setEndDate(LocalDate.of(2024, 4, 17));
        reservation.setGuest(guest);
        reservation.setHost(host);
        boolean success = repository.update(reservation);
        assertFalse(success);
    }

    @Test
    void shouldDeleteReservation() throws DataAccessException {
        Reservation reservation = repository.findReservation("cc136272-2816-416f-95b8-7122c4856b12", 1);
        assertTrue(repository.delete(reservation));
        assertEquals(10, repository.findReservationsByHostId("cc136272-2816-416f-95b8-7122c4856b12").size());
    }

    @Test
    void shouldNotDeleteNonExistingReservation() throws DataAccessException {
        Host host = hostRepository.findById("cc136272-2816-416f-95b8-7122c4856b12");
        Guest guest = guestRepository.findById(9);
        Reservation reservation = new Reservation();

        reservation.setReservationId(999);
        reservation.setStartDate(LocalDate.of(2024, 4, 10));
        reservation.setEndDate(LocalDate.of(2024, 4, 17));
        reservation.setGuest(guest);
        reservation.setHost(host);
        boolean success = repository.delete(reservation);
        assertFalse(success);
    }

}