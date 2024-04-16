package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;
    private final GuestFileRepository guestRepository;
    private final HostFileRepository hostRepository;

    public ReservationFileRepository(@Value("./data/reservations") String directory,
                                     GuestFileRepository guestRepository, HostFileRepository hostRepository) {
        this.directory = directory;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    @Override
    public List<Reservation> findReservationsByHostId(String hostId) throws DataAccessException {
        ArrayList<Reservation> result = new ArrayList<>();
        String filePath = Paths.get(directory, hostId + ".csv").toString();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, hostId));
                }
            }
        } catch (IOException ex) {
            //
        }
        return result;
    }

    @Override
    public Reservation findReservation(String hostId, int reservationId) throws DataAccessException {
        List<Reservation> all = findReservationsByHostId(hostId);
        for (Reservation reservation : all) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataAccessException {
        String hostId = reservation.getHost().getHostId();
        List<Reservation> all = findReservationsByHostId(hostId);
        int nextId = 1;
        for (Reservation existingReservation : all) {
            nextId = Math.max(nextId, existingReservation.getReservationId() + 1);
        }
        reservation.setReservationId(nextId);
        all.add(reservation);
        writeToFile(all, hostId);

        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataAccessException {
        List<Reservation> all = findReservationsByHostId(reservation.getHost().getHostId());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservation.getReservationId()) {
                all.set(i, reservation);
                writeToFile(all, reservation.getHost().getHostId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataAccessException {
        List<Reservation> all = findReservationsByHostId(reservation.getHost().getHostId());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == (reservation.getReservationId())) {
                all.remove(i);
                writeToFile(all, reservation.getHost().getHostId());
                return true;
            }
        }
        return false;
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%d,%.2f",
                reservation.getReservationId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getGuestId(),
                reservation.getTotal().doubleValue());
    }

    private Reservation deserialize(String[] fields, String hostId) throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(Integer.parseInt(fields[0]));
        reservation.setStartDate(LocalDate.parse(fields[1]));
        reservation.setEndDate(LocalDate.parse(fields[2]));
        reservation.setGuest(guestRepository.findById(Integer.parseInt(fields[3])));
        reservation.setHost(hostRepository.findById(hostId));
        reservation.setTotal(BigDecimal.valueOf(Double.parseDouble(fields[4])));
        return reservation;
    }

    private void writeToFile(List<Reservation> reservations, String hostId) {
        String filePathString = Paths.get(directory, hostId + ".csv").toString();
        Path filePath = Paths.get(filePathString);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println(HEADER);
            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (IOException ex) {
            //
        }
    }
}
