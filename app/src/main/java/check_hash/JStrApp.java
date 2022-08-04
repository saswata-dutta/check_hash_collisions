package check_hash;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JStrApp {
    public static void main(String[] args) {
        Gson gson = new Gson();
        String input = "{\n" +
                "    \"message\": \"someName someLastName has sent you a question\",\n" +
                "    \"parameters\": \"{\\\"firstName\\\":\\\"someName\\\",\\\"lastName\\\":\\\"someLastName\\\"}\",\n" +
                "    \"id\": 141\n" +
                "}";

        System.out.println(input);
        JsonObject messageJobj = gson.fromJson(input, JsonObject.class);

        String paramStr =
                messageJobj.getAsJsonPrimitive("parameters").getAsString();

        JsonObject paramObj = gson.fromJson(paramStr, JsonObject.class);
        System.out.println(paramObj);

        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> params = gson.fromJson(paramStr, mapType);
        System.out.println(params);
    }
}
