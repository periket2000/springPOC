package es.mybi.demo.controller;

import es.mybi.demo.core.entity.User;
import es.mybi.demo.core.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    UserService service;

    @GetMapping(value = "/user/{userId}")
    public @ResponseBody User getTestData(@PathVariable Long userId) {
        logger.debug("Getting user by id {}", userId);
        return service.getUser(userId);
    }
}
