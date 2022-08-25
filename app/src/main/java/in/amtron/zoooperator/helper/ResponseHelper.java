package in.amtron.zoooperator.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import in.amtron.zoooperator.model.ResponseData;

public class ResponseHelper {

    private JsonObject jsonObject;

    public ResponseHelper(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean isStatusSuccessful(){
        return Integer.parseInt(String.valueOf(jsonObject.get("status"))) == 1;
    }

    public String getDataAsString(){
        return String.valueOf(jsonObject.get("data"));
    }

    public ResponseData getData(){
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.toString(),ResponseData.class);
    }

    public String getErrorMsg(){
        return String.valueOf(jsonObject.get("msg"));
    }
}
