package learn.dontwreckmyhouse.models;

public class Guest {
    private int guestId;
    private String firstName;
    private String lastName;
    private String guestEmail;
    private String phoneNumber;
    private String state;

    public Guest() {}

    public Guest(int guestId, String firstName, String lastName, String guestEmail, String phoneNumber, String state) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.guestEmail = guestEmail;
        this.phoneNumber = phoneNumber;
        this.state = state;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
