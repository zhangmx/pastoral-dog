package com.zmx.mylibrary.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

//import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.InetAddresses;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

//import android.net.wifi.WifiManager;
//import android.content.Context;
import android.util.Patterns;

import java.net.InetAddress;
import java.nio.ByteOrder;


/**
 * @author wuyexiong
 * Device相关信息获取类
 */
public class DeviceUtil {
    private DeviceUtil() {
    }

    /**
     * 获取设备ID，优先返回IMEI，其次ANDROID，最后随机生成一个UUID
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String deviceId = null;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            deviceId = manager.getDeviceId();
        }
        if (deviceId != null) {
            return "IMEI:" + deviceId;
        }
        deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if (deviceId != null) {
            return "ANDROID_ID:" + deviceId;
        }
        deviceId = Installation.id(context);
        return "UUID:" + deviceId;
    }

    /**
     * 获取设备的MAC地址
     *
     * @param context
     * @return
     */
    public static String getMACAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        @SuppressLint("MissingPermission") String macAddress = wInfo.getMacAddress();
        if (!TextUtils.isEmpty(macAddress)) {
            return macAddress.toUpperCase().trim();
        }
        String interfaceNames[] = new String[]{"eth0", "wlan0"};
        macAddress = getMACAddress(interfaceNames[0]);
        if (!TextUtils.isEmpty(macAddress)) {
            return macAddress.toUpperCase().trim();
        }
        macAddress = getMACAddress(interfaceNames[1]);
        if (!TextUtils.isEmpty(macAddress)) {
            return macAddress.toUpperCase().trim();
        }
        return "-";
    }

    /**
     * 获取设备的Mac地址
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    @SuppressLint("NewApi")
    private static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)
                    return null;
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
            try { // this is so Linux hack return
                return loadFileAsString("/sys/class/net/" + interfaceName + "/address").toUpperCase().trim();
            } catch (IOException e) {
                return null;
            }
        } // for now eat exceptions
        return null;
    }

    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = false;
//                        boolean isIPv4 = Patterns.IP_ADDRESS.matcher(sAddr).matches();


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            isIPv4 = InetAddresses.isNumericAddress(sAddr);
                        } else {
                            isIPv4 = Patterns.IP_ADDRESS.matcher(sAddr).matches();
                        }


//                        boolean isIPv4 = InetAddresses.isInetAddress(sAddr);
//                        boolean isIPv4 = IPAddressUtil.isIPv4LiteralAddress(sAddr);
//                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port
                                // suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "-";
    }

    /**
     * 1.using getHostAddress : ***** IP=fe80::65ca:a13d:ea5a:233d%rmnet_sdio0
     * 2.using hashCode and Formatter : ***** IP=238.194.77.212
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "-";
    }

    /**
     * 获取设备的品牌
     *
     * @return
     */
    public static String getDeviceBrand() {
        String brand = Build.BRAND;
        if (TextUtils.isEmpty(brand)) {
            brand = "-";
        }

        return brand;
    }

    /**
     * 获取设备的型号
     *
     * @return
     */
    public static String getDeviceModel() {
        String model = Build.MODEL;
        if (TextUtils.isEmpty(model)) {
            model = "-";
        }
        return model;
    }

    /**
     * @return 设备SDK版本号
     */
    public static String getDeviceSDKVersion() {
        String version = String.valueOf(Build.VERSION.SDK_INT);
        if (TextUtils.isEmpty(version)) {
            version = "-";
        }

        return version;
    }

    /**
     * 获取当前应用的版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = pInfo.versionCode;
        } catch (Exception e) {
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * 获取当前应用的版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = pInfo.versionName;
        } catch (Exception e) {
            versionName = "";
        }
        return versionName;
    }

    /**
     * 获取友盟渠道名
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        String channelName = null;

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            channelName = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            channelName = "noChannel";
        }
        return channelName;
    }


    /**
     * Convert byte array to hex string
     *
     * @param bytes toConvert
     * @return hexValue
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     *
     * @param str which to be converted
     * @return array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     *
     * @param filename which to be converted to string
     * @return String value of File
     * @throws java.io.IOException if error occurs
     */
    public static String loadFileAsString2(String filename) throws java.io.IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress2(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress2(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }


    public String getLocalIpAddress3() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("SocketException", ex.toString());
        }
        return null;
    }


//    private String getLocalWifiIpAddress() {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
//        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
//
//        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
//            ipAddress = Integer.reverseBytes(ipAddress);
//        }
//
//        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
//
//        String ipAddressString;
//        try {
//            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
//        } catch (UnknownHostException ex) {
//            ipAddressString = null;
//        }
//
//        return ipAddressString;
//    }


//    public String getWifiIpSimpleVersion() {
//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//        return ip;
//    }


}