package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceJDBCTest {
    private CommentServiceJDBC commentServiceJDBC;

    @BeforeEach
    void setUp() {
        commentServiceJDBC = new CommentServiceJDBC();
        commentServiceJDBC.reset();
    }

    @Test
    void addCommentShouldInsertComment() {
        Comment comment = new Comment("game1", "player1", "comment1", new Date());
        assertDoesNotThrow(() -> commentServiceJDBC.addComment(comment));
    }

    @Test
    void getCommentsShouldReturnListOfComments() {
        String game = "game2";
        Comment comment = new Comment("game2", "player2", "comment2", new Date());
        commentServiceJDBC.addComment(comment);
        List<Comment> comments = assertDoesNotThrow(() -> commentServiceJDBC.getComments(game));
        assertFalse(comments.isEmpty());
        assertEquals(1, comments.size());
        assertEquals("game2", comments.get(0).getGame());
        assertEquals("player2", comments.get(0).getPlayer());
        assertEquals("comment2", comments.get(0).getComment());
    }

    @Test
    void getCommentsShouldReturnEmptyListWhenGameDoesNotExist() {
        String game = "nonExistingGame";
        List<Comment> comments = assertDoesNotThrow(() -> commentServiceJDBC.getComments(game));
        assertTrue(comments.isEmpty());
    }

    @Test
    void resetShouldDeleteAllComments() {
        assertDoesNotThrow(() -> commentServiceJDBC.reset());
        assertEquals(0, assertDoesNotThrow(() -> commentServiceJDBC.getComments("game1")).size());
    }
}