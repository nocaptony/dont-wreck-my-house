package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findReservationsByHost(Host host) throws DataAccessException;

    Reservation findReservation(Host host, int reservationId) throws DataAccessException;

    Reservation add(Reservation reservation) throws DataAccessException;

    boolean update(Reservation reservation) throws DataAccessException;

    boolean delete(Reservation reservation) throws DataAccessException;
}
