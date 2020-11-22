package me.kuku.yuq;

import com.IceCreamQAQ.Yu.loader.AppClassloader;
import com.IceCreamQAQ.Yu.util.IO;
import com.icecreamqaq.yuq.YuQStarter;
import me.kuku.yuq.utils.OkHttpUtils;
import okhttp3.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Start {
    public static void main(String[] args) {
        // 从conf文件夹拉设备信息到根目录
        String deviceName = "device.json";
        File confDeviceFile = new File("conf/" + deviceName);
        File rootDeviceFile = new File(deviceName);
        if (confDeviceFile.exists() && !rootDeviceFile.exists()){
            try {
                IO.writeFile(rootDeviceFile, IO.read(new FileInputStream(confDeviceFile), true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // 如果没有配置文件，下载配置文件
        File confFile = new File("conf");
        if (!confFile.exists()) confFile.mkdir();
        File yuqFile = new File("conf/YuQ.properties");
        if (!yuqFile.exists()){
            try {
                Response response = OkHttpUtils.get("https://ty.kuku.me/kuku/bot/YuQ.properties");
                response.close();
                String url = response.header("location");
                byte[] bytes = OkHttpUtils.getBytes(url);
                IO.writeFile(yuqFile, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AppClassloader.registerTransformerList("com.IceCreamQAQ.Yu.web.WebClassTransformer");
        YuQStarter.start();
    }
}