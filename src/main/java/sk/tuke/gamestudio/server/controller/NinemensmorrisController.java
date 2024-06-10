package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.ninemensmorris.core.*;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/ninemensmorris")
@Scope(WebApplicationContext.SCOPE_SESSION) // Vytvara sa pre kazdy Session zvlast
public class NinemensmorrisController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;

    private static final int INDEX_OFFSET = 1;

    private Field field = new Field();;
    private Player player1 = new Player("Player 1", "X");
    private Player player2 = new Player("Player 2", "O");
    private Player currentPlayer = player1;
    private GameMode mode;

    private boolean score = false;
    private boolean rating = false;
    private boolean commentToogle = false;
    private boolean isGameOver = false;

    private int posx = 0;
    private int posy = 0;
    private int turnCount = 0;

    private String gameOverMessage = "";


    List<Comment> comments;
    List<Score> topScores;
    int averageRating;
    private Integer previousPosition = 0;

    @GetMapping("/new")
    public String newGame(Model model) {
        field = new Field();
        player1 = new Player(player1.getName(), "X");
        if (mode == null) mode = GameMode.HUMAN_VS_HUMAN;
        switch (mode) {
            case HUMAN_VS_HUMAN:
                player2 = new Player(player2.getName(), "O");
                break;
            case HUMAN_VS_SMART_BOT:
                player2 = new SmartBot(player2.getName(), "O", player1);
                break;
            case HUMAN_VS_RANDOM_BOT:
                player2 = new RandomBot(player2.getName(), "O", player1);
                break;
        }
        previousPosition = null;
        currentPlayer = player1;
        score = false; rating = false; commentToogle = false; isGameOver = false;
        turnCount = 0;
        prepareModel(model);
        return "ninemensmorris";
    }

    @GetMapping
    public String ninemensmorris(Model model) {
        prepareModel(model);
        return "ninemensmorris";
    }

    @GetMapping("/startup")
    public String startup(Model model) {
        prepareModel(model);
        return "ninemensmorris_startup";
    }
    @PostMapping("/startup")
    public String startup(@RequestParam String player2Name, @RequestParam String gameMode, Model model, HttpSession session) {

/*        if (player1Name.isEmpty()) {
            player1Name = "Player 1";
        }*/
        String player1Name = (String) session.getAttribute("username"); // get the username from the session
        if (player1Name == null) {
            player1Name = "Player 1";
        }
        if ( player2Name.isEmpty()){
            player2Name = "Player 2";
        }
        mode = GameMode.valueOf(gameMode);
        field = new Field();
        turnCount = 0;
        player1 = new Player(player1Name, "X");

        switch (mode) {
            case HUMAN_VS_HUMAN:
                player2 = new Player(player2Name, "O");
                break;
            case HUMAN_VS_SMART_BOT:
                player2 = new SmartBot(player2Name, "O", player1);
                break;
            case HUMAN_VS_RANDOM_BOT:
                player2 = new RandomBot(player2Name, "O", player1);
                break;
            default:
                player2 = new Player(player2Name, "O");
                break;
        }
        currentPlayer = player1;
        prepareModel(model);
        return "ninemensmorris";
    }

    @GetMapping("/move")
    public String move(@RequestParam int position, Model model) {

        if (isGameOver) {prepareModel(model); return "ninemensmorris";}

        posx = field.getFieldPositionByIndex(position).getDrawPosX();
        posy = field.getFieldPositionByIndex(position).getDrawPosY();
        model.addAttribute("posX", posx);
        model.addAttribute("posY", posy);

        if (currentPlayer.getState() == PlayerState.MOVING) {
            if (previousPosition == null) {
                // This is the first click, so store the current position
                previousPosition = position;
            } else {
                // This is the second click, so move the piece from the previous position to the new position
                if (field.isMoveValid(previousPosition, position, currentPlayer)) {
                    field.movePiece(previousPosition, position, currentPlayer);
                    //field.printField();
                    if (field.wasMillCreated(position, currentPlayer)) {
                        model.addAttribute("message", "A mill was created! Please click on a piece to remove.");
                        previousPosition = null; // Reset the previous position
                        prepareModel(model);
                        return "ninemensmorris_remove"; // return a different view that includes clickable areas for the pieces that can be removed
                    }
                    previousPosition = null; // Reset the previous position
                    switchPlayer(); // Only switch player if a valid move was made
                    if (player2 instanceof Bot) {
                        if (currentPlayer != player2) switchPlayer();
                        return "redirect:/ninemensmorris/moveBot";
                    }
                } else {
                    model.addAttribute("message", "Invalid move. Please try again.");
                }
                previousPosition = null; // Reset the previous position
            }
        } else if (field.isMoveValid(position, currentPlayer) && currentPlayer.getState() == PlayerState.PLACING) {
            field.placePiece(position, currentPlayer);
            //field.printField();
            if (currentPlayer.getNumberOfPiecesLeftToPlace() == 0) {
                currentPlayer.setState(PlayerState.MOVING);
            }
            if (field.wasMillCreated(position, currentPlayer)) {
                prepareModel(model);
                model.addAttribute("message", "A mill was created! Please click on a piece to remove.");
                return "ninemensmorris_remove"; // return a different view that includes clickable areas for the pieces that can be removed
            }
            previousPosition = null;
            switchPlayer(); // Only switch player if a valid move was made
            if (player2 instanceof Bot) {
                if (currentPlayer != player2) switchPlayer();
                return "redirect:/ninemensmorris/moveBot";
            }
        }
        else {
            model.addAttribute("message", "Invalid move. Please try again.");
        }
        prepareModel(model);
        return "ninemensmorris";
    }

    @GetMapping("/moveBot")
    public String moveBot(Model model) {

        int[] playerMove = handleInputBot();
        switch (currentPlayer.getState()) {
            case PLACING:
                System.out.println("Bot move: " + playerMove[0]);
                if (field.isMoveValid(playerMove[0] - INDEX_OFFSET, currentPlayer)) {
                    field.placePiece(playerMove[0] - INDEX_OFFSET, currentPlayer);

                    if (field.wasMillCreated(playerMove[0] - INDEX_OFFSET, currentPlayer)) {
                        PlayerState previousState = currentPlayer.getState();
                        currentPlayer.setState(PlayerState.REMOVING);
                        int pieceToRemove = ((Bot) currentPlayer).getIndexToRemovePieceOfOpponent(field);
                        field.removePiece(pieceToRemove - INDEX_OFFSET);
                        currentPlayer.setState(previousState);
                    }

                    if (currentPlayer.getNumberOfPiecesLeftToPlace() == 0) {
                        currentPlayer.setState(PlayerState.MOVING);
                    }
                    switchPlayer();
                    checkPlayerLoss(model);
                    // return "ninemensmorris";

                }
                break;
            case MOVING:
                System.out.println("Bot move: " + playerMove[0] + " -> " + playerMove[1]);
                if (field.isMoveValid(playerMove[0] - INDEX_OFFSET, playerMove[1] - INDEX_OFFSET, currentPlayer)) {
                    field.movePiece(playerMove[0] - INDEX_OFFSET, playerMove[1] - INDEX_OFFSET, currentPlayer);

                    if (field.wasMillCreated(playerMove[1] - INDEX_OFFSET, currentPlayer)) {
                        PlayerState previousState = currentPlayer.getState();
                        currentPlayer.setState(PlayerState.REMOVING);
                        int pieceToRemove = ((Bot) currentPlayer).getIndexToRemovePieceOfOpponent(field);
                        field.removePiece(pieceToRemove - INDEX_OFFSET);
                        currentPlayer.setState(previousState);
                    }
                    switchPlayer();
                    checkPlayerLoss(model);
                }

                break;
            default:
                break;
        }

        prepareModel(model);
        return "ninemensmorris";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam int position, Model model) {

        if (isGameOver) {prepareModel(model); return "ninemensmorris";}

        PlayerState player1OldState = currentPlayer.getState();
        currentPlayer.setState(PlayerState.REMOVING);
        if (field.isMoveValid(position, currentPlayer)) {
            field.removePiece(position);
            currentPlayer.setState(player1OldState); // or whatever state should be next
            switchPlayer();
            checkPlayerLoss(model);
        }
        else {
            model.addAttribute("message", "Invalid move. Please click on a piece to remove.");
            prepareModel(model);
            return "ninemensmorris_remove"; // return a different view that includes clickable areas for the pieces that can be removed
        }

        prepareModel(model);
        if (player2 instanceof Bot) {
            if (currentPlayer != player2) switchPlayer();
            return "redirect:/ninemensmorris/moveBot";
        }
        return "ninemensmorris";
    }

    @GetMapping("/scores")
    public String scores(Model model) {
        if (score) score = false;
        else score = true; rating = false; commentToogle = false;
        topScores = scoreService.getTopScores("ninemensmorris");
        prepareModel(model);
        return "ninemensmorris";
    }
    @GetMapping("/comments")
    public String comments(Model model) {
        if (commentToogle) commentToogle = false;
        else commentToogle = true; rating = false; score = false;
        comments = commentService.getComments("ninemensmorris");
        prepareModel(model);
        return "ninemensmorris";
    }
    @PostMapping("/comments")
    public String comment(@RequestParam String text, Model model) {
        if (currentPlayer.getName().equals("Player 1") || currentPlayer.getName().equals("Player 2")) {
            model.addAttribute("message", "Default users cannot add comments. Please register with a unique username.");
            prepareModel(model);
            return "ninemensmorris";
        }
        commentService.addComment(new Comment("ninemensmorris", currentPlayer.getName(), text, new java.util.Date()));
        comments = commentService.getComments("ninemensmorris");
        prepareModel(model);
        return "ninemensmorris";
    }

    @GetMapping("/ratings")
    public String ratings(Model model) {
        if (rating) rating = false;
        else rating = true; commentToogle = false; score = false;
        averageRating = ratingService.getAverageRating("ninemensmorris");
        prepareModel(model);
        return "ninemensmorris";
    }
    @PostMapping("/ratings")
    public String rating(@RequestParam int rating, Model model) {
        System.out.println("Rating: " + rating);
        if (currentPlayer.getName().equals("Player 1") || currentPlayer.getName().equals("Player 2")) {
            model.addAttribute("message", "Default users cannot add ratings. Please register with a unique username.");
            prepareModel(model);
            return "ninemensmorris";
        }
        ratingService.setRating(new Rating("ninemensmorris", currentPlayer.getName(), rating, new java.util.Date()));
        averageRating = ratingService.getAverageRating("ninemensmorris");
        prepareModel(model);
        return "ninemensmorris";
    }


    private void prepareModel(Model model) {
        model.addAttribute("field", field);
        model.addAttribute("player1", player1);
        model.addAttribute("player2", player2);
        model.addAttribute("score", score);
        model.addAttribute("comments", comments);
        model.addAttribute("scores", topScores);
        model.addAttribute("avgRating", averageRating);
        model.addAttribute("rating", rating);
        model.addAttribute("commentToogle", commentToogle);
        model.addAttribute("isGameOver", isGameOver);
        model.addAttribute("gameOverMessage", gameOverMessage);
        model.addAttribute("currentPlayerColor", currentPlayer == player1 ? "red" : "blue");
        model.addAttribute("round", (turnCount + 1) / 2);
    }
    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        turnCount++;
    }

    private void checkPlayerLoss(Model model) {
        if (field.hasPlayerLost(currentPlayer)) {
            isGameOver = true;
            turnCount = (turnCount + 1);
            switchPlayer();
            if (currentPlayer instanceof Bot == false) { // if the winner is not a bot
                Score score = new Score("ninemensmorris", currentPlayer.getName(), 1500 - turnCount * 10, new Date());
                if (!currentPlayer.getName().equals("Player 1") && !currentPlayer.getName().equals("Player 2")) scoreService.addScore(score);
                gameOverMessage = "Congratulations ! You have won the game! Your score is: " + (1500 - turnCount * 10) + " points.";
                model.addAttribute("message", "Player " + currentPlayer.getName() + " has won!");
            }
            else { // if the winner is a bot
                switchPlayer();
                gameOverMessage = "You have lost the game! Your score is: " + (1500 - turnCount * 10) + " points.";
                model.addAttribute("message", "Player " + currentPlayer.getName() + " has lost!");
            }
        }
    }

    private int[] handleInputBot() {
        int[] playerMove;
        System.out.println("Current player: " + currentPlayer.getName());
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

}
