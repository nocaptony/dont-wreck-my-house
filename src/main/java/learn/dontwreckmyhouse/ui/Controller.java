package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataAccessException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Controller {
    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService hostService,
                      ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House!");
        try {
            runAppLoop();
        } catch (DataAccessException ex ) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataAccessException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewReservationsForHost();
                    break;
                case CREATE_RESERVATION:
                    addReservation();
                    break;
                case EDIT_RESERVATION:
                    updateReservation();
                    break;
                case DELETE_RESERVATION:
                    deleteReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsForHost() throws DataAccessException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());
        Host host = getHost();
        if (host == null) {
            return;
        }
        List<Reservation> reservations = reservationService.findReservationByHostId(host.getHostId());
        if (reservations == null || reservations.isEmpty()) {
            view.displayStatus(false, String.format("No reservations found for host with email %s.", host.getHostEmail()));
        } else {
            view.displayAllReservations(reservations, host);
        }
    }

    private void addReservation() throws DataAccessException {
        view.displayHeader(MainMenuOption.CREATE_RESERVATION.getMessage());
        Host host = getHost();
        if (host == null) {
            return;
        }
        Guest guest = getGuest();
        if (guest == null) {
            return;
        }
        List<Reservation> reservations = reservationService.findReservationByHostId(host.getHostId());

        view.displayAllReservations(reservations, host);
        Reservation reservation = view.makeReservation(host, guest, reservationService);
        view.displayReservationSummary(reservation);
        if (view.isOkay()) {
            Result<Reservation> result = reservationService.add(reservation);
            if (result.isSuccess()) {
                String successMessage = String.format("Reservation %s created.", result.getPayload().getReservationId());
                view.displayStatus(true, successMessage);
            } else {
                view.displayStatus(false, result.getErrorMessages());
            }
        }
    }

    private void updateReservation() throws DataAccessException {
        Host host = getHost();
        List<Reservation> reservations = null;
        if (host != null) {
            reservations = reservationService.findReservationByHostId(host.getHostId());
        }

        if (host == null || reservations == null || reservations.isEmpty()) {
            if (host == null) {
                view.displayMessage("Host does not exist in the database. Please try again.");
            } else {
                view.displayMessage("No reservations found.");
            }
            return;
        }

        view.displayAllReservations(reservations, host);
        int reservationIdToUpdate = view.getReservationIdToUpdate();
        Reservation reservationToUpdate = findReservationById(reservations, reservationIdToUpdate);
        if (reservationToUpdate == null) {
            view.displayMessage("Invalid reservation ID.");
            return;
        }

        view.displayReservationSummary(reservationToUpdate);
        boolean update = view.confirmReservationUpdate();
        if (update) {
            view.editReservation(reservationToUpdate);
            BigDecimal updatedTotal = reservationService.calculateTotal(reservationToUpdate.getStartDate(),
                    reservationToUpdate.getEndDate(), host);
            reservationToUpdate.setTotal(updatedTotal);
            Result<Reservation> result = reservationService.update(reservationToUpdate);
            if (result.isSuccess()) {
                view.displayStatus(true, String.format("Reservation ID: %s has been updated. New total: $%.2f.",
                        reservationToUpdate.getReservationId(), updatedTotal));
            } else {
                view.displayStatus(false, result.getErrorMessages());
            }
        } else {
            view.displayMessage("Reservation update cancelled.");
        }
    }

    private void deleteReservation() throws DataAccessException {
        view.displayHeader(MainMenuOption.DELETE_RESERVATION.getMessage());

        Host host = getHost();
        List<Reservation> reservations = null;
        if (host != null) {
            reservations = reservationService.findReservationByHostId(host.getHostId());
        }

        if (host == null || reservations == null || reservations.isEmpty()) {
            if (host == null) {
                view.displayMessage("Host does not exist in the database. Please try again.");
            } else {
                view.displayMessage("No reservations found.");
            }
            return;
        }

        view.displayAllReservations(reservations, host);
        int reservationIdToDelete = view.getReservationIdToDelete();
        Reservation reservationToDelete = findReservationById(reservations, reservationIdToDelete);
        if (reservationToDelete == null) {
            view.displayMessage("Invalid reservation ID.");
            return;
        }

        boolean delete = view.confirmReservationDeletion();
        if (delete) {
            Result<Reservation> result = reservationService.delete(reservationToDelete);
            if (result.isSuccess()) {
                view.displayStatus(true, String.format("Reservation ID: %s has been deleted.", reservationToDelete.getReservationId()));
            } else {
                view.displayStatus(false, result.getErrorMessages());
            }
        } else {
            view.displayMessage("Reservation deletion cancelled.");
        }
    }

    private BigDecimal calculateTotal(Reservation reservation, Host host) {
        return reservationService.calculateTotal(reservation.getStartDate(),
                reservation.getEndDate(), reservation.getHost());
    }

    private Host getHost() throws DataAccessException {
        List<Host> allHosts = hostService.findAll();
        return view.selectHost(allHosts);
    }

    private Guest getGuest() throws DataAccessException {
        List<Guest> allGuests = guestService.findAll();
        return view.selectGuest(allGuests);
    }

    private Reservation findReservationById(List<Reservation> reservations, int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }

}
