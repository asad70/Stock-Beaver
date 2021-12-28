package com.example.stockbeaver;
import java.util.ArrayList;

/**
 * Class that holds information about users
 */
public class User {
    private   String email;
    private   String password;
    private   String username;
    private   ArrayList<String> followers = new ArrayList<>();
    private   ArrayList<String> requests = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }
    public User(String email, String password, String username) {
        //This is for registration:
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User(String email, String password, String username, ArrayList<String> followers, ArrayList<String> requests) {
        //This is for login:
        this.email = email;
        this.password = password;
        this.followers = followers;
        this.requests = requests;
        this.username = username;
    }

    /**
     * Gets the email
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email
     * @param email name of the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     * @param password name of the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Gets the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username
     * @param username name of the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the list of followers
     * @return the list of followers
     */
    public ArrayList<String> getFollowers() {
        return followers;
    }
    /**
     * Gets the list of requests
     * @return the list of requests
     */
    public ArrayList<String> getRequests() {
        return requests;
    }

    /**
     * Implement remove user from requests after acceptance.
     * @param username The name of the accepted user.
     */
    public void acceptRequest(String username) {
        this.requests.remove(username);
        this.followers.add(username);
    }
    /**
     * Implement remove user from requests after being declined.
     * @param username The name of the declined user.
     */
    public void declineRequest(String username) {
        if(!this.requests.contains(username)) throw new IllegalArgumentException("User is not in" + username + "requests");
        else this.requests.remove(username);
    }
}