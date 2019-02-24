package letscode.sarafan.service;

import letscode.sarafan.domain.Dialog;
import letscode.sarafan.domain.User;
import letscode.sarafan.repo.MessageRepo;
import letscode.sarafan.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepo.findByUsername(username);

    }

    public boolean addUser(User user){

        User registrationFromDb = userRepo.findByUsername(user.getUsername());

        if (registrationFromDb != null){
            return false;
        }

        user.setPoints(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return true;
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);

        userRepo.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepo.save(user);
    }

    public void addDialog(User user, Dialog dialog){
        user.getDialogs().add(dialog);
        userRepo.save(user);
    }

}
