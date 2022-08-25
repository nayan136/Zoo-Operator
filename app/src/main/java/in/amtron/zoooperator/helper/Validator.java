package in.amtron.zoooperator.helper;

import android.util.Log;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator {

    private HashMap<EditText, String> validationData = new HashMap<EditText, String>();
    private boolean hasNoError = true;

    public void setData(EditText et, String rule) {
        validationData.put(et,rule);
    }

    public boolean validate(){
        hasNoError = true;
        for (Map.Entry<EditText, String> entry : validationData.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
            validateEditText(entry.getKey(),entry.getValue());
        }
        return hasNoError;
    }

    private void validateEditText(EditText et, String rule) {
        Log.i("Validation - Error",rule);
        List<String> ruleArray = getRules(rule);
        String editTextData = et.getText().toString().trim();
        for(int i=0;i<ruleArray.size();i++){
            if(ruleArray.get(i).equals("required")){
                if(editTextData.length() == 0){
                    et.setError("Please enter a value");
                    hasNoError = false;
                    break;
                }
            }else if(ruleArray.get(i).equals("mobile")){
                if(editTextData.length() != 10){
                    et.setError("Please enter a valid mobile number");
                    hasNoError = false;
                    break;
                }
            }else if(ruleArray.get(i).equals("name")){
                if(Arrays.asList(editTextData.split(" ")).size() < 2){
                    et.setError("Please enter a valid name");
                    hasNoError = false;
                    break;
                }
            }else if(ruleArray.get(i).equals("age")){
                if(Integer.parseInt(editTextData) > 120){
                    et.setError("Please enter a valid age");
                    hasNoError = false;
                    break;
                }
            }else if(ruleArray.get(i).equals("orderId")){
                if(editTextData.length() != 35){
                    et.setError("Please enter a valid order id");
                    hasNoError = false;
                    break;
                }
            }
            Log.i("Validation - Error",String.valueOf(hasNoError));
        }
    }

    private List<String> getRules(String rule){
        return Arrays.asList(rule.split(","));
    }
}
