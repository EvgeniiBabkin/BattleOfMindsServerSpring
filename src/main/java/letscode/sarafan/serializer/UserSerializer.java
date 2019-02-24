package letscode.sarafan.serializer;


import com.google.gson.*;
import letscode.sarafan.domain.Dialog;
import letscode.sarafan.domain.User;

import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User>{

    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject userObject = baseInfoUser(src);

        JsonArray subscribersUser = new JsonArray();

        userObject.add("user_subscribers", subscribersUser);

        for (User user : src.getSubscribers()){
            subscribersUser.add(new JsonPrimitive(user.getUsername()));
        }

        JsonArray subscriptionsUser = new JsonArray();

        userObject.add("user_subscriptions", subscriptionsUser);

        for (User user : src.getSubscriptions()){
            JsonObject subscriptionsObject = baseInfoUser(user);
            subscriptionsUser.add(subscriptionsObject);
        }

        JsonArray dialogsUser = new JsonArray();

        userObject.add("user_dialogs", dialogsUser);

        for (Dialog dialog : src.getDialogs()){
            JsonObject dialogObject = new JsonObject();
            dialogObject.addProperty("dialog_name", dialog.getDialogName());
            dialogsUser.add(dialogObject);
        }

        return userObject;

    }

    private JsonObject baseInfoUser(User user){
        JsonObject userObject = new JsonObject();

        userObject.addProperty("user_name", user.getUsername());
        userObject.addProperty("id", user.getId());
        userObject.addProperty("points", user.getPoints());
        userObject.addProperty("status", user.getOnline());

        return userObject;
    }
}
