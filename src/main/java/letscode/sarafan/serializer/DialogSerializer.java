package letscode.sarafan.serializer;

import com.google.gson.*;
import letscode.sarafan.domain.Dialog;
import letscode.sarafan.domain.Message;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class DialogSerializer implements JsonSerializer<Dialog> {

    @Override
    public JsonElement serialize(Dialog src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject dialogObject = new JsonObject();

        dialogObject.addProperty("dialog_name", src.getDialogName());

        JsonArray messagesDialog = new JsonArray();

        for (int i = 0; i < src.getMessages().size(); i++){
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("sender_name", src.getMessages().get(i).getSenderName());
            messageObject.addProperty("receiver_name", src.getMessages().get(i).getReceiverName());
            messageObject.addProperty("text", src.getMessages().get(i).getText());
          // messageObject.addProperty("date", src.getMessages().get(i).getLocalDateTime().toString());
            messagesDialog.add(messageObject);
        }

        dialogObject.add("user_messages", messagesDialog);



        return dialogObject;
    }

    public static class AllDialogSerializer implements JsonSerializer<Dialog>{

        @Override
        public JsonElement serialize(Dialog src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject dialogObject = new JsonObject();

            dialogObject.addProperty("dialog_name", src.getDialogName());
            JsonArray messagesDialog = new JsonArray();
            JsonObject messageObject = new JsonObject();
            messageObject.addProperty("sender_name", src.getMessages().get(src.getMessages().size() - 1).getSenderName());
            messageObject.addProperty("receiver_name", src.getMessages().get(src.getMessages().size() - 1).getReceiverName());
            messageObject.addProperty("text", src.getMessages().get(src.getMessages().size() - 1).getText());
            messagesDialog.add(messageObject);

            dialogObject.add("last_message", messagesDialog);
            return dialogObject;
        }
    }
}
