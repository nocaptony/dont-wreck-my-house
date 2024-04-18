package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataAccessException;
import learn.dontwreckmyhouse.data.ReservationRepositoryDouble;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {
    ReservationService service;
    @BeforeEach
    void setUp() {
        ReservationRepositoryDouble repositoryDouble = new ReservationRepositoryDouble();
        service = new ReservationService(repositoryDouble);
    }

    @Test
    void shouldFindReservationsByHostId() throws DataAccessException {
        assertNotNull(service.findReservationByHost(ReservationRepositoryDouble.HOST));
        assertEquals(1, service.findReservationByHost(ReservationRepositoryDouble.HOST).size());
    }

    @Test
    void shouldNotFindNonExistingHostId() throws DataAccessException {
        Host host = new Host();
        host.setHostId("thisiddoesnotexist");
        assertEquals(0, service.findReservationByHost(host).size());
    }

    @Test
    void shouldFindSingleReservation() throws DataAccessException {
        assertNotNull(service.findReservationById(ReservationRepositoryDouble.HOST, 1));
        assertEquals("mctesttest2@gmail.com",
                service.findReservationById(ReservationRepositoryDouble.HOST,
                        1).getGuest().getGuestEmail());
    }

    @Test
    void shouldNotFindNonExistingSingleReservation() throws DataAccessException {
        Host host = new Host();
        host.setHostId("thisdoesnotexist");
        assertNull(service.findReservationById(host, 5));
    }

    @Test
    void shouldAddReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(50));
        reservation.setEndDate(LocalDate.now().plusDays(55));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddNullGuest() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(50));
        reservation.setEndDate(LocalDate.now().plusDays(55));
        reservation.setGuest(null);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationWithNullHost() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(50));
        reservation.setEndDate(LocalDate.now().plusDays(55));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(null);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationWithNullStartDate() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(null);
        reservation.setEndDate(LocalDate.now().plusDays(55));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationWithNullEndDate() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(50));
        reservation.setEndDate(null);
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddOverlappingReservation() throws DataAccessException {
        /*reservation.setStartDate(LocalDate.now()); // 4/11/2024
        reservation.setEndDate(LocalDate.now().plusDays(5));*/ // 4/16/2024
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1)); // 4/12/2024
        reservation.setEndDate(LocalDate.now().plusDays(3)); // 4/15/2024
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationWithStartDateAfterEndDate() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(55));
        reservation.setEndDate(LocalDate.now().plusDays(50));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationWithStartDateFromThePast() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(55));
        reservation.setEndDate(LocalDate.now().minusDays(50));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddAReservationStartDateOnAnotherEndDate() throws DataAccessException {
        Reservation reservation = new Reservation();
        // reservation.setStartDate(LocalDate.now().plusDays(4)); // this wont work
        reservation.setStartDate(LocalDate.now().plusDays(5)); // Note: reservation in the RPdouble ends here
        reservation.setEndDate(LocalDate.now().plusDays(10));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        service.add(reservation);
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldUpdateReservation() throws DataAccessException {
        LocalDate start = LocalDate.of(2024, 5, 1);
        LocalDate end = LocalDate.of(2024, 5, 8);
        Reservation reservation = service.findReservationById(ReservationRepositoryDouble.HOST, 1);
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        reservation.setTotal(service.calculateTotal(start, end, ReservationRepositoryDouble.HOST));
        Result<Reservation> result = service.update(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNonExistingReservation() throws DataAccessException {
        Reservation reservation = service.findReservationById(ReservationRepositoryDouble.HOST, 999);
        Result<Reservation> result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldDeleteReservation() throws DataAccessException {
        Reservation reservation = service.findReservationById(ReservationRepositoryDouble.HOST, 1);
        Result<Reservation> result = service.delete(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotDeleteNonExistingReservation() throws DataAccessException {
        Reservation reservation = service.findReservationById(ReservationRepositoryDouble.HOST, 999);
        Result<Reservation> result = service.delete(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotDeletePastReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(55));
        reservation.setEndDate(LocalDate.now().minusDays(50));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        Result<Reservation> result = service.delete(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldCalculateStandardRateCorrectly() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setStartDate(LocalDate.of(2024, 4, 22)); // Monday
        reservation.setEndDate(LocalDate.of(2024, 4, 26)); // Friday
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST); // Host Standard rate: 1200 * 4 = 4800
        BigDecimal total = service.calculateTotal(reservation.getStartDate(),
                reservation.getEndDate(), reservation.getHost());

        assertEquals(BigDecimal.valueOf(4800).setScale(2, RoundingMode.HALF_UP), total);
    }

    @Test
    void shouldCalculateWeekendRateCorrectly() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setStartDate(LocalDate.of(2024, 4, 27)); // Saturday
        reservation.setEndDate(LocalDate.of(2024, 4, 29)); // Monday
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST); // Weekend rate: 1600 * 2 = 3200
        BigDecimal total = service.calculateTotal(reservation.getStartDate(),
                reservation.getEndDate(), reservation.getHost());

        assertEquals(BigDecimal.valueOf(3200).setScale(2, RoundingMode.HALF_UP), total);
    }

    @Test
    void shouldCalculateTotalRateCorrectly() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setStartDate(LocalDate.of(2024, 4, 22)); // Monday
        reservation.setEndDate(LocalDate.of(2024, 4, 29)); // Monday
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        // Standard rate: 1200 * 5 = 6000
        // Weekend rate: 1600 * 2 = 3200
        // Total: 9200
        BigDecimal total = service.calculateTotal(reservation.getStartDate(),
                reservation.getEndDate(), reservation.getHost());

        assertEquals(BigDecimal.valueOf(9200).setScale(2, RoundingMode.HALF_UP), total);
    }

}