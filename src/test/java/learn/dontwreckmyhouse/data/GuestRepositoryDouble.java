package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {
    //guest_id,first_name,last_name,email,phone,state
    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        guests.addAll(makeGuests());
    }

    @Override
    public List<Guest> findAll() throws DataAccessException {
        return new ArrayList<>(guests);
    }

    @Override
    public Guest findById(int id) throws DataAccessException {
        List<Guest> allGuests = findAll();
        for (Guest guest : allGuests) {
            if (guest.getGuestId() == id) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest findByEmail(String email) throws DataAccessException {
        List<Guest> allGuests = findAll();
        for (Guest guest : allGuests) {
            if (guest.getGuestEmail().equalsIgnoreCase(email)) {
                return guest;
            }
        }
        return null;
    }

    private static List<Guest> makeGuests() {
        Guest guest1 = new Guest(1, "Test",
                "McTest", "testmail@yahoo.com",
                "(921) 9239394", "MA");

        Guest guest2 = new Guest(2, "McTest",
                "Tester", "mctesttest2@gmail.com",
                "(211) 2825242", "VA");

        Guest guest3 = new Guest(3, "Testy",
                "The-Tester", "the-tester@outlook.com",
                "(515) 5535547", "CO");

        ArrayList<Guest> guests = new ArrayList<>();
        guests.add(guest1);
        guests.add(guest2);
        guests.add(guest3);

        return guests;
    }
}
