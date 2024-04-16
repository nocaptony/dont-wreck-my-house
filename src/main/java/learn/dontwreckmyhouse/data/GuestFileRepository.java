package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {
    private final String filePath;
    public GuestFileRepository(@Value("./data/guests.csv") String filePath) {
        this.filePath = filePath;
    }
    @Override
    public List<Guest> findAll() throws DataAccessException {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            //
        }
        return result;
    }

    @Override
    public Guest findById(int id) throws DataAccessException {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getGuestId() == id) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest findByEmail(String email) throws DataAccessException {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getGuestEmail().equalsIgnoreCase(email)) {
                return guest;
            }
        }
        return null;
    }

    private Guest deserialize(String[] fields) {
        Guest guest = new Guest();
        guest.setGuestId(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setGuestEmail(fields[3]);
        guest.setPhoneNumber(fields[4]);
        guest.setState(fields[5]);
        return guest;
    }
}
