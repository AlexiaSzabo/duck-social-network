package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.repository.FriendshipRepository;
import org.example.repository.UserRepository;

import java.util.*;

/**
 * Manages friendships AND computes community metrics (merged from CommunityService).
 */
public class FriendshipService {

    private final FriendshipRepository friendshipRepo;
    private final UserRepository userRepo;

    private final ObservableList<Friendship> friendships = FXCollections.observableArrayList();


    public FriendshipService(FriendshipRepository friendshipRepo, UserRepository userRepo) {
        this.friendshipRepo = friendshipRepo;
        this.userRepo = userRepo;

        friendships.addAll(friendshipRepo.findAll());
    }

    /**
     * Creates a new friendship between two users.
     */
    public void addFriendship(int u1, int u2) {
        // verificam daca prietenia exista deja
        boolean exists = friendships.stream().anyMatch(f ->
                (f.getUser1Id() == u1 && f.getUser2Id() == u2) ||
                        (f.getUser1Id() == u2 && f.getUser2Id() == u1));

        if (exists) return; // nu mai adaugam

        Friendship f = new Friendship(u1, u2);
        friendshipRepo.save(f);
        friendships.add(f); // update instant in TableView
    }


    /**
     * Removes a friendship connection.
     */
    public void removeFriendship(int u1, int u2) {
        friendshipRepo.delete(u1, u2);
        friendships.removeIf(f -> (f.getUser1Id() == u1 && f.getUser2Id() == u2)
                || (f.getUser1Id() == u2 && f.getUser2Id() == u1));
    }
    /**
     * Returns all friendship pairs.
     */
    public List<Friendship> getAll() {
        return friendshipRepo.findAll();
    }

    public ObservableList<Friendship> getObservableFriendships() {
        return friendships;
    }


    public List<Integer> findFriendsOf(int userId) {
        return friendshipRepo.findFriendsOf(userId); // repo-ul returnează lista de Useri
    }

    public boolean areFriends(int u1, int u2) {
        return friendshipRepo.findFriendsOf(u1).contains(u2);
    }


    /**
     * Returns list of friends of a specific user.
     */
    public List<Integer> getFriends(int id) {
        return friendshipRepo.findFriendsOf(id);
    }

    // -------------------------------------------------------
    // COMMUNITY LOGIC (from former CommunityService)
    // -------------------------------------------------------

    /**
     * Computes how many connected components exist in the user graph.
     * Uses DFS to explore all users belonging to one community.
     */
    public int numberOfCommunities() {
        List<User> users = userRepo.findAll();
        Set<Integer> visited = new HashSet<>();
        int communities = 0;

        for (User u : users) {
            if (!visited.contains(u.getId())) {
                dfs(u.getId(), visited);
                communities++;
            }
        }

        return communities;
    }

    /**
     * Computes the most sociable community (the largest connected component).
     * Returns a list of user IDs that belong to that component.
     */
    public List<Integer> mostSociableCommunity() {
        List<User> users = userRepo.findAll();
        Set<Integer> visited = new HashSet<>();
        List<Integer> best = new ArrayList<>();

        for (User u : users) {
            if (!visited.contains(u.getId())) {

                List<Integer> component = new ArrayList<>();

                dfsCollect(u.getId(), visited, component);

                if (component.size() > best.size()) {
                    best = component;
                }
            }
        }

        return best;
    }

    /**
     * Standard DFS that marks all users reachable from 'userId'.
     * Used for counting communities.
     */
    private void dfs(int userId, Set<Integer> visited) {
        visited.add(userId);

        for (int friendId : friendshipRepo.findFriendsOf(userId)) {
            if (!visited.contains(friendId)) {
                dfs(friendId, visited);
            }
        }
    }

    /**
     * DFS variant that also collects user IDs in a list.
     * Used for finding the largest community.
     */
    private void dfsCollect(int userId, Set<Integer> visited, List<Integer> component) {
        visited.add(userId);
        component.add(userId);

        for (int friendId : friendshipRepo.findFriendsOf(userId)) {
            if (!visited.contains(friendId)) {
                dfsCollect(friendId, visited, component);
            }
        }
    }


    public List<Friendship> getFriendshipPage(int page, int size) {
        int offset = (page - 1) * size;
        return friendshipRepo.findPage(size, offset);
    }

    public long getFriendshipCount() {
        return friendshipRepo.count();
    }

}
