package sk.tuke.gamestudio.game.ninemensmorris.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.ninemensmorris.core.*;
import sk.tuke.gamestudio.service.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final int MAX_POSITIONS = 24;

    private final int INDEX_OFFSET = 1;

    private String player1Name = "PLAYER_1";
    private String player2Name = "PLAYER_2";
    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_RED_X = ANSI_RED + "X" + ANSI_RESET;
    private final String ANSI_BLUE_O = ANSI_BLUE + "O" + ANSI_RESET;

    private enum Command {
        RESTART, EXIT, NEW, HELP, COMMENT, RATING, ADD_COMMENT, ADD_RATING, SCORE
    }

    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    /*private ScoreService scoreService = new ScoreServiceJDBC();
    private CommentService commentService = new CommentServiceJDBC();
    private RatingService ratingService = new RatingServiceJDBC();*/
    private Field field;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private GameState gameState;
    private int turnCount = 0;
    private GameMode gameMode;

    public ConsoleUI(Field field) {
        this.field = field;
        this.gameState = GameState.START;
    }

    private int[] handleInput() {
        while (true) {
            String statusMessage = "";
            statusMessage = updateStatusMessage(currentPlayer, statusMessage);
            System.out.println(currentPlayer.getName() + statusMessage);

            String input = scanner.nextLine();
            Command command = null;
            try {
                command = parseCommand(input);
                if (command != null) {
                    handleGameState(command);
                } else {
                    int[] playerMove = Arrays.stream(input.split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    if (isValidInput(playerMove, currentPlayer.getState())) {
                        return playerMove;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or commands: restart, exit");
            }
        }
    }

    private Command parseCommand(String input) {
        input = input.replace(" ", "_");
        try {
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void handleGameState(Command command) {
        switch (command) {
            case RESTART:
                System.out.println("Restarting game...");
                restartGame();
                break;
            case EXIT:
                System.out.println("Exiting game...");
                gameState = GameState.END;
                System.exit(0);
                break;
            case NEW:
                System.out.println("Starting new game...");
                gameState = GameState.PLAYING;
                restartGame();
                currentPlayer = player1;
                break;
            case HELP:
                printCommands();
                break;
            case COMMENT:
                showAllComments();
                System.out.println("\nTo add comment type: add comment");
                break;
            case RATING:
                showAverageRating();
                System.out.println("\nTo add rating type: add rating");
                break;
            case ADD_COMMENT:
                addComment();
                break;
            case ADD_RATING:
                addRating();
                break;
            case SCORE:
                showTopScores();
                break;
            default:
                System.out.println("Invalid command. Please enter a valid command");
                printCommands();
                break;
        }
    }

    private boolean isValidInput(int[] input, PlayerState state) {
        if (state == PlayerState.MOVING && input.length != 2) {
            System.out.println("Invalid input. Please enter 2 numbers between " + INDEX_OFFSET + " and " + MAX_POSITIONS + ".");
            return false;
        }
        for (int i : input) {
            if (i < INDEX_OFFSET || i > MAX_POSITIONS) {
                System.out.println("Invalid input. Please enter a number between " + INDEX_OFFSET + " and " + MAX_POSITIONS + ".");
                return false;
            }
        }
        return true;
    }

    private String updateStatusMessage(Player currentPlayer, String statusMessage) {
        switch (currentPlayer.getState()) {
            case PLACING:
                statusMessage = ": place a piece to position: #";
                break;
            case MOVING:
                statusMessage = ": Move a piece from position to position: # #";
                break;
            case REMOVING:
                statusMessage = ": Remove a piece from position: #";
                break;
        }
        return statusMessage;
    }

    public void play() {
        //scoreService.reset();
        //commentService.reset();
        //ratingService.reset();
        initializeGame();

        System.out.println("Welcome to the game " + player1.getName() + " and " + player2.getName() + " !");
        System.out.println("Type 'new' to start a new game, 'exit' to exit the game or 'help' for help.");

        // MAIN MENU
        while (gameState != GameState.END) {
            handleMainMenu();

            // GAMEPLAY
            while (gameState == GameState.PLAYING) {
                handleGameplay();
            }

        }
    }

    private void initializeGame() {
        System.out.println("Welcome,\nChoose game mode:");
        System.out.println("[1]Human VS Human\n[2]Human VS Smart Bot\n[3]Human VS Random Bot");

        gameMode = getGameModeFromUser();
        createPlayers(gameMode);
    }

    private GameMode getGameModeFromUser() {
        GameMode gameMode = null;
        while (gameMode == null) {
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    gameMode = GameMode.HUMAN_VS_HUMAN;
                    break;
                case "2":
                    gameMode = GameMode.HUMAN_VS_SMART_BOT;
                    break;
                case "3":
                    gameMode = GameMode.HUMAN_VS_RANDOM_BOT;
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1, 2 or 3.");
                    break;
            }
        }
        return gameMode;
    }

    private void createPlayers(GameMode gameMode) {
        System.out.println(ANSI_RED + "Player 1" + ANSI_RESET + " choose your name: (press enter to use default name)");
        String input = scanner.nextLine();
        player1Name = input.isEmpty() ? player1Name : input;
        player1 = new Player(ANSI_RED + player1Name + ANSI_RESET, ANSI_RED_X);
        currentPlayer = player1;
        switch (gameMode) {
            case HUMAN_VS_HUMAN:
                System.out.println(ANSI_BLUE + "Player 2" + ANSI_RESET + " choose your name: (press enter to use default name)");
                input = scanner.nextLine();
                player2Name = input.isEmpty() ? player2Name : input;
                player2 = new Player(ANSI_BLUE + player2Name + ANSI_RESET, ANSI_BLUE_O);
                break;
            case HUMAN_VS_SMART_BOT:
                player2 = new SmartBot(ANSI_BLUE + "Smart Bot" + ANSI_RESET, ANSI_BLUE_O, player1);
                break;
            case HUMAN_VS_RANDOM_BOT:
                player2 = new RandomBot(ANSI_BLUE + "Random Bot" + ANSI_RESET, ANSI_BLUE_O, player1);
                break;
        }
    }

    private void handleGameplay() {
        printPlayerStatus();
        int[] playerMove;
        if (currentPlayer instanceof Bot) {
            playerMove = handleInputBot();
        } else {
            playerMove = handleInput();
        }
        handlePlayerState(playerMove);
        checkPlayerLoss();
    }

    private int[] handleInputBot() {
        int[] playerMove;
        System.out.println(currentPlayer.getName() + "'s turn");
        Bot bot = (Bot) currentPlayer;
        switch (bot.getState()) {
            case PLACING:
                playerMove = new int[]{bot.getIndexToPlacePiece(field)};
                break;
            case MOVING:
                playerMove = bot.getMove(field);
                break;
            case REMOVING:
                playerMove = new int[]{bot.getIndexToRemovePieceOfOpponent(field)};
                break;
            default:
                throw new IllegalStateException("Unexpected state: " + bot.getState());
        }
        return playerMove;
    }

    private void handlePlayerState(int[] playerMove) {
        switch (currentPlayer.getState()) {
            case PLACING:
                handlePlayerPlacing(playerMove);
                break;
            case MOVING:
                handlePlayerMoving(playerMove);
                break;
            default:
                break;
        }
    }

    private void checkPlayerLoss() {
        if (field.hasPlayerLost(currentPlayer)) {
            System.out.println(currentPlayer.getName() + " loses !");
            turnCount = (turnCount + 1) / 2;
            switchPlayer();
            System.out.println(currentPlayer.getName() + " wins !");

            if (gameMode != GameMode.HUMAN_VS_RANDOM_BOT && !(currentPlayer instanceof Bot)) addPlayerScore();
            else System.out.println("Scores are not added in Human VS Random Bot mode.");
            showTopScores();
            //gameState = GameState.END;
            promptToPlayAgain();
        }
    }

    private void promptToPlayAgain() {
        System.out.println("Do you want to play again? (yes/no)");
        String input = scanner.nextLine();
        if (input.equals("yes") || input.equals("y")) {
            gameState = GameState.START;
            System.out.println("\nType 'new' to start a new game, 'exit' to exit the game or 'help' for help.");
        } else {
            gameState = GameState.END;
        }
    }

    private void handleMainMenu() {
        Command command = parseCommand(scanner.nextLine());
        if (command != null) handleGameState(command);
    }

    private void printPlayerStatus() {
        System.out.println(currentPlayer.getName() + "'s pieces:\n   Pieces placed: " + currentPlayer.getNumberOfPiecesPlaced() + "   Pieces yet to place: " + currentPlayer.getNumberOfPiecesLeftToPlace());
    }

    private void handlePlayerMoving(int[] playerMove) {
        if (field.isMoveValid(playerMove[0] - INDEX_OFFSET, playerMove[1] - INDEX_OFFSET, currentPlayer)) {
            field.movePiece(playerMove[0] - INDEX_OFFSET, playerMove[1] - INDEX_OFFSET, currentPlayer);
            System.out.println(currentPlayer.getName() + ": Moving from " + playerMove[0] + " to " + playerMove[1]);
            handleMillCreation(playerMove[1] - INDEX_OFFSET);
            endTurn();
        } else {
            System.out.println("Invalid move. Please try again.");
        }
    }

    private void handlePlayerPlacing(int[] playerMove) {
        if (field.isMoveValid(playerMove[0] - INDEX_OFFSET, currentPlayer)) {
            field.placePiece(playerMove[0] - INDEX_OFFSET, currentPlayer);
            handleMillCreation(playerMove[0] - INDEX_OFFSET);

            updatePlayerState(currentPlayer);

            endTurn();
        } else {
            System.out.println("Invalid move. Please try again.");
        }
    }

    private void endTurn() {
        for (int i = 0; i < 2; i++) {
            System.out.println("\n");
        }
        field.printField();
        switchPlayer();
        turnCount++;
    }

    private void handleMillCreation(int position) {
        if (field.wasMillCreated(position, currentPlayer)) {
            field.printField();
            System.out.println(currentPlayer.getName() + ": Mill was created");
            handlePieceRemoving();
        }
    }

    private void handlePieceRemoving() {
        PlayerState previousState = currentPlayer.getState();
        currentPlayer.setState(PlayerState.REMOVING);
        while (currentPlayer.getState() == PlayerState.REMOVING) {
            int pieceToRemove;
            if (currentPlayer instanceof Bot) {
                pieceToRemove = ((Bot) currentPlayer).getIndexToRemovePieceOfOpponent(field);
            } else {
                pieceToRemove = handleInput()[0];
            }
            if (field.isMoveValid(pieceToRemove - INDEX_OFFSET, currentPlayer)) {
                field.removePiece(pieceToRemove - INDEX_OFFSET);
                System.out.println(currentPlayer.getName() + ": Removed piece from position " + pieceToRemove);
                currentPlayer.setState(previousState);
            } else {
                System.out.println("Invalid move. Please try again.");
            }
        }
    }

    private void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    private void addPlayerScore() {
        int multiplier = (gameMode == GameMode.HUMAN_VS_HUMAN) ? 10 : 20;
        String modifiedPlayerName;
        if (currentPlayer.getSymbol().equals(ANSI_RED_X)){
            modifiedPlayerName = player1Name;
        }
        else{
            modifiedPlayerName = player2Name;
        }
        Score score = new Score("ninemensmorris", modifiedPlayerName, 1500 - turnCount * multiplier, new Date());
        scoreService.addScore(score);
    }

    private void showTopScores() {
        System.out.println("\nTop scores:");
        printFormattedScores(scoreService.getTopScores("ninemensmorris"));
    }

    private void showAllComments() {
        List<Comment> comments = commentService.getComments("ninemensmorris");
        printFormattedComments(comments);
    }

    private void showAverageRating() {
        int averageRating = ratingService.getAverageRating("ninemensmorris");
        if (averageRating == 0) {
            System.out.println("\nNo ratings yet.");
        } else {
            System.out.println("\nAverage rating: " + averageRating);
        }
    }

    private void addComment() {
        System.out.println("Enter your comment:");
        String commentText = scanner.nextLine();
        String modifiedPlayerName;
        if (currentPlayer.getSymbol().equals(ANSI_RED_X)){
            modifiedPlayerName = player1Name;
        }
        else{
            modifiedPlayerName = player2Name;
        }
        Comment comment = new Comment("ninemensmorris", modifiedPlayerName, commentText, new Date());
        commentService.addComment(comment);
        //System.out.println("Comment added successfully.");
    }

    private void addRating() {
        System.out.println("Enter your rating (1-5):");
        int ratingValue = scanner.nextInt();
        String modifiedPlayerName;
        if (currentPlayer.getSymbol().equals(ANSI_RED_X)){
            modifiedPlayerName = player1Name;
        }
        else{
            modifiedPlayerName = player2Name;
        }
        Rating rating = new Rating("ninemensmorris", modifiedPlayerName, ratingValue, new Date());
        ratingService.setRating(rating);
        //System.out.println("Rating added successfully.");
    }

    private void restartGame() {
        field = new Field();
        player1 = new Player(ANSI_RED + player1.getName() + ANSI_RESET, ANSI_RED_X);
        switch (gameMode) {
            case HUMAN_VS_HUMAN:
                player2 = new Player(ANSI_BLUE + player2.getName() + ANSI_RESET, ANSI_BLUE_O);
                break;
            case HUMAN_VS_SMART_BOT:
                player2 = new SmartBot(ANSI_BLUE + player2.getName() + ANSI_RESET, ANSI_BLUE_O, player1);
                break;
            case HUMAN_VS_RANDOM_BOT:
                player2 = new RandomBot(ANSI_BLUE + player2.getName() + ANSI_RESET, ANSI_BLUE_O, player1);
                break;
        }
        this.currentPlayer = player1;
        turnCount = 0;
        field.printField();
    }

    private void updatePlayerState(Player currentPlayer) {
        if (currentPlayer.getNumberOfPiecesLeftToPlace() == 0) {
            currentPlayer.setState(PlayerState.MOVING);
        }
    }

    private void printCommands() {
        System.out.println("List of available commands:");
        System.out.println("new: Starts a new game.");
        System.out.println("exit: Exits the game.");
        System.out.println("restart: Restarts the current game.");
        System.out.println("help: Displays this list of commands.");
        System.out.println("comment: Displays all comments.");
        System.out.println("rating: Displays the average rating.");
        System.out.println("add comment: Adds a new comment.");
        System.out.println("add rating: Adds a new rating.");
        System.out.println("score: Displays top scores.");
        System.out.println();
    }
    private void printFormattedComments(List<Comment> comments) {
        if (comments.isEmpty()) {
            System.out.println("No comments yet.");
            return;
        }
        System.out.println("\nComments:");
        System.out.println(String.format("%-20s %-20s %-20s", "Player", "Comment", "Date"));
        for (Comment comment : comments) {
            System.out.println(String.format("%-20s %-20s %-20s", comment.getPlayer(), comment.getComment(), comment.getCommentedOn()));
        }
    }
    private void printFormattedScores(List<Score> scores) {
        if (scores.isEmpty()) {
            System.out.println("No scores yet.");
            return;
        }
        System.out.println(String.format("%-20s %-20s %-20s %-20s", "Game", "Player", "Points", "Played On"));
        for (Score score : scores) {
            System.out.println(String.format("%-20s %-20s %-20d %-20s", score.getGame(), score.getPlayer(), score.getPoints(), score.getPlayedOn()));
        }
    }

    private void show(){
        if (field != null) field.printField();
    }
}
