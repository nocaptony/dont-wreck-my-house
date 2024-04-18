package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataAccessException;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findReservationByHost(Host host) throws DataAccessException {
        return reservationRepository.findReservationsByHost(host);
    }

    public Reservation findReservationById(Host host, int reservationId) throws DataAccessException {
        return reservationRepository.findReservation(host, reservationId);
    }

    public Result<Reservation> add(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.add(reservation));
        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);

        if (!result.isSuccess()) {
            return result;
        }

        reservationRepository.update(reservation);
        return result;
    }

    public Result<Reservation> delete(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);

        if (!result.isSuccess()) {
            return result;
        }

        reservationRepository.delete(reservation);
        return result;
    }

    public BigDecimal calculateTotal(LocalDate start, LocalDate end, Host host) {
        if (start == null || end == null || host == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal weekDays = getWeekDays(start, end);
        BigDecimal weekEndDays = getWeekendDays(start, end);

        BigDecimal weekDayTotal = weekDays.multiply(host.getStandardRate());
        BigDecimal weekEndTotal = weekEndDays.multiply(host.getWeekendRate());

        return weekDayTotal.add(weekEndTotal).setScale(2, RoundingMode.HALF_UP);

    }

    private BigDecimal getWeekDays(LocalDate startDate, LocalDate endDate) {
        if (isEndDateBeforeStartDate(startDate, endDate)) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal weekdaysCount = BigDecimal.ZERO;
            LocalDate tempDate = startDate;
            while (tempDate.isBefore(endDate)) {
                int dayOfWeekValue = tempDate.getDayOfWeek().getValue();
                if (dayOfWeekValue >= 1 && dayOfWeekValue <= 5) {
                    weekdaysCount = weekdaysCount.add(BigDecimal.ONE);
                }
                tempDate = tempDate.plusDays(1);
            }
            return weekdaysCount;
        }
    }

    private BigDecimal getWeekendDays(LocalDate startDate, LocalDate endDate) {
        if (isEndDateBeforeStartDate(startDate, endDate)) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal weekendDays = BigDecimal.ZERO;
            LocalDate tempDate = startDate;
            while (tempDate.isBefore(endDate)) {
                int dayOfWeekValue = tempDate.getDayOfWeek().getValue();
                if (dayOfWeekValue == 6 || dayOfWeekValue == 7) {
                    weekendDays = weekendDays.add(BigDecimal.ONE);
                }
                tempDate = tempDate.plusDays(1);
            }
            return weekendDays;
        }
    }

    private Result<Reservation> validate(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Cannot find reservation.");
            return result;
        }

        validateBasicReservationDetails(reservation, result);

        LocalDate now = LocalDate.now();
        if (reservation.getStartDate() != null && reservation.getEndDate() != null) {
            if (reservation.getStartDate().isBefore(now) || reservation.getEndDate().isBefore(now)) {
                result.addErrorMessage("Reservation dates must be in the future.");
            }
            if (isEndDateBeforeStartDate(reservation.getStartDate(), reservation.getEndDate())) {
                result.addErrorMessage("Reservation start date must be before end date.");
            }
        }

        if (reservation.getReservationId() > 0) {
            Reservation existingReservation = reservationRepository.findReservation(reservation.getHost(), reservation.getReservationId());
            if (existingReservation != null && existingReservation.getStartDate().isBefore(now)) {
                result.addErrorMessage("Cannot delete past reservations.");
            }
        }

        validateDoesNotOverlap(reservation, result);

        return result;
    }

    private boolean isEndDateBeforeStartDate(LocalDate startDate, LocalDate endDate) {
        return endDate.isBefore(startDate) || endDate.equals(startDate);
    }

    private void validateBasicReservationDetails(Reservation reservation, Result<Reservation> result) {
        if (reservation.getGuest() == null) {
            result.addErrorMessage("Reservation guest is required.");
        }
        if (reservation.getHost() == null) {
            result.addErrorMessage("Reservation host is required.");
        }
        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Reservation start date is required.");
        }
        if (reservation.getEndDate() == null) {
            result.addErrorMessage("Reservation end date is required.");
        }
    }

    private void validateDoesNotOverlap(Reservation reservation, Result<Reservation> result) throws DataAccessException {
        if (reservation.getHost() != null) {
            List<Reservation> reservations = findReservationByHost(reservation.getHost());
            LocalDate startDate = reservation.getStartDate();
            LocalDate endDate = reservation.getEndDate();
            if (startDate != null && endDate != null) {
                for (Reservation r : reservations) {
                    if ((startDate.isBefore(r.getEndDate())) &&
                            (r.getStartDate().isBefore(endDate)) &&
                            reservation.getReservationId() != r.getReservationId()) {
                        result.addErrorMessage("Reservation cannot overlap existing reservation.");
                        break;
                    }
                }
            }
        }
    }

}
