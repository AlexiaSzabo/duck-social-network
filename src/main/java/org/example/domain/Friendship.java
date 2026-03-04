package org.example.domain;

/**
 * Represents a friendship relation between two users.
 * Matches the "friendships" database table.
 * This class does not extend Entity because the primary key is composite.
 */
public class Friendship {

    private Integer user1Id;
    private Integer user2Id;

    public Friendship(Integer user1Id, Integer user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public Integer getUser1Id() {
        return user1Id;
    }
    public Integer getUser2Id() {
        return user2Id;
    }


    @Override
    public String toString() {
        return String.format(
                "Friendship{user1=%d, user2=%d}",
                user1Id, user2Id
        );
    }

}
