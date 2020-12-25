package me.kuku.yuq.utils;

import com.IceCreamQAQ.Yu.util.OkHttpWebImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.message.*;
import com.icecreamqaq.yuq.mirai.MiraiBot;
import com.icecreamqaq.yuq.mirai.message.ImageReceive;
import me.kuku.yuq.entity.QQLoginEntity;
import okhttp3.Cookie;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotUtils {

    public static String shortUrl(String url){
        try {
            JSONObject jsonObject = OkHttpUtils.getJson("https://tb.am/action/json-url.php?url_long=" + URLEncoder.encode(url, "utf-8"));
            if (jsonObject.getInteger("statusCode") == 200){
                return jsonObject.getString("shorturl");
            }else return jsonObject.getString("message");
        } catch (IOException e) {
            e.printStackTrace();
            return "缩短失败，原链接：" + url;
        }
    }

    public static String regex(String regex, String text){
        Matcher matcher = Pattern.compile(regex).matcher(text);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    public static String regex(String first, String last, String text){
        String regex = String.format("(?<=%s).*?(?=%s)", first, last);
        return regex(regex, text);
    }

    private static String random(String str, int length){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++){
            int at = (int) (Math.random() * str.length());
            result.append(str.charAt(at));
        }
        return result.toString();
    }

    public static String randomStr(int length){
        return random("1234567890abcdefghijklmnopqrstuvwxyz", length);
    }

    public static String randomNum(int length){
        return random("1234567890", length);
    }

    public static Long randomLong(long min, long max){
        return ((long) (Math.random() * max)) % (max - min + 1) + min;
    }

    public static int randomInt(int min, int max){
        return ((int) (Math.random() * max)) % (max - min + 1) + min;
    }

    public static QQLoginEntity toQQLoginEntity(OkHttpWebImpl web, MiraiBot miraiBot){
        ConcurrentHashMap<String, Map<String, Cookie>> map = web.getDomainMap();
        Map<String, Cookie> qunMap = map.get("qun.qq.com");
        String groupPsKey = qunMap.get("p_skey").value();
        Map<String, Cookie> qqMap = map.get("qq.com");
        String sKey = qqMap.get("skey").value();
        Map<String, Cookie> qZoneMap = map.get("qzone.qq.com");
        String psKey = qZoneMap.get("p_skey").value();
        return new QQLoginEntity(null, FunKt.getYuq().getBotId(), 0L, "", sKey, psKey, groupPsKey, miraiBot.superKey,
                QQUtils.getToken(miraiBot.superKey).toString(), null, true);
    }

    public static JSONArray messageToJsonArray(Message rm){
        if (rm == null) return null;
        ArrayList<MessageItem> body = rm.getBody();
        JSONArray aJsonArray = new JSONArray();
        for (MessageItem messageItem: body){
            JSONObject aJsonObject = new JSONObject();
            if (messageItem instanceof Text){
                Text text = (Text) messageItem;
                aJsonObject.put("type", "text");
                aJsonObject.put("content", text.getText());
            }else if (messageItem instanceof ImageReceive){
                ImageReceive image = (ImageReceive) messageItem;
                aJsonObject.put("type", "image");
                aJsonObject.put("id", image.getId());
                aJsonObject.put("content", image.getUrl());
            }else if (messageItem instanceof Face){
                Face face = (Face) messageItem;
                aJsonObject.put("type", "face");
                aJsonObject.put("content", face.getFaceId());
            }else if (messageItem instanceof At){
                At at = (At) messageItem;
                aJsonObject.put("type", "at");
                aJsonObject.put("content", at.getUser());
            }else if (messageItem instanceof XmlEx){
                XmlEx xmlEx = (XmlEx) messageItem;
                aJsonObject.put("type", "xml");
                aJsonObject.put("content", xmlEx.getValue());
                aJsonObject.put("serviceId", xmlEx.getServiceId());
            }else if (messageItem instanceof JsonEx){
                JsonEx jsonEx = (JsonEx) messageItem;
                aJsonObject.put("type", "at");
                aJsonObject.put("content", jsonEx.getValue());
            }else continue;
            aJsonArray.add(aJsonObject);
        }
        return aJsonArray;
    }

    public static Message jsonArrayToMessage(JSONArray jsonArray){
        Message msg = Message.Companion.toMessage("");
        MessageItemFactory mif = FunKt.getMif();
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject aJsonObject = jsonArray.getJSONObject(i);
            switch (aJsonObject.getString("type")){
                case "text":
                    msg.plus(aJsonObject.getString("content"));
                    break;
                case "image":
                    msg.plus(mif.imageByUrl(aJsonObject.getString("content")));
                    break;
                case "face":
                    msg.plus(mif.face(aJsonObject.getInteger("content")));
                    break;
                case "at":
                    msg.plus(mif.at(aJsonObject.getLong("content")));
                    break;
                case "xml":
                    msg.plus(mif.xmlEx(aJsonObject.getInteger("serviceId"), aJsonObject.getString("content")));
                    break;
                case "json":
                    msg.plus(mif.jsonEx(aJsonObject.getString("content")));
                    break;
            }
        }
        return msg;
    }

    public static boolean equalsMessageJsonArray(JSONArray jsonArray1, JSONArray jsonArray2){
        if (jsonArray1 == null || jsonArray2 == null) return false;
        if (jsonArray1.size() != jsonArray2.size()) return false;
        for (int i = 0; i < jsonArray1.size(); i++){
            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
            JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
            String type1 = jsonObject1.getString("type");
            String type2 = jsonObject2.getString("type");
            if (type1 == null || type2 == null) return false;
            if (!type1.equals(type2)) return false;
            if (type1.equals("image")){
                if (!jsonObject1.getString("id").equals(jsonObject2.getString("id"))) return false;
            }else {
                if (!jsonObject1.getString("content").equals(jsonObject2.getString("content"))) return false;
            }
        }
        return true;
    }

    public static JSONArray delManager(JSONArray jsonArray, String content){
        for (int i = 0; i < jsonArray.size(); i++){
            String str = jsonArray.getString(i);
            if (content.equals(str)){
                jsonArray.remove(str);
                break;
            }
        }
        return jsonArray;
    }

    public static JSONArray delMonitorList(JSONArray jsonArray, String username){
        List<JSONObject> list = new ArrayList<>();
        jsonArray.forEach(obj -> {
            JSONObject jsonObject = (JSONObject) obj;
            if (username.equals(jsonObject.getString("name"))) list.add(jsonObject);
        });
        list.forEach(jsonArray::remove);
        return jsonArray;
    }

    public static List<JSONObject> match(JSONArray jsonArray, String userId){
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (userId.equals(jsonObject.getString("id"))) list.add(jsonObject);
        }
        return list;
    }

}
