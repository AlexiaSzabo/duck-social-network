package org.example.ui;

import org.example.domain.*;
import org.example.event.Event;
import org.example.event.RaceEvent;
import org.example.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Main console UI with 3 submenus.
 */
public class ConsoleUI {

    private final UserService userService;
    private final PersonService personService;
    private final DuckService duckService;
    private final FriendshipService friendshipService;
    private final CardService cardService;
    private final CardMemberService cardMemberService;
    private final EventService eventService;
    private final RaceEventService raceEventService;
    private final RaceParticipantService raceParticipantService;

    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(
            UserService userService,
            PersonService personService,
            DuckService duckService,
            FriendshipService friendshipService,
            CardService cardService,
            CardMemberService cardMemberService,
            EventService eventService,
            RaceEventService raceEventService,
            RaceParticipantService raceParticipantService
    ) {
        this.userService = userService;
        this.personService = personService;
        this.duckService = duckService;
        this.friendshipService = friendshipService;
        this.cardService = cardService;
        this.cardMemberService = cardMemberService;
        this.eventService = eventService;
        this.raceEventService = raceEventService;
        this.raceParticipantService = raceParticipantService;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Users menu");
            System.out.println("2. Cards menu");
            System.out.println("3. Events menu");
            System.out.println("0. Exit");

            int option = readInt("Choose option: ");

            switch (option) {
                case 1 -> usersMenu();
                case 2 -> cardsMenu();
                case 3 -> eventsMenu();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    // -------------------------------------------------------------------------
    // USERS MENU
    // -------------------------------------------------------------------------

    private void usersMenu() {
        while (true) {
            System.out.println("\n--- USERS MENU ---");
            System.out.println("1. Add user (PERSON / DUCK)");
            System.out.println("2. Remove user");
            System.out.println("3. Add friendship");
            System.out.println("4. Remove friendship");
            System.out.println("5. Show all users");
            System.out.println("6. Show number of communities");
            System.out.println("7. Show most sociable community");
            System.out.println("0. Back");

            int option = readInt("Choose option: ");

            switch (option) {
                case 1 -> addUserMenu();
                case 2 -> removeUser();
                case 3 -> addFriendship();
                case 4 -> removeFriendship();
                case 5 -> showAllUsers();
                case 6 -> showCommunitiesCount();
                case 7 -> showMostSociableCommunity();
                case 0 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addUserMenu() {
        System.out.println("Add PERSON or DUCK?");
        System.out.println("1. Person");
        System.out.println("2. Duck");

        int type = readInt("Choose type: ");

        String username = readString("Username: ");
        String email = readString("Email: ");
        String password = readString("Password: ");

        User newUser = new User(username, email, password);
        newUser = userService.addUser(newUser);

        if (type == 1) {
            String nume = readString("Last name: ");
            String prenume = readString("First name: ");
            String ocupatie = readString("Occupation: ");
            double empatie = readDouble("Empathy level: ");
            LocalDate date = LocalDate.parse(readString("Birth date (yyyy-mm-dd): "));

            Person p = new Person(newUser.getId(), nume, prenume, ocupatie, empatie, date);
            personService.addPerson(p);

            System.out.println("Person added!");

        } else if (type == 2) {

            TipRata tip = TipRata.valueOf(readString("Duck type (FLYING/SWIMMING/FLYING_AND_SWIMMING): ").toUpperCase());
            double viteza = readDouble("Speed: ");
            double rezistenta = readDouble("Stamina: ");

            Duck d = new Duck(newUser.getId(),username,email, password, tip, viteza, rezistenta);
            duckService.addDuck(d);

            System.out.println("Duck user added!");
        }
    }

    private void removeUser() {
        int id = readInt("User ID: ");
        userService.deleteUser(id);
        System.out.println("User removed.");
    }

    private void addFriendship() {
        int u1 = readInt("User 1 ID: ");
        int u2 = readInt("User 2 ID: ");
        friendshipService.addFriendship(u1, u2);
        System.out.println("Friendship added.");
    }

    private void removeFriendship() {
        int u1 = readInt("User 1 ID: ");
        int u2 = readInt("User 2 ID: ");
        friendshipService.removeFriendship(u1, u2);
        System.out.println("Friendship removed.");
    }

    private void showAllUsers() {
        System.out.println("\n--- ALL USERS ---");
        userService.getAllUsers().forEach(System.out::println);
    }

    private void showCommunitiesCount() {
        System.out.println("Number of communities: " + friendshipService.numberOfCommunities());
    }

    private void showMostSociableCommunity() {
        System.out.println("Most sociable community: " + friendshipService.mostSociableCommunity());
    }

    // -------------------------------------------------------------------------
    // CARDS MENU
    // -------------------------------------------------------------------------

    private void cardsMenu() {
        while (true) {
            System.out.println("\n--- CARDS MENU ---");
            System.out.println("1. Add card");
            System.out.println("2. Remove card");
            System.out.println("3. Add member to card");
            System.out.println("4. Remove member from card");
            System.out.println("5. Show all cards");
            System.out.println("0. Back");

            int option = readInt("Choose option: ");

            switch (option) {
                case 1 -> addCard();
                case 2 -> removeCard();
                case 3 -> addMemberToCard();
                case 4 -> removeMemberFromCard();
                case 5 -> showAllCards();
                case 0 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addCard() {
        String name = readString("Card name: ");

        CardType type = null;

        while (type == null) {
            String input = readString("Card type (flying / swimming): ").trim().toUpperCase();

            try {
                type = CardType.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid type! Allowed: flying / swimming.");
            }
        }

        int owner = readInt("Owner user ID: ");

        Card c = new Card(null, name, type, owner);
        cardService.addCard(c);

        System.out.println("Card created!");
    }


    private void removeCard() {
        int id = readInt("Card ID: ");
        cardService.deleteCard(id);
        System.out.println("Card removed.");
    }

    private void addMemberToCard() {
        int cardId = readInt("Card ID: ");
        int duckId = readInt("Duck ID: ");
        cardMemberService.addDuckToCard(cardId, duckId);
        System.out.println("Duck added to card.");
    }

    private void removeMemberFromCard() {
        int cardId = readInt("Card ID: ");
        int duckId = readInt("Duck ID: ");
        cardMemberService.removeDuckFromCard(cardId, duckId);
        System.out.println("Duck removed from card.");
    }

    private void showAllCards() {
        System.out.println("\n--- ALL CARDS ---");
        cardService.getAllCards().forEach(System.out::println);
    }

    // -------------------------------------------------------------------------
    // EVENTS MENU
    // -------------------------------------------------------------------------

    private void eventsMenu() {
        while (true) {
            System.out.println("\n--- EVENTS MENU ---");
            System.out.println("1. Add Race Event");
            System.out.println("2. Add participant to event");
            System.out.println("3. Remove participant from event");
            System.out.println("4. Show all events");
            System.out.println("5. Delete event");
            System.out.println("0. Back");

            int option = readInt("Choose option: ");

            switch (option) {
                case 1 -> addRaceEvent();
                case 2 -> addParticipantToEvent();
                case 3 -> removeParticipantFromEvent();
                case 4 -> showAllEvents();
                case 5 -> deleteEvent();
                case 0 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addRaceEvent() {
        String name = readString("Event name: ");
        int creator = readInt("Creator user ID: ");
        int lanes = readInt("Number of lanes: ");

        RaceEvent ev = new RaceEvent(0, name, creator, lanes);
        raceEventService.addRaceEvent(ev);

        System.out.println("Race event created!");
    }




    private void addParticipantToEvent() {
        int eventId = readInt("Event ID: ");
        int duckId = readInt("Duck ID: ");
        raceParticipantService.addParticipant(eventId, duckId);
        System.out.println("Participant added!");
    }

    private void removeParticipantFromEvent() {
        int eventId = readInt("Event ID: ");
        int duckId = readInt("Duck ID: ");
        raceParticipantService.removeParticipant(eventId, duckId);
        System.out.println("Participant removed.");
    }

    private void showAllEvents() {
        System.out.println("\n--- ALL EVENTS ---");
        eventService.getAllEvents().forEach(System.out::println);
    }

    private void deleteEvent() {
        int id = readInt("Event ID: ");
        eventService.deleteEvent(id);
        System.out.println("Event deleted.");
    }

    // -------------------------------------------------------------------------
    // Input helpers
    // -------------------------------------------------------------------------

    private int readInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(scanner.nextLine());
    }

    private double readDouble(String msg) {
        System.out.print(msg);
        return Double.parseDouble(scanner.nextLine());
    }

    private String readString(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}
