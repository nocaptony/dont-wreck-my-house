package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {
    public final static Guest GUEST = makeGuest();
    public final static Host HOST = makeHost();
    public final static Reservation RESERVATION = makeReservation();
    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        reservations.add(RESERVATION);
    }
    @Override
    public List<Reservation> findReservationsByHost(Host host) throws DataAccessException {
        List<Reservation> all = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getHost().getHostId().equals(host.getHostId())) {
                all.add(reservation);
            }
        }
        return all;
    }

    @Override
    public Reservation findReservation(Host host, int reservationId) throws DataAccessException {
        List<Reservation> reservationsByHostId = findReservationsByHost(host);
        for (Reservation reservation : reservationsByHostId) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataAccessException {
        List<Reservation> all = findReservationsByHost(reservation.getHost());

        int maxReservationId = 0;
        for (Reservation r : all) {
            int reservationId = r.getReservationId();
            if (reservationId > maxReservationId) {
                maxReservationId = reservationId;
            }
        }

        int nextId = maxReservationId + 1;
        reservation.setReservationId(nextId);

        all.add(reservation);

        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataAccessException {
        return findReservation(reservation.getHost(), reservation.getReservationId()) != null;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataAccessException {
        return reservations.remove(reservation);
    }

    private static Guest makeGuest() {
        return new Guest(2, "McTest",
                "Tester", "mctesttest2@gmail.com",
                "(211) 2825242", "VA");
    }

    private static Host makeHost() {
        return new Host("7e4b1d91-ec1a-42c3-b6bc-2c0d4bba9b7d", "Test",
                "test@yahoo.com", "(601) 333 3212", "12 Tester Road", "Boulder",
                "Colorado", "88891", BigDecimal.valueOf(1200), BigDecimal.valueOf(1600));
    }

    private static Reservation makeReservation() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(5));
        reservation.setTotal(new BigDecimal(100));
        reservation.setGuest(makeGuest());
        reservation.setHost(makeHost());

        return reservation;
    }
}
