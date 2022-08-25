package in.amtron.zoooperator.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static String getToday(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String getCurrentTime(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return df.format(c);
    }

    // string type dd/mm/yyyy
    public static int[] getDateArr(String date){
        int[] dateArr = new int[3];
        if(!isStringEmpty(date)){
            if(containParticularCharacter(date,"/")){
                String[] dateArrayStr =  date.split("/");
                for(int i = 0; i < dateArrayStr.length; i++) {
                    dateArr[i] = Integer.parseInt(dateArrayStr[i]);
                }
            }
        }
        return dateArr;
    }

    // 10/9/2021 -> 2021-09-10
    public static String mysqlDateFormat(String date){
        int[] dateArr = getDateArr(date);
        String zeroIfRequired = dateArr[1]<10?"0":"";
        String zeroIfRequiredOnDay = dateArr[0]<10?"0":"";
        return dateArr[2]+"-"+zeroIfRequired+dateArr[1]+"-"+zeroIfRequiredOnDay+dateArr[0];
    }

    // 2021-09-10 -> 10/09/2021
    public static String mysqlDateFormatToReadableFormat(String date){
        if(!isStringEmpty(date)){
            if(validateLengthOfString(date,10)){
                if(containParticularCharacter(date,"-")){
                    String[] dateArr =  date.split("-");
                    return dateArr[2]+"/"+dateArr[1]+"/"+dateArr[0];
                }
            }

        }
        return date;
    }

    //change 10/9/2021 -> 10/09/2021
    public static String getFormatDate(String date){
        if(!isStringEmpty(date)){
            if(containParticularCharacter(date,"/")){
                String[] dateArrayStr =  date.split("/");
                if(Integer.parseInt(dateArrayStr[0]) <10){
                    dateArrayStr[0] = "0"+Integer.parseInt(dateArrayStr[0]);
                }
                if(Integer.parseInt(dateArrayStr[1]) <10){
                    dateArrayStr[1] = "0"+Integer.parseInt(dateArrayStr[1]);
                }
                date = dateArrayStr[0]+"/"+dateArrayStr[1]+"/"+dateArrayStr[2];
            }
        }
        Log.i("Date Helper *********",date);
        return date;
    }

    // 10:00:00 -> 10:00 AM
    public static String getFormatTime(String time){
        if(!isStringEmpty(time)){
            if(validateLengthOfString(time,8)){
                if(containParticularCharacter(time,":")){
                    String[] timeArrayStr =  time.split(":");
                    String postpand = "AM";
                    int hour = Integer.parseInt(timeArrayStr[0]);
                    if(hour > 11 && hour<24){
                        if(hour>12){
                            timeArrayStr[0]  = String.valueOf(Integer.parseInt(timeArrayStr[0])-12);
                        }
                        postpand = "PM";
                    }
                    return timeArrayStr[0]+":"+timeArrayStr[1]+" "+postpand;
                }
            }
        }
        return time;
    }

    private static boolean isStringEmpty(String v){
        return v.isEmpty();
    }

    private static boolean validateLengthOfString(String v, int length){
        return v.length() == length;
    }

    private static boolean containParticularCharacter(String v, String needle){
        return v.contains(needle);
    }
}
