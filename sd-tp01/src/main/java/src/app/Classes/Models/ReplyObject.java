package src.app.Classes.Models;

public class ReplyObject {

    // Attributes
    private boolean wasOperationSuccessful;
    private String message;
    private User user;
    private Channel channel;

    // Constructors

    // Empty constructor
    public ReplyObject() {
        this.wasOperationSuccessful = false;
        this.message = "";
        this.user = null;
    }

    // Constructor with all parameters -- user
    public ReplyObject(boolean success, String message, User user) {
        this.wasOperationSuccessful = success;
        this.message = message;
        this.user = user;
    }

    // Constructor with all parameters -- channel
    public ReplyObject(boolean success, String message, Channel channel) {
        this.wasOperationSuccessful = success;
        this.message = message;
        this.channel = channel;
    }

    // Constructor with only success and message
    public ReplyObject(boolean success, String message) {
        this.wasOperationSuccessful = success;
        this.message = message;
        this.user = null;
    }

    // Constructor with only success and user
    public ReplyObject(boolean success, User user) {
        this.wasOperationSuccessful = success;
        this.message = "";
        this.user = user;
    }

    // Constructor with only success and channel
    public ReplyObject(boolean success, Channel channel) {
        this.wasOperationSuccessful = success;
        this.message = "";
        this.channel = channel;
    }

    // Constructor with only success
    public ReplyObject(boolean success) {
        this.wasOperationSuccessful = success;
        this.message = "";
        this.user = null;
    }

    // Getters
    public boolean getWasOperationSuccessful() {
        return this.wasOperationSuccessful;
    }

    public String getMessage() {
        return this.message;
    }

    public User getUser() {
        return this.user;
    }

    public Channel getChannel() {
        return this.channel;
    }

    // Setters
    public void setWasOperationSuccessful(boolean success) {
        this.wasOperationSuccessful = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "Success: " + this.wasOperationSuccessful + "\nMessage: " + this.message + "\nUser: " + this.user;
    }
}