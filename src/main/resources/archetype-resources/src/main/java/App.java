private void updateReservation() throws DataAccessException {
    Host host = getHost();
    List<Reservation> reservations = null;
    if (host != null) {
        reservations = reservationService.findReservationByHost(host);
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