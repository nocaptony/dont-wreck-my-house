package learn.dontwreckmyhouse.ui;

public enum MainMenuOption {
    EXIT(0, "Exit"),
    VIEW_RESERVATIONS_FOR_HOST(1, "View Reservations for Host"),
    CREATE_RESERVATION(2, "Make a Reservation"),
    EDIT_RESERVATION(3, "Edit a Reservation"),
    DELETE_RESERVATION(4, "Cancel a Reservation");

    private final int value;
    private final String message;

    MainMenuOption(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
