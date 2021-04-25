package me.kuku.yuq.controller;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.IceCreamQAQ.Yu.annotation.Config;
import com.IceCreamQAQ.Yu.annotation.Synonym;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.annotation.GroupController;
import com.icecreamqaq.yuq.annotation.PathVar;
import com.icecreamqaq.yuq.annotation.QMsg;
import com.icecreamqaq.yuq.controller.ContextSession;
import com.icecreamqaq.yuq.entity.Group;
import com.icecreamqaq.yuq.entity.Member;
import com.icecreamqaq.yuq.job.RainInfo;
import com.icecreamqaq.yuq.message.*;
import me.kuku.yuq.entity.ConfigEntity;
import me.kuku.yuq.entity.GroupEntity;
import me.kuku.yuq.logic.*;
import me.kuku.yuq.pojo.CodeType;
import me.kuku.yuq.pojo.ConfigType;
import me.kuku.yuq.pojo.DCloudPojo;
import me.kuku.yuq.pojo.Result;
import me.kuku.yuq.service.ConfigService;
import me.kuku.yuq.service.GroupService;
import me.kuku.yuq.service.MessageService;
import me.kuku.yuq.utils.*;
import okhttp3.Response;
import org.smartboot.http.utils.StringUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@GroupController
public class ToolController {
    @Inject
    private ToolLogic toolLogic;
    @Inject
    private GroupService groupService;
    @Inject
    private AILogic qqAILogic;
    @Inject
    @Named("baiduAILogic")
    private AILogic baiduAILogic;
    @Inject
    private ConfigService configService;
    @Inject
    private MessageService messageService;
    @Inject
    private RainInfo rainInfo;
    @Inject
    private MessageItemFactory mif;
    @Inject
    private MyApiLogic myApiLogic;
    @Config("YuQ.Mirai.protocol")
    private String protocol;
    @Inject
    private TeambitionLogic teambitionLogic;
    @Inject
    private DCloudLogic dCloudLogic;
    Long recallTime = 0L;
    @Config("YuQ.Mirai.bot.master")
    private String master;
    @Config("YuQ.Mirai.bot.api")
    private String api;

    private final LocalDateTime startTime;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

    public ToolController() {
        startTime = LocalDateTime.now();
    }

    @QMsg(at = true)
    @Action("百度 {content}")
    public String teachYouBaidu(String content) throws IOException {
        return toolLogic.teachYou(content, "baidu");
    }

    @QMsg(at = true)
    @Action("谷歌 {content}")
    public String teachYouGoogle(String content) throws IOException {
        return toolLogic.teachYou(content, "google");
    }

    @QMsg(at = true)
    @Action("bing {content}")
    public String teachYouBing(String content) throws IOException {
        return toolLogic.teachYou(content, "bing");
    }

    @QMsg(at = true)
    @Action("搜狗 {content}")
    public String teachYouSouGou(String content) throws IOException {
        return toolLogic.teachYou(content, "sougou");
    }

    @QMsg(at = true, atNewLine = true)
    @Action("舔狗日记")
    public String dogLicking() throws IOException {
        return toolLogic.dogLicking();
    }

    @QMsg(at = true, atNewLine = true)
    @Action("百科 {params}")
    public String baiKe(String params) throws IOException {
        return toolLogic.baiKe(params);
    }

    @QMsg(at = true, atNewLine = true)
    @Action("嘴臭")
    @Synonym({"祖安语录"})
    public String mouthOdor() throws IOException {
        return toolLogic.mouthOdor();
    }

    @QMsg(at = true)
    @Action("毒鸡汤")
    public String poisonousChickenSoup() throws IOException {
        return toolLogic.poisonousChickenSoup();
    }

    @QMsg(at = true)
    @Action("名言")
    public String saying() throws IOException {
        return toolLogic.saying();
    }

    @QMsg(at = true)
    @Action("一言")
    public String hiToKoTo() throws IOException {
        return toolLogic.hiToKoTo().get("text");
    }

    @Action("缩短/{params}")
    @QMsg(at = true)
    public String shortUrl(String params) {
        return BotUtils.shortUrl(params);
    }

    @Action("ip/{params}")
    @QMsg(at = true)
    public String queryIp(String params) throws IOException {
        return toolLogic.queryIp(params);
    }

    @Action("whois/{params}")
    @QMsg(at = true, atNewLine = true)
    public String queryWhois(String params) throws IOException {
        return toolLogic.queryWhois(params);
    }

    @Action("icp/{params}")
    @QMsg(at = true, atNewLine = true)
    public String queryIcp(String params) throws IOException {
        return toolLogic.queryIcp(params);
    }

    @Action("知乎日报")
    @QMsg(at = true, atNewLine = true)
    public String zhiHuDaily() throws IOException {
        return toolLogic.zhiHuDaily();
    }

    @QMsg(at = true, atNewLine = true)
    @Action("测吉凶")
    public String qqGodLock(long qq) throws IOException {
        return toolLogic.qqGodLock(qq);
    }

    @QMsg(at = true, atNewLine = true)
    @Action("拼音/{params}")
    public String convertPinYin(String params) throws IOException {
        return toolLogic.convertPinYin(params);
    }

//    @QMsg(at = true, atNewLine = true)
//    @Action("笑话")
//    public String jokes() throws IOException {
//        return toolLogic.jokes();
//    }

    @QMsg(at = true, atNewLine = true)
    @Action("垃圾/{params}")
    public String rubbish(String params) throws IOException {
        return toolLogic.rubbish(params);
    }

    @Action("解析/{url}")
    @QMsg(at = true, atNewLine = true)
    public String parseVideo(String url) throws IOException {
        return toolLogic.parseVideo(url);
    }

    @Action("ping/{domain}")
    @QMsg(at = true, atNewLine = true)
    public String ping(String domain) throws IOException {
        return toolLogic.ping(domain);
    }

//    @Action("搜 {question}")
//    @QMsg(at = true)
//    public String search(String question) throws IOException {
//        return toolLogic.searchQuestion(question);
//    }


    @Action("撤回时间 {recallTime}")
    @QMsg(at = true)
    public String setRecallTime(long group, String recallTime) {
        GroupEntity byGroup = groupService.findByGroup(group);
        byGroup.setRecallTime(Long.parseLong(recallTime));
        groupService.save(byGroup);
        return "撤回时间修改成功";
    }

    @Action("色图五连")
    public void tenColorPic(Group group, long qq) throws IOException {
        for (int i = 0; i < 5; i++) {
            colorPic(group, qq);
        }
    }

    @Action("色图")
    @Synonym({"涩图来", "涩图", "来份涩图"})
    public void colorPic(Group group, long qq) {
        GroupEntity groupEntity = groupService.findByGroup(group.getId());
        if (groupEntity == null || groupEntity.getColorPic() == null || Boolean.FALSE.equals(groupEntity.getColorPic())) {
            group.sendMessage(FunKt.getMif().at(qq).plus("该功能已关闭！！"));
            return;
        }
        String type = groupEntity.getColorPicType();
        if (type == null) {
            group.sendMessage(FunKt.getMif().at(qq).plus("机器人没有配置色图类型，无法获取！！"));
            return;
        }
        ExecutorUtils.execute(() -> {
            try {
                if ("lolicon".equals(type) || "loliconR18".equals(type)) {
                    ConfigEntity configEntity = configService.findByType("loLiCon");
                    if (configEntity == null) {
                        group.sendMessage(FunKt.getMif().at(qq).plus("机器人还没有配置lolicon的apiKey，无法获取色图！！"));
                        return;
                    }
                    String apiKey = configEntity.getContent();
                    Result<Map<String, String>> result = toolLogic.colorPicByLoLiCon(apiKey, type.equals("loliconR18"), false);
                    Map<String, String> map = result.getData();
                    if (map == null) {
                        MessageSource messageSource = group.sendMessage(FunKt.getMif().at(qq).plus(result.getMessage()));
                        //撤回
                        new Thread(() -> {
                            try {
                                TimeUnit.SECONDS.sleep((groupEntity.getRecallTime() == null ? recallTime : groupEntity.getRecallTime()));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            messageSource.recall();
                        }).start();
                        return;
                    }
                    byte[] by = toolLogic.piXivPicProxy(map.get("url"));
                    MessageSource messageSource = group.sendMessage(FunKt.getMif().imageByInputStream(new ByteArrayInputStream(by)).toMessage());
                    //撤回
                    new Thread(() -> {
                        try {
                            TimeUnit.SECONDS.sleep((groupEntity.getRecallTime() == null ? recallTime : groupEntity.getRecallTime()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        messageSource.recall();
                    }).start();
                } else if (type.contains("danbooru")) {
                    String[] arr = type.split("-");
                    String danType = null;
                    if (arr.length > 1) {
                        danType = arr[1];
                    }
                    String url;
                    if (danType == null) {
                        url = "https://api.kuku.me/danbooru";
                    } else {
                        url = "https://api.kuku.me/danbooru?type=" + danType;
                    }
                    Response response = OkHttpUtils.get(url);
                    if (response.header("content-type") != null) {
                        group.sendMessage(FunKt.getMif().at(qq).plus("danbooru的tags类型不匹配，请重新设置tags类型，具体tag类型可前往https://danbooru.donmai.us/" +
                                "查看，如果tag中带空格，请用_替换"));
                    } else {
                        byte[] bytes = OkHttpUtils.getBytes(response);
                        MessageSource messageSource = group.sendMessage(FunKt.getMif().imageByInputStream(new ByteArrayInputStream(bytes)).toMessage());
                        //撤回
                        new Thread(() -> {
                            try {
                                TimeUnit.SECONDS.sleep((groupEntity.getRecallTime() == null ? recallTime : groupEntity.getRecallTime()));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            messageSource.recall();
                        }).start();
                    }
                } else {
                    MessageSource messageSource = group.sendMessage(Message.Companion.toMessage("色图类型不匹配！！"));
                    //撤回
                    new Thread(() -> {
                        try {
                            TimeUnit.SECONDS.sleep((groupEntity.getRecallTime() == null ? recallTime : groupEntity.getRecallTime()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        messageSource.recall();
                    }).start();
                }
            } catch (Exception e) {
                group.sendMessage(FunKt.getMif().at(qq).plus("色图获取失败，请重试！异常信息为：" + e.getMessage()));
            }
        });
    }


    @Action("看美女")
    public Image girl() throws IOException {
        return FunKt.getMif().imageByUrl(toolLogic.girlImage());
    }

    @QMsg(at = true)
    @Action("蓝奏 {url}")
    public String lanZou(String url, @PathVar(2) String pwd) throws UnsupportedEncodingException {
        String resultUrl;
        if (pwd == null) {
            resultUrl = "https://v1.alapi.cn/api/lanzou?url=" + URLEncoder.encode(url, "utf-8");
        } else {
            resultUrl = "https://v1.alapi.cn/api/lanzou?url=" + URLEncoder.encode(url, "utf-8") + "&pwd=" + pwd;
        }
        return BotUtils.shortUrl(resultUrl);
    }

    @Action("lol周免")
    @QMsg(at = true, atNewLine = true)
    public String lolFree() throws IOException {
        return toolLogic.lolFree();
    }

    @Action("缩写/{content}")
    @QMsg(at = true, atNewLine = true)
    public String abbreviation(String content) throws IOException {
        return toolLogic.abbreviation(content);
    }

    @Action("几点了")
    public Image time() throws IOException {
        return FunKt.getMif().imageByInputStream(new ByteArrayInputStream(toolLogic.queryTime()));
    }

    @Action("\\^BV.*\\")
    @Synonym({"\\^bv.*\\"})
    @QMsg(at = true)
    public Message bvToAv(Message message) throws IOException {
        String bv = message.getBody().get(0).toPath();
        Result<Map<String, String>> result = toolLogic.bvToAv(bv);
        if (result.getCode() == 200) {
            Map<String, String> map = result.getData();
            MessageItemFactory mif = FunKt.getMif();
            return mif.imageByUrl(map.get("pic")).plus(
                    "标题：" + map.get("title") + "\n" +
                            "描述：" + map.get("desc") +
                            "链接：" + map.get("url")
            );
        } else {
            return Message.Companion.toMessage(result.getMessage());
        }
    }

    @Action("分词")
    @QMsg(at = true, atNewLine = true)
    public String wordSegmentation(long qq, ContextSession session, Group group) throws IOException {
        group.sendMessage(FunKt.getMif().at(qq).plus("请输入需要中文分词的内容！！"));
        Message nextMessage = session.waitNextMessage();
        return toolLogic.wordSegmentation(Message.Companion.firstString(nextMessage));
    }

    @Action("acg")
    public Image acgPic() throws IOException {
        return FunKt.getMif().imageByUrl(toolLogic.acgPic());
    }

    @Action("搜图 {img}")
    @QMsg(at = true)
    public Message searchImage(Image img) throws IOException {
        ConfigEntity configEntity = configService.findByType(ConfigType.SauceNao.getType());
        if (configEntity == null) {
            return BotUtils.toMessage("机器人没有配置搜图（sauceNao）的apikey，无法搜图！！");
        }
        String url = toolLogic.sauceNaoIdentifyPic(configEntity.getContent(), img.getUrl());
        if (url != null) {
            return FunKt.getMif().imageByUrl(img.getUrl()).plus(url);
        } else {
            return Message.Companion.toMessage("没有找到这张图片！！！");
        }
    }

    @Action("OCR {img}")
    @Synonym({"ocr {img}"})
    @QMsg(at = true, atNewLine = true)
    public String ocr(Image img) throws IOException {
        return baiduAILogic.generalOCR(img.getUrl());
    }

    @Action("查发言数")
    public String queryMessage(Group group) {
        Map<Long, Long> map = messageService.findCountQQByGroupAndToday(group.getId());
        StringBuilder sb = new StringBuilder().append("本群今日发言数统计如下：").append("\n");
        for (Map.Entry<Long, Long> entry : map.entrySet()) {
            sb.append("@").append(group.get(entry.getKey()).nameCardOrName())
                    .append("（").append(entry.getKey()).append("）").append("：")
                    .append(entry.getValue()).append("条").append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    @Action("语音合成 {text}")
    public Message voice(String text, Group group, long qq) throws IOException {
        Result<byte[]> result = qqAILogic.voiceSynthesis(text);
        if (result.getCode() == 200) {
            return FunKt.getMif().voiceByByteArray(result.getData()).toMessage();
        } else {
            return FunKt.getMif().at(qq).plus(result.getMessage());
        }
    }

    @Action("点歌 {name}")
    public Object musicFromQQ(String name) throws IOException {
        String xmlStr = toolLogic.songByQQ(name);
        return mif.xmlEx(2, xmlStr);
    }

    @Action("163点歌 {name}")
    public Object musicFrom163(String name) throws IOException {
        Result<String> xmlStr = toolLogic.songBy163(name);
        return mif.xmlEx(2, xmlStr.getData());
    }

    @Action("统计")
    @Synonym({"运行状态"})
    public String status() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 睡眠1s
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余
        long acaliableByte = memory.getAvailable();
        Properties props = System.getProperties();
        //系统名称
        String osName = props.getProperty("os.name");
        //架构名称
        String osArch = props.getProperty("os.arch");
        Runtime runtime = Runtime.getRuntime();
        //jvm总内存
        long jvmTotalMemoryByte = runtime.totalMemory();
        //jvm最大可申请
        long jvmMaxMoryByte = runtime.maxMemory();
        //空闲空间
        long freeMemoryByte = runtime.freeMemory();
        //jdk版本
        String jdkVersion = props.getProperty("java.version");
        //jdk路径
        String jdkHome = props.getProperty("java.home");
        LocalDateTime nowTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, nowTime);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        String ss = days + "天" + hours + "小时" + minutes + "分钟";
        return "程序运行时长：" + ss + "\n" +
                "cpu核数：" + processor.getLogicalProcessorCount() + "\n" +
                "cpu当前使用率：" + new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu)) + "\n" +
                "总内存：" + formatByte(totalByte) + "\n" +
                "已使用内存：" + formatByte(totalByte - acaliableByte) + "\n" +
                "操作系统：" + osName + "\n" +
                "系统架构：" + osArch + "\n" +
                "jvm内存总量：" + formatByte(jvmTotalMemoryByte) + "\n" +
                "jvm已使用内存：" + formatByte(jvmTotalMemoryByte - freeMemoryByte) + "\n" +
                "java版本：" + jdkVersion;
    }

    @Action("消息统计")
    public String message() {
        return "当前收发消息状态：\n" +
                "收：" + rainInfo.getCountRm() + " / 分钟\n" +
                "发：" + rainInfo.getCountSm() + " / 分钟\n" +
                "总计：\n" +
                "收：" + rainInfo.getCountRa() + " 条，\n" +
                "发：" + rainInfo.getCountSa() + " 条。";
    }

    private String formatByte(long byteNumber) {
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }


    @Action("抽象话 {word}")
    @QMsg(at = true)
    public String abstractWords(String word) {
        return "抽象话如下：\n" + toolLogic.abstractWords(word);
    }

    @Action("窥屏检测")
    public void checkPeeping(Group group) {
        String random = BotUtils.randomNum(4);
        group.sendMessage(FunKt.getMif().jsonEx("{\"app\":\"com.tencent.miniapp\",\"desc\":\"\",\"view\":\"notification\",\"ver\":\"1.0.0.11\",\"prompt\":\"QQ程序\",\"appID\":\"\",\"sourceName\":\"\",\"actionData\":\"\",\"actionData_A\":\"\",\"sourceUrl\":\"\",\"meta\":{\"notification\":{\"appInfo\":{\"appName\":\"三楼有只猫\",\"appType\":4,\"appid\":1109659848,\"iconUrl\":\"" + api + "\\/tool\\/peeping\\/check\\/" + random + "\"},\"button\":[],\"data\":[],\"emphasis_keyword\":\"\",\"title\":\"请等待15s\"}},\"text\":\"\",\"extraApps\":[],\"sourceAd\":\"\",\"extra\":\"\"}").toMessage());
        executorService.schedule(() -> {
            String msg;
            try {
                JSONObject jsonObject = OkHttpUtils.getJson(api + "/tool/peeping/result/" + random);
                if (jsonObject.getInteger("code") == 200) {
                    StringBuilder sb = new StringBuilder();
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
                    sb.append("检测到共有").append(jsonArray.size()).append("位小伙伴在窥屏").append("\n");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                        sb.append(singleJsonObject.getString("ip"))
                                .append("-").append(singleJsonObject.getString("address"))
                                /*.append("-").append(singleJsonObject.getString("simpleUserAgent"))*/.append("\n");
                    }
                    msg = BotUtils.removeLastLine(sb);
                } else {
                    msg = jsonObject.getString("message");
                }
            } catch (IOException e) {
                e.printStackTrace();
                msg = "查询失败，请重试！！";
            }
            group.sendMessage(FunKt.getMif().text(msg).toMessage());
        }, 15, TimeUnit.SECONDS);
    }

    @Action("code {type}")
    @QMsg(at = true, atNewLine = true)
    public String codeExecute(ContextSession session, Group group, long qq, String type) throws IOException {
        CodeType codeType = CodeType.parse(type);
        if (codeType == null) {
            return "没有找到这个语言类型！！";
        }
        group.sendMessage(FunKt.getMif().at(qq).plus("请输入代码！！"));
        Message message = session.waitNextMessage();
        String code = Message.Companion.firstString(message);
        return toolLogic.executeCode(code, codeType);
    }

    @Action("dcloud上传")
    @QMsg(at = true, atNewLine = true)
    public String dCloudUpload(Group group, long qq, ContextSession session) {
        ConfigEntity configEntity = configService.findByType(ConfigType.DCloud.getType());
        if (configEntity == null) {
            return "机器人还没有配置dCloud，请联系机器人主人进行配置。";
        }
        JSONObject jsonObject = configEntity.getContentJsonObject();
        group.sendMessage(FunKt.getMif().at(qq).plus("请发送需要上传的图片"));
        Message imageMessage = session.waitNextMessage();
        DCloudPojo dCloudPojo = new DCloudPojo(jsonObject.getString("cookie"), jsonObject.getString("token"));
        String spaceId = jsonObject.getString("spaceId");
        StringBuilder sb = new StringBuilder().append("您上传的图片链接如下：").append("\n");
        int i = 1;
        for (MessageItem item : imageMessage.getBody()) {
            if (item instanceof Image) {
                Image image = (Image) item;
                String url = image.getUrl();
                String id = image.getId();
                sb.append(i++).append("、");
                InputStream is = null;
                try {
                    Response response = OkHttpUtils.get(url);
                    is = OkHttpUtils.getByteStream(response);
                    long size = Objects.requireNonNull(response.body()).contentLength();
                    Result<String> result = dCloudLogic.upload(dCloudPojo, spaceId, id, is, Math.toIntExact(size));
                    if (result.getCode() == 502) {
                        Result<DCloudPojo> reResult = dCloudLogic.reLogin();
                        if (reResult.isFailure()) {
                            return "cookie失效，尝试重新登录，登录失败。" + reResult.getMessage();
                        } else {
                            dCloudPojo = reResult.getData();
                            result = dCloudLogic.upload(dCloudPojo, spaceId, id, OkHttpUtils.getBytes(url));
                        }
                    }
                    if (result.isFailure()) {
                        sb.append(result.getMessage()).append("\n");
                    } else {
                        sb.append(result.getData()).append("\n");
                    }
                } catch (IOException e) {
                    sb.append("网络出现异常，上传失败").append("\n");
                } finally {
                    IOUtils.close(is);
                }
            }
        }
        return BotUtils.removeLastLine(sb);
    }

    @Action("妹子图")
    public Object girlImage(long qq) {
        byte[] bytes = toolLogic.girlImageGaNk();
        if (bytes != null) {
            return FunKt.getMif().imageByByteArray(bytes);
        } else {
            return FunKt.getMif().at(qq).plus("图片获取失败，请重试！！");
        }
    }


    @Action("抽签")
    @QMsg(at = true)
    public String random(long qq, Group group, ContextSession session) throws IOException {
        int num = Jrrp.get(qq);
        JSONObject jsonObject = toolLogic.luckJson(num);
        group.sendMessage(FunKt.getMif().at(qq).plus("今日运势：\n" + jsonObject.getJSONObject("fields").getString("texk_key") + "\n发送解签查看详解"));
        Message nextMessage = session.waitNextMessage();
        String ss = BotUtils.firstString(nextMessage);
        if ("解签".equals(ss)) {
            return jsonObject.getJSONObject("fields").getString("text") + "\n凶签也不必气馁。人生势必起伏。";
        } else {
            return "您错过解签了！！";
        }
    }

    @Action("丢")
    @Synonym({"爬"})
    public Message diu(Member qq, @PathVar(1) String paramQQ, @PathVar(0) String type) {
        String url = qq.getAvatar();
        if (paramQQ != null) {
            if (!paramQQ.matches("^[0-9][0-9]*[0-9]$"))
                return FunKt.getMif().at(qq).plus("您输入的不为qq号，请重试！！");
            url = "http://q1.qlogo.cn/g?b=qq&nk=" + paramQQ + "&s=640";
        }
        byte[] bytes;
        if ("丢".equals(type))
            bytes = toolLogic.diu(url);
        else bytes = toolLogic.pa(url);
        if (bytes == null) return FunKt.getMif().at(qq).plus("图片生成失败，请重试！！");
        return FunKt.getMif().imageByByteArray(bytes).toMessage();
    }

    //@Action("\\.*\\")
    public String readMessage(@PathVar(0) String str) throws IOException {
        if (StringUtils.isBlank(str) || "图片".equals(str) || str.contains("表情_")) {
            return null;
        }
        return new Random().nextInt(100) > 95 ? toolLogic.qinYunKeChat(str) : null;
    }


    @Action("cosplay")
    @Synonym({"cosplay"})
    public Image cos() {
        return FunKt.getMif().imageByUrl("https://api.a1827.workers.dev/api.php");
    }

    public static void main(String[] args) {

    }
}
