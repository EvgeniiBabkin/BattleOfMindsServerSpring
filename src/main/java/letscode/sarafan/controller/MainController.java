package letscode.sarafan.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import letscode.sarafan.domain.User;
import letscode.sarafan.repo.UserRepo;
import letscode.sarafan.serializer.UserSerializer;
import letscode.sarafan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(User.class, new UserSerializer())
            .create();

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @GetMapping
    public String getUser (@AuthenticationPrincipal User user){

        String main = gson.toJson(user);
        return main;
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user){

        if (user.getSubscribers().contains(currentUser)) {
            return "error";
        }

        userService.subscribe(currentUser, user);
        return "user add friend";

    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user){

        if (user.getSubscribers().contains(currentUser)) {
            userService.unsubscribe(currentUser, user);
            return "user add friend";
        }

        return "error";
    }


}
