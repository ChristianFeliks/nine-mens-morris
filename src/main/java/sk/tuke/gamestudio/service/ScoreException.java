package sk.tuke.gamestudio.service;

public class ScoreException extends RuntimeException {
    public ScoreException(String message) {
        super(message);
        System.out.println("ScoreException: " + message);
    }

    public ScoreException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("ScoreException: " + message + " " + cause);
    }
}
