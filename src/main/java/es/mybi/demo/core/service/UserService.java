package es.mybi.demo.core.service;

import es.mybi.demo.core.entity.User;
import es.mybi.demo.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        user.setUser_id(null);
        user.setCreated_at(new java.util.Date());
        user.setUpdated_at(new java.util.Date());
        return userRepository.save(user);
    }
}
