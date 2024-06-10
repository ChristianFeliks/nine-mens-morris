package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.User;

public class UserServiceRestClient implements UserService {
    private static final String URL = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void registerUser(User user) throws UserException {
        restTemplate.postForEntity(URL, user, User.class);
    }

    @Override
    public User findByUsername(String username) throws UserException {
        return restTemplate.getForObject(URL + "/" + username, User.class);
    }

    @Override
    public void reset() throws UserException {
        // restTemplate.delete(URL);
        throw new UnsupportedOperationException("Not supported via web service");
    }
}