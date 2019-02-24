package letscode.sarafan.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import letscode.sarafan.domain.Dialog;
import letscode.sarafan.domain.Message;
import letscode.sarafan.domain.User;
import letscode.sarafan.repo.DialogRepo;
import letscode.sarafan.repo.MessageRepo;
import letscode.sarafan.repo.UserRepo;
import letscode.sarafan.serializer.DialogSerializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private DialogRepo dialogRepo;

    @Autowired
    private UserRepo userRepo;

    Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Dialog.class, new DialogSerializer())
            .create();


    @GetMapping("allDialogs")
    public String getAllDialogs(@AuthenticationPrincipal User user){

        Gson gsonDialog = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Dialog.class, new DialogSerializer.AllDialogSerializer())
                .create();

        return gsonDialog.toJson(user.getDialogs());
    }

    @GetMapping("userDialog/{user}")
    public String getUserDialog(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user){
        for (Dialog dialog : currentUser.getDialogs()){
            if (dialog.getDialogName().equals(user.getUsername())){
                return gson.toJson(dialog);
            }
        }
        return "fatal error!";
    }


    @PostMapping("sendMessage/{user}")
    public String sendMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @RequestBody Message message
    ){
        message.setLocalDateTime(LocalDateTime.now());
        setUserMessage(currentUser, user, message);

        for (Dialog dialog : currentUser.getDialogs()){
            if (dialog.getDialogName().equals(user.getUsername())){

                dialog.getMessages().add(message);
                dialogRepo.save(dialog);
                return gson.toJson(dialog);
            }
        }

        Dialog dialog = createNewDialog(user, currentUser, message);

        return gson.toJson(dialog);
    }

    public void setUserMessage(User currentUser, User user, Message message){
        for (Dialog receiverDialog : user.getDialogs()){
            if (receiverDialog.getDialogName().equals(currentUser.getUsername())){
                receiverDialog.getMessages().add(message);
                dialogRepo.save(receiverDialog);
                return;
            }
        }

        createNewDialog(currentUser, user, message);
    }

    private Dialog createNewDialog(User currentUser, User user, Message message){
        Dialog dialog = new Dialog();

        dialog.getMessages().add(message);
        dialog.setDialogName(currentUser.getUsername());

        user.getDialogs().add(dialog);
        userRepo.save(user);
        return dialog;
    }




    @GetMapping
    public List<Message> list() {

        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    public Message getOne(@PathVariable("id") Message message) {

        return message;
    }



    @PostMapping
    public Message create(@RequestBody Message message) {

        message.setLocalDateTime(LocalDateTime.now());
        return messageRepo.save(message);
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {

        BeanUtils.copyProperties(message, messageFromDb, "id");

        return messageRepo.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {

        messageRepo.delete(message);
    }



}
