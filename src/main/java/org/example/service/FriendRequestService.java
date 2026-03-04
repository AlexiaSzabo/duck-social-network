package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.domain.FriendRequest;
import org.example.repository.FriendRequestRepository;

import java.util.List;

public class FriendRequestService {

    private final FriendRequestRepository repo;
    private final FriendshipService friendshipService;


    public FriendRequestService(FriendRequestRepository repo, FriendshipService friendshipService) {
        this.repo = repo;
        this.friendshipService = friendshipService;
    }

    public FriendRequest addFriendRequest(FriendRequest fr) {
        return repo.save(fr);
    }

    // Returneaza lista de pending requests pentru un user
    public ObservableList<FriendRequest> getPendingRequestsObservable(Integer userId) {
        List<FriendRequest> list = repo.findAll().stream()
                .filter(fr -> fr.getToUserId().equals(userId) && fr.getStatus() == FriendRequest.Status.PENDING)
                .toList();
        return FXCollections.observableArrayList(list);
    }

    public List<FriendRequest> getPendingRequestsForUser(Integer userId) {
        return repo.findAll().stream()
                .filter(fr -> fr.getToUserId().equals(userId) && fr.getStatus() == FriendRequest.Status.PENDING)
                .toList();
    }

    // Raspunde la request (APPROVED sau REJECTED)
    public void respondToRequest(int requestId, FriendRequest.Status status) {
        FriendRequest fr = repo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found: " + requestId));
        fr.setStatus(status);
        repo.update(fr);
    }


    public boolean canSendRequest(Integer fromId, Integer toId) {

        // Daca sunt prieteni acm atunci nu poate sa ii trimita friend request
        if (friendshipService.areFriends(fromId, toId)) {
            return false;
        }

        //Daca exista un request PENDING intre ei nu conteaza din partea caruia ca nu o sa mai pot trimite inca unul
        boolean hasPending = repo.findAll().stream()
                .anyMatch(fr ->
                        fr.getStatus() == FriendRequest.Status.PENDING &&
                                (
                                        (fr.getFromUserId().equals(fromId) && fr.getToUserId().equals(toId)) ||
                                                (fr.getFromUserId().equals(toId) && fr.getToUserId().equals(fromId))
                                )
                );

        return !hasPending; // daca nu e pending atunci se poate trimite friend request
    }



    // Returneaza toate request-urile
    public List<FriendRequest> getAllRequests() {
        return repo.findAll();
    }
}
