package sk.tuke.gamestudio.entity;


import javax.persistence.*;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
@NamedQuery(name = "User.resetUsers", query = "DELETE FROM User")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String username;
    private String password;

    public User() {
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "ident=" + ident +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}