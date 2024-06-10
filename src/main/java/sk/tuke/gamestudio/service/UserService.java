package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.User;

public interface UserService {
    void registerUser(User user) throws UserException;
    User findByUsername(String username) throws UserException;
    void reset() throws UserException;
}