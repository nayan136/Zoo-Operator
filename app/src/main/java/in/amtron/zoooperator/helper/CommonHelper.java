package in.amtron.zoooperator.helper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class CommonHelper {

    public static final String DATABASE_NAME="zoodatabase";

    public static String getIPAddress() {
//        try {
//            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface intf : interfaces) {
//                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
//                for (InetAddress addr : addrs) {
//                    if (!addr.isLoopbackAddress()) {
//                        String sAddr = addr.getHostAddress().toUpperCase();
//                        boolean isIPv4 = Inet4Address.isIPv4Address(sAddr);
//                        if (useIPv4) {
//                            if (isIPv4)
//                                return sAddr;
//                        } else {
//                            if (!isIPv4) {
//                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
//                                return delim<0 ? sAddr : sAddr.substring(0, delim);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "162.148.12.5";
    }

    public static String getOsVersion(){
        return String.valueOf(android.os.Build.VERSION.SDK_INT);
    }

    public static String getApiKey(){
        return "m0b!leap!@Z@@";
    }

    public static String getImei(){
        return "No IMEI 001";
    }
}
