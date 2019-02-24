package letscode.sarafan.controller;


import letscode.sarafan.domain.User;
import letscode.sarafan.repo.UserRepo;
import letscode.sarafan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("registration")
public class RegistrationController {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    private Map<String, String> messageError = new HashMap<>();
    private Map<String, String> message = new HashMap<>();
    private Map<String, String> messageLogIn = new HashMap<>();
    private Map<String, String> messageLogOut = new HashMap<>();


    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    @PostMapping("/signup")
    public String addUser(@RequestBody User user){

        return userService.addUser(user) ? "registration" : "error";
    }



    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


    @PostMapping("/signin")
    public String signInUser(@RequestHeader("username") String username, @RequestHeader("password") String password){
        User userFromDb = userRepo.findByUsername(username);


        if (userFromDb != null && password.equals(userFromDb.getPassword())){


            userFromDb.setToken(userFromDb.getUsername() + "." + userFromDb.getPassword());
            userRepo.save(userFromDb);
            messageLogIn.put("Message", userFromDb.getToken());
            return "token";
        }

        messageError.put("Message", "Fatal error!");
        return  "error";
    }


    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////



    @PostMapping("/signout")
    public Map<String, String> signOutUser(@RequestBody User registration){

        User userFromDb = userRepo.findByUsername(registration.getUsername());

        if (userFromDb != null){

            userFromDb.setToken(null);
            userRepo.save(userFromDb);
            messageLogOut.put("Message", "LogOut successfully");
            return messageLogOut;

        }

        messageError.put("Message", "Fatal error!");
        return messageError;
    }

}
