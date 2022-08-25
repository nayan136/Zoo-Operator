package in.amtron.zoooperator.helper;

import com.google.gson.Gson;

import in.amtron.zoooperator.model.User;

public class Util {

    public static String getCurrentMode(int i){
        if(i == 0){
            return "OFFLINE";
        }else if(i==1){
            return "ONLINE";
        }
        return "SERVER ERROR";
    }

    public static String objectToString(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String moneyFormat(double price){
        return "₹ "+moneyFormat1(price);
    }

    public static String moneyFormat1(double price){
//        return  String.valueOf(price)+".00";
        return String.format("%.2f", price);
    }

    public static double money(double price){
        String newPrice = moneyFormat1(price);
        return Double.parseDouble(newPrice);
    }

    public static String moneyFormatRupee(float price){
        return  "Rs. "+moneyFormat1(price);
    }

}
