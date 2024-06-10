package sk.tuke.gamestudio.service;

public class CommentException extends RuntimeException {
    public CommentException(String message) {
        super(message);
        System.out.println("CommentException: " + message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("CommentException: " + message + " " + cause);
    }
}
