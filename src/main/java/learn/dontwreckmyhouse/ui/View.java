package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;

@Component
public class View {
    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }
        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public Host selectHost(List<Host> hosts) {
        String hostEmail = io.readEmail("Enter Host Email: ");
        for (Host host : hosts) {
            if (host.getHostEmail().equalsIgnoreCase(hostEmail)) {
                return host;
            }
        }
        displayStatus(false, String.format("Sorry, host with email %s does not exist.", hostEmail));
        return null;
    }

    public Guest selectGuest(List<Guest> guests) {
        String guestEmail = io.readEmail("Enter Guest Email: ");
        for (Guest guest : guests) {
            if (guest.getGuestEmail().equalsIgnoreCase(guestEmail)) {
                return guest;
            }
        }
        displayStatus(false, String.format("Sorry, guest with email %s does not exist.", guestEmail));
        return null;
    }

    public Reservation makeReservation(Host host, Guest guest, ReservationService reservationService) {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        LocalDate start = io.readLocalDate("Start (MM/DD/YYYY): ");
        LocalDate end = io.readLocalDate("End (MM/DD/YYYY): ");
        reservation.setStartDate(start);
        reservation.setEndDate(end);

        BigDecimal total = reservationService.calculateTotal(start, end, host);
        reservation.setTotal(total);

        return reservation;
    }

    public void displayAllReservations(List<Reservation> reservations, Host host) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }

        displayHeader(host.getLastName() + ": " + host.getCity() + ", " + host.getState());

        reservations.sort(Comparator.comparing(Reservation::getStartDate));

        io.printf("%-5s %-12s %-12s %-18s %-18s %-30s %-10s%n",
                "ID", "Start Date", "End Date", "Guest First Name", "Guest Last Name", "Guest Email", "Total");

        for (Reservation reservation : reservations) {
            io.printf("%-5s %-12s %-12s %-18s %-18s %-30s $%.2f%n",
                    reservation.getReservationId(),
                    reservation.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    reservation.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getGuestEmail(),
                    reservation.getTotal());
        }
    }

    public boolean confirmReservationUpdate() {
        return io.readBoolean("Are you sure you want to update this reservation? [y/n]: ");
    }

    public void editReservation(Reservation reservation) {
        displayHeader(String.format("Editing Reservation %s%n", reservation.getReservationId()));
        reservation.setStartDate(io.readLocalDate(String.format("Start (%s): ",
                reservation.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))));
        reservation.setEndDate(io.readLocalDate(String.format("End (%s): ",
                reservation.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))));
    }

    public boolean isOkay() {
        return io.readBoolean("Is this okay? [y/n]: ");
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public void displayMessage(String message) {
        io.println("");
        io.println(message);
    }

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public int getReservationIdToDelete() {
        return io.readInt("Enter the ID of the reservation to delete: ");
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayReservationSummary(Reservation reservation) {
        displayHeader("Summary");
        io.printf("Start Date: %s%n", reservation.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        io.printf("End Date: %s%n", reservation.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        io.printf("Total: $%.2f%n", reservation.getTotal());
    }

    public int getReservationIdToUpdate() {
        return io.readInt("Enter the ID of the reservation to update: ");
    }

    public boolean confirmReservationDeletion() {
        return io.readBoolean("Are you sure you want to delete this reservation? (y/n): ");
    }

}
