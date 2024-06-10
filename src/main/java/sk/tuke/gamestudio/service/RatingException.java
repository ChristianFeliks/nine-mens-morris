package sk.tuke.gamestudio.service;

public class RatingException extends RuntimeException {
    public RatingException(String message) {
        super(message);
        System.out.println("RatingException: " + message);
    }

    public RatingException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("RatingException: " + message + " " + cause);
    }
}
