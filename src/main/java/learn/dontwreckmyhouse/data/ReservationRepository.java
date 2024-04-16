package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findReservationsByHostId(String hostId) throws DataAccessException;

    Reservation findReservation(String hostId, int reservationId) throws DataAccessException;

    Reservation add(Reservation reservation) throws DataAccessException;

    boolean update(Reservation reservation) throws DataAccessException;

    boolean delete(Reservation reservation) throws DataAccessException;
}
