import java.io.*;
import java.util.*;

// Abstract base class for users
abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String password;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public abstract String determineRole();

    @Override
    public String toString() {
        return "ID: " + userId + ", Name: " + name + ", Email: " + email + ", Role: " + determineRole();
    }
}

// Admin user class
class Admin extends User {
    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String determineRole() {
        return "Admin";
    }
}

// Volunteer user class
class Volunteer extends User {
    public Volunteer(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String determineRole() {
        return "Volunteer";
    }
}

// Victim user class
class Victim extends User {
    public Victim(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String determineRole() {
        return "Victim";
    }
}

// Disaster event class
class DisasterEvent {
    private final int eventId;
    private final String disasterType;
    private final String location;
    private final String description;

    public DisasterEvent(int eventId, String disasterType, String location, String description) {
        this.eventId = eventId;
        this.disasterType = disasterType;
        this.location = location;
        this.description = description;
    }

    public int getEventId() {
        return eventId;
    }

    public void displayEventDetails() {
        System.out.println("Event ID: " + eventId);
        System.out.println("Disaster Type: " + disasterType);
        System.out.println("Location: " + location);
        System.out.println("Description: " + description);
    }

    @Override
    public String toString() {
        return eventId + "," + disasterType + "," + location + "," + description;
    }

    public static DisasterEvent fromString(String line) {
        String[] parts = line.split(",");
        return new DisasterEvent(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
    }
}

// Main Disaster Management System
public class DisasterManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<User> users = new ArrayList<>();
    private static final List<DisasterEvent> disasterEvents = new ArrayList<>();
    private static final String USERS_FILE = "users.txt";
    private static final String DISASTERS_FILE = "disasters.txt";

    public static void main(String[] args) {
        loadUsersFromFile();
        loadDisastersFromFile();

        System.out.println("Welcome to the Disaster Management System!");

        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    saveUsersToFile();
                    saveDisastersToFile();
                    System.out.println("Thank you for using the system!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        // Check if User ID is already registered
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                System.out.println("Error: User ID is already registered. Try logging in.");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        // Check if Email is already registered
        for (User user : users) {
            if (user.email.equals(email)) {
                System.out.println("Error: Email is already registered. Try logging in.");
                return;
            }
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Role (Admin, Volunteer, Victim): ");
        String role = scanner.nextLine();

        switch (role) {
            case "Admin" -> users.add(new Admin(userId, name, email, password));
            case "Volunteer" -> users.add(new Volunteer(userId, name, email, password));
            case "Victim" -> users.add(new Victim(userId, name, email, password));
            default -> {
                System.out.println("Invalid role. Registration failed.");
                return;
            }
        }
        System.out.println("Registration successful!");
    }

    private static void loginUser() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        for (var user : users) {
            if (user.getUserId().equals(userId) && user.password.equals(password)) {
                System.out.println("Login successful! Welcome, " + user.name + " (" + user.determineRole() + ")");
                switch (user) {
                    case Admin admin -> adminMenu(admin);
                    case Volunteer volunteer -> volunteerMenu(volunteer);
                    case Victim victim -> victimMenu(victim);
                    default -> {
                    }
                }
                return;
            }
        }
        System.out.println("Invalid credentials. Try again.");
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n1. Add Disaster Information");
            System.out.println("2. View Registered Users");
            System.out.println("3. View Disaster Events");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addDisasterEvent();
                case 2 -> viewRegisteredUsers();
                case 3 -> viewDisasterEvents();
                case 4 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void volunteerMenu(Volunteer volunteer) {
        while (true) {
            System.out.println("\n1. View Disaster Events");
            System.out.println("2. Respond to a Disaster");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewDisasterEvents();
                case 2 -> System.out.println("Responding feature is under development.");
                case 3 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void victimMenu(Victim victim) {
        while (true) {
            System.out.println("\n1. Report a Disaster");
            System.out.println("2. View Disaster Events");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> reportDisaster();
                case 2 -> viewDisasterEvents();
                case 3 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addDisasterEvent() {
        System.out.print("Enter Disaster Type: ");
        String type = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        DisasterEvent event = new DisasterEvent(generateID(), type, location, description);
        disasterEvents.add(event);
        System.out.println("Disaster event added successfully!");
    }

    private static void reportDisaster() {
        addDisasterEvent();
    }

    private static void viewRegisteredUsers() {
        System.out.println("\nRegistered Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void viewDisasterEvents() {
        if (disasterEvents.isEmpty()) {
            System.out.println("No disaster events recorded.");
            return;
        }
        System.out.println("\nDisaster Events:");
        for (DisasterEvent event : disasterEvents) {
            event.displayEventDetails();
            System.out.println("----------------------------");
        }
    }

    private static int generateID() {
        return (int) (Math.random() * 1000);
    }

    private static void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String userId = parts[0];
                String name = parts[1];
                String email = parts[2];
                String password = parts[3];
                String role = parts[4];
                switch (role) {
                    case "Admin" -> users.add(new Admin(userId, name, email, password));
                    case "Volunteer" -> users.add(new Volunteer(userId, name, email, password));
                    case "Victim" -> users.add(new Victim(userId, name, email, password));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private static void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.getUserId() + "," + user.name + "," + user.email + "," + user.password + "," + user.determineRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private static void loadDisastersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DISASTERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                disasterEvents.add(DisasterEvent.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading disasters: " + e.getMessage());
        }
    }

    private static void saveDisastersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISASTERS_FILE))) {
            for (DisasterEvent event : disasterEvents) {
                writer.write(event.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving disasters: " + e.getMessage());
        }
    }
}
