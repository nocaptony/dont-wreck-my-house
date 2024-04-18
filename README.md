# Module05-Mastery-Project-Dont-Wreck-My-House
Student Facing Mastery Project for Async Java Program 

Don't Wreck My House Project estimated time completion: 32 hours

TASK PLANNING:

1. Model Creation
Estimated Completion Time: 1 hour
Tasks: Create the Host, Guest, and Reservation models. Host model represents a host entity, Guest model represents a guest entity, and Reservation model represents a reservation entity.

2. Interface Creation
Estimated Completion Time: 1 hour
Tasks: Develop interfaces for HostRepository, GuestRepository, and ReservationRepository.

3. HostFileRepository Class Creation
Estimated Completion Time: 1 hour
Tasks: Implement the HostFileRepository class with overridden methods to perform CRUD operations specific to hosts, develop tests for HostFileRepository class functionality to ensure its correctness and reliability.

4. GuestFileRepository Class Creation
Estimated Completion Time: 1 hour
Tasks: Implement the GuestFileRepository class with overridden methods to perform CRUD operations specific to guests, develop tests for GuestFileRepository class functionality to ensure its correctness and reliability.

5. ReservationFileRepository Class Creation
Estimated Completion Time: 2 hours
Tasks: Implement the ReservationFileRepository class with overridden methods to perform CRUD operations specific to reservations.

6. HostService Class Creation
Estimated Completion Time: 1.5 hours
Tasks: Implement pass-through methods in the HostService class to interact with the HostRepository.

7. HostServiceTest Class Creation
Estimated Completion Time: 45 minutes
Tasks: Develop tests for HostService class functionality to ensure its correctness and reliability.

8. HostRepositoryDouble Class Creation
Estimated Completion Time: 1 hour
Tasks: Implement pass-through methods in the HostRepositoryDouble class to copy behavior of HostFileRepository for testing purposes.

9. HostServiceTest Class Creation
Estimated Completion Time: 45 minutes
Tasks: Develop tests for HostService class functionality to ensure its correctness and reliability.

10. GuestService Class Creation
Estimated Completion Time: 45 minutes
Tasks: Implement pass-through methods in the GuestService class to interact with the GuestRepository.

11. GuestRepositoryDouble Class Creation
Estimated Completion Time: 1 hour
Tasks: Implement pass-through methods in the GuestRepositoryDouble class to copy behavior of GuestFileRepository for testing purposes.

12. GuestServiceTest Class Creation
Estimated Completion Time: 45 minutes
Tasks: Develop tests for GuestService class functionality to ensure its correctness and reliability.

13. ReservationService Class Creation
Estimated Completion Time: 3 hours
Tasks: Implement pass-through methods, CRUD methods, and helper methods in the ReservationService class.

14. ReservationRepositoryDouble Class Creation
Estimated Completion Time: 1.5 hours
Tasks: Implement methods in the ReservationRepositoryDouble class to copy behavior of ReservationFileRepository for testing purposes.

15. ReservationServiceTest Class Creation
Estimated Completion Time: 1.5 hours
Tasks: Develop tests for ReservationService class functionality to ensure its correctness and reliability.

16. ConsoleIO Class Creation
Estimated Completion Time: 1.5 hours
Tasks: Implement methods in the ConsoleIO class for handling console input and output.

17. MainMenuOption Enum Creation
Estimated Completion Time: 45 mins
Tasks: Create enums for main menu options.

18. data.properties && dependency-config.xml File Creation
Estimated Completion Time: 45 mins
Tasks: Create properties file with file paths.

19. Annotation of Files
Estimated Completion Time: 30 mins
Tasks: Annotate components, services, and repositories in the codebase.

20. App Class Creation
Estimated Completion Time: 15 mins
Tasks: Implement the main method with proper Springframework syntax in the App class.

21. View Class Creation
Estimated Completion Time: 4 hours
Tasks: 
- Develop methods for viewing reservations by host in the View class
- Develop methods for making reservations in the View clas 
- Develop methods for editing reservations in the View class 
- Develop methods for deleting reservations in the View class

23. Test Method in Console
Estimated Completion Time: 1 hour
Tasks: Test console methods until they work properly.

24. Controller Class Creation
Estimated Completion Time: 4 hours
Tasks: 
- Implement methods for viewing reservations by host in the Controller class
- Implement methods for making reservations in the Controller class
- Implement methods for editing reservations in the Controller class.
- Implement methods for deleting reservations in the Controller class

25. Test Method in Console
Estimated Completion Time: 1 hour
Tasks: Test console methods until they work properly.



Initial Design:

dontwreckmyhouse/
├─── src/
│   ├─── main/
│   │   ├─── java/
│   │   │   ├── learn/dontwreckmyhouse/
│   │   │   │   ├── model/
│   │   │   │   │   ├── Host.java (attributes, getters, setters)
│   │   │   │   │   ├── Guest.java (attributes, getters, setters)
│   │   │   │   │   └── Reservation.java (attributes, getters, setters)
│   │   │   │   ├── domain/
│   │   │   │   │   ├── HostRepository.java (CRUD methods)
│   │   │   │   │   ├── GuestRepository.java (CRUD methods)
│   │   │   │   │   └── ReservationRepository.java (CRUD methods)
│   │   │   │   ├── service/
│   │   │   │   │   ├── HostService.java (interact with HostRepository)
│   │   │   │   │   │   - getHosts()
│   │   │   │   │   │   - createHost(Host)
│   │   │   │   │   │   - ... (other CRUD methods)
│   │   │   │   │   ├── GuestService.java (interact with GuestRepository)
│   │   │   │   │   │   - getGuests()
│   │   │   │   │   │   - createGuest(Guest)
│   │   │   │   │   │   - ... (other CRUD methods)
│   │   │   │   │   └── ReservationService.java (CRUD, helper methods)
│   │   │   │               - getReservationsByHost(Host)
│   │   │   │               - createReservation(Reservation)
│   │   │   │               - ... (other CRUD methods)
│   │   │   │               - checkAvailability(Date, Date) (helper method)
│   │   │   │   └── ui/  (New folder for UI classes)
│   │   │   │       ├── ConsoleIO.java (console input/output methods)
│   │   │   │       ├── Controller.java (user interaction logic)
│   │   │   │       ├── MainMenuOption.java (enums for menu options)
│   │   │   │       └── View.java (reservation view methods)
│   │   │       └── resources/
│   │   │           ├── data.properties (file paths)
│   │   │           └── dependency-config.xml (Spring dependencies)
│   └── app.java  (Spring application entry point)
├─── pom.xml  (project dependencies)
└── README.md  (project documentation)
├─── test/  (Integration tests)
│   ├── domain/   (Test files for repository and repository doubles)
│   │   ... (test classes)
│   └── service/  (Test files for service classes)
│       ... (test classes)