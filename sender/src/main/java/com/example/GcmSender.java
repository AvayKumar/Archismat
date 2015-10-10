package com.example;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GcmSender {

    public static final String API_KEY = "AIzaSyDcaddc3qtxkGYruv6Y2Ue4dgRKgMHW-vQ";

    public static void main(String[] args) {

        if (args.length < 1 || args.length > 2 || args[0] == null) {
            System.err.println("usage: ./gradlew run -Pargs=\"MESSAGE[,DEVICE_TOKEN]\"");
            System.err.println("");
            System.err.println("Specify a test message to broadcast via GCM. If a device's GCM registration token is\n" +
                    "specified, the message will only be sent to that device. Otherwise, the message \n" +
                    "will be sent to all devices subscribed to the \"global\" topic.");
            System.err.println("");
            System.err.println("Example (Broadcast):\n" +
                    "On Windows:   .\\gradlew.bat run -Pargs=\"<Your_Message>\"\n" +
                    "On Linux/Mac: ./gradlew run -Pargs=\"<Your_Message>\"");
            System.err.println("");
            System.err.println("Example (Unicast):\n" +
                    "On Windows:   .\\gradlew.bat run -Pargs=\"<Your_Message>,<Your_Token>\"\n" +
                    "On Linux/Mac: ./gradlew run -Pargs=\"<Your_Message>,<Your_Token>\"");
            System.exit(1);
        }

        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            String dataAlert = "{\"type\": \"0\", \"message\":\"Test alert message. Test alert message. Test alert message. Test alert message. Test alert message. Test alert message.\"}";
            String dataEvent = "{\"type\": \"1\", \"name\":\"Insomania 9 PM\", \"location\":\"NIT Rourkela, BBA\",\"desc\":\"Event description. Event description. Event description. Event description.\",\"long\":\"84.900676\",\"lat\":\"22.251738\"}";
            String dataPic = "{\"type\": \"2\", \"url\":\"http://www.wired.com/images_blogs/rawfile/2013/11/offset_WaterHouseMarineImages_62652-2-660x440.jpg\",\"desc\":\"Start by doing what's necessary ; then do what's possible; and suddenly you are doing the impossible.\"}";
            jData.put("message", dataEvent);
            // Where to send GCM message.
            if (args.length > 1 && args[1] != null) {
                jGcmData.put("to", args[1].trim());
            } else {
                jGcmData.put("to", "/topics/archismat");
                //jGcmData.put("to", "fHVpu62JRuY:APA91bGJXUZ4nV66q3LTZOPVl4MH483w1IiLzxDT_KZpwOYqKZGEUrCWoizL5hetH_V_FnypFMJ1rUFNb9KcgH5VECAyoyABU4q_L0BQLUDypeiKutok5yPrw10SLZ4_y1vPsru5e2q6");
            }

            // What to send in GCM message.
            jGcmData.put("data", jData);

            System.out.println( "Json Data" + jGcmData.toString() );

            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput( true );

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        }
    }

}
