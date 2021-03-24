package me.kuku.yuq.controller.ark;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

import static java.lang.Math.random;

public abstract class ArkNightsPool implements CardPool {
    String description() {
        return null;
    }

    Double sixProbability = 0.02;
    Double fiveProbability = 0.08;
    Double fourProbability = 0.5;

    Double getUpSixFloor() {
        return null;
    }

    Double getUpFiveFloor() {
        return null;
    }

    Double getUpFourFloor() {
        return null;
    }

    abstract List<String> getUpSix();

    abstract List<String> upFive();

    abstract List<String> upFour();

    abstract List<String> normalSix();

    abstract List<String> normalFive();

    abstract List<String> normalFour();

    List<String> rubbish() {
        return Arrays.asList("月见夜", "空爆", "芬", "香草", "翎羽", "玫兰莎", "卡缇", "米格鲁", "克洛斯", "炎熔", "芙蓉", "安赛尔", "史都华德", "梓兰", "斑点", "泡普卡");
    }

    @Override
    public String invoke(CardSettle cardSettle) {
        int level = cardSettle.getLevel();
        String name;
        switch (level) {
            case 7:
                name = getUpSix().get(new Random().nextInt(getUpSix().size()));
                return "⭐️" + name + "(" + cardSettle.getCount() + ")";
            case 6:
                name = normalSix().get((new Random().nextInt(normalSix().size())));
                return "⭐️" + name + "(" + cardSettle.getCount() + ")";
            case 5:
                name = upFive().get((new Random().nextInt(upFive().size())));
                return "✨" + name + "(" + cardSettle.getCount() + ")";
            case 4:
                name = normalFive().get((new Random().nextInt(normalFive().size())));
                return "✨" + name + "(" + cardSettle.getCount() + ")";
            case 3:
                name = upFour().get((new Random().nextInt(upFour().size())));
                return "★" + name + "(" + cardSettle.getCount() + ")";
            case 2:
                name = normalFour().get((new Random().nextInt(normalFour().size())));
                return "★" + name + "(" + cardSettle.getCount() + ")";
            default:
                name = rubbish().get((new Random().nextInt(rubbish().size())));
                return "☆" + name + "(" + cardSettle.getCount() + ")";
        }
    }
}


@Data
@EqualsAndHashCode(callSuper = false)
class NormalPool extends ArkNightsPool {
    String name = "方舟";

    String description() {
        return super.description();
    }

    @Override
    List<String> getUpSix() {
        return Collections.singletonList("");
    }

    @Override
    List<String> upFive() {
        return Collections.singletonList("");
    }

    @Override
    List<String> upFour() {
        return Collections.singletonList("");
    }

    @Override
    List<String> normalSix() {
        return Arrays.asList("能天使", "推进之王", "伊芙利特", "艾雅法拉", "安洁丽娜", "闪灵", "夜莺", "星熊", "赛雷娅", "银灰", "斯卡蒂", "陈", "黑", "赫拉格", "麦哲伦", "莫斯提马", "煌", "阿", "刻俄柏", "风笛", "傀影", "温蒂", "早露", "铃兰", "棘刺", "森蚺", "史尔特尔", "瑕光", "泥岩");
    }

    @Override
    List<String> normalFive() {
        return Arrays.asList("白面鸮", "凛冬", "德克萨斯", "芙兰卡", "拉普兰德", "幽灵鲨", "蓝毒", "白金", "陨星", "天火", "梅尔", "赫默", "华法琳", "临光", "红", "雷蛇", "可颂", "普罗旺斯", "守林人", "崖心", "初雪", "真理", "空", "狮蝎", "食铁兽", "夜魔", "诗怀雅", "格劳克斯", "星极", "送葬人", "槐琥", "苇草", "布洛卡", "灰喉", "哞", "惊蛰", "慑砂", "巫恋", "极境", "石棉", "月禾", "莱恩哈特", "断崖", "蜜蜡", "贾维", "安哲拉", "燧石", "四月", "奥斯塔", "絮雨");
    }

    @Override
    List<String> normalFour() {
        return Arrays.asList("夜烟", "远山", "杰西卡", "流星", "白雪", "清道夫", "红豆", "杜宾", "缠丸", "霜叶", "慕斯", "砾", "暗锁", "末药", "调香师", "角峰", "蛇屠箱", "古米", "深海色", "地灵", "阿消", "猎蜂", "格雷伊", "苏苏洛", "桃金娘", "红云", "梅", "安比尔", "宴", "刻刀", "波登可", "卡达", "孑", "酸糖", "芳汀", "泡泡", "杰克");
    }


}

/**
 * 起止时间：11月12日04:00~11月26日03:59寻访说明：常驻标准寻访更新，该寻访中以下干员获得概率提升；
 * ★★★★★★：早露 / 夜莺（占6★出率的50%）
 * ★★★★★：白面鸮 / 红 / 月禾（占5★出率的50%）
 */
@Data
@EqualsAndHashCode(callSuper = false)
class ArkPool20201112 extends ArkNightsPool {
    String name = "早露";

    String description() {
        return "11月12日04:00~11月26日03:59";
    }

    @Override
    List<String> getUpSix() {
        return Arrays.asList("早露", "夜莺");
    }

    @Override
    List<String> upFive() {
        return Arrays.asList("白面鸮", "红", "月禾");
    }

    @Override
    List<String> upFour() {
        return Collections.singletonList("");
    }

    @Override
    List<String> normalSix() {
        return Arrays.asList("能天使", "推进之王", "伊芙利特", "艾雅法拉", "安洁丽娜", "闪灵", "夜莺", "星熊", "赛雷娅", "银灰", "斯卡蒂", "陈", "黑", "赫拉格", "麦哲伦", "莫斯提马", "煌", "阿", "刻俄柏", "风笛", "傀影", "温蒂", "早露", "铃兰", "棘刺", "森蚺", "史尔特尔", "瑕光", "泥岩");
    }

    @Override
    List<String> normalFive() {
        return Arrays.asList("白面鸮", "凛冬", "德克萨斯", "芙兰卡", "拉普兰德", "幽灵鲨", "蓝毒", "白金", "陨星", "天火", "梅尔", "赫默", "华法琳", "临光", "红", "雷蛇", "可颂", "普罗旺斯", "守林人", "崖心", "初雪", "真理", "空", "狮蝎", "食铁兽", "夜魔", "诗怀雅", "格劳克斯", "星极", "送葬人", "槐琥", "苇草", "布洛卡", "灰喉", "哞", "惊蛰", "慑砂", "巫恋", "极境", "石棉", "月禾", "莱恩哈特", "断崖", "蜜蜡", "贾维", "安哲拉", "燧石", "四月", "奥斯塔", "絮雨");
    }

    @Override
    List<String> normalFour() {
        return Arrays.asList("夜烟", "远山", "杰西卡", "流星", "白雪", "清道夫", "红豆", "杜宾", "缠丸", "霜叶", "慕斯", "砾", "暗锁", "末药", "调香师", "角峰", "蛇屠箱", "古米", "深海色", "地灵", "阿消", "猎蜂", "格雷伊", "苏苏洛", "桃金娘", "红云", "梅", "安比尔", "宴", "刻刀", "波登可", "卡达", "孑", "酸糖", "芳汀", "泡泡", "杰克");
    }


}

/**
 * 起止时间：11月15日16:00~11月29日03:59
 * ★★★★★★：(6★出率: 2%):阿/星熊/刻俄柏/风笛
 * ★★★★★：蓝毒/华法琳/真理/星极/布洛卡/石棉
 */
@Data
@EqualsAndHashCode(callSuper = false)
class JoinAction20201122 extends ArkNightsPool {
    String name = "联合行动风笛";

    String description() {
        return "11月15日16:00~11月29日03:59";
    }

    @Override
    List<String> getUpSix() {
        return Collections.singletonList("");
    }

    @Override
    List<String> upFive() {
        return Collections.singletonList("");
    }

    @Override
    List<String> upFour() {
        return Collections.singletonList("");
    }

    @Override
    List<String> normalSix() {
        return Arrays.asList("阿", "星熊", "刻俄柏", "风笛");
    }

    @Override
    List<String> normalFive() {
        return Arrays.asList("蓝毒", "华法琳", "真理", "星极", "布洛卡", "石棉");
    }

    @Override
    List<String> normalFour() {
        return Arrays.asList("夜烟", "远山", "杰西卡", "流星", "白雪", "清道夫", "红豆", "杜宾", "缠丸", "霜叶", "慕斯", "砾", "暗锁", "末药", "调香师", "角峰", "蛇屠箱", "古米", "深海色", "地灵", "阿消", "猎蜂", "格雷伊", "苏苏洛", "桃金娘", "红云", "梅", "安比尔", "宴", "刻刀", "波登可", "卡达", "孑", "酸糖", "芳汀", "泡泡", "杰克");
    }


}

@Data
@EqualsAndHashCode(callSuper = false)
class MoonIsObscure20210120 extends ArkNightsPool {
    String name = "月隐晦明";

    String description() {
        return "02月05日 16:00 - 02月19日 03:59  ";
    }

    Double getUpSixFloor() {
        return 0.7;
    }

    Double getUpFiveFloor() {
        return 0.5;
    }

    Double getUpFourFloor() {
        return null;
    }

    @Override
    List<String> getUpSix() {
        return Arrays.asList("夕[限定]", "嵯峨");
    }

    @Override
    List<String> upFive() {
        return Collections.singletonList("乌有");
    }

    @Override
    List<String> upFour() {
        return Collections.singletonList("");
    }

    @Override
    List<String> normalSix() {
        return Arrays.asList("年[限定]", "年[限定]", "年[限定]", "年[限定]", "年[限定]");
    }

    @Override
    List<String> normalFive() {
        return Arrays.asList("白面鸮", "凛冬", "德克萨斯", "芙兰卡", "拉普兰德", "幽灵鲨", "蓝毒", "白金", "陨星", "天火", "梅尔", "赫默", "华法琳", "临光", "红", "雷蛇", "可颂", "普罗旺斯", "守林人", "崖心", "初雪", "真理", "空", "狮蝎", "食铁兽", "夜魔", "诗怀雅", "格劳克斯", "星极", "送葬人", "槐琥", "苇草", "布洛卡", "灰喉", "哞", "惊蛰", "慑砂", "巫恋", "极境", "石棉", "月禾", "莱恩哈特", "断崖", "蜜蜡", "贾维", "安哲拉", "燧石", "四月", "奥斯塔", "絮雨", "卡夫卡", "爱丽丝");
    }

    @Override
    List<String> normalFour() {
        return Arrays.asList("夜烟", "远山", "杰西卡", "流星", "白雪", "清道夫", "红豆", "杜宾", "缠丸", "霜叶", "慕斯", "砾", "暗锁", "末药", "调香师", "角峰", "蛇屠箱", "古米", "深海色", "地灵", "阿消", "猎蜂", "格雷伊", "苏苏洛", "桃金娘", "红云", "梅", "安比尔", "宴", "刻刀", "波登可", "卡达", "孑", "酸糖", "芳汀", "泡泡", "杰克", "松果", "豆苗");
    }


}

/**
 * 起止时间：03月18日16:00~04月01日03:59
 * ★★★★★★：(6★出率: 2%):安洁丽娜/刻俄柏
 * ★★★★★：普罗旺斯/絮语/拉普兰德/
 */
@Data
@EqualsAndHashCode(callSuper = false)
class Normal20210317 extends ArkNightsPool {
    String name = "安洁丽娜";

    String description() {
        return "03月18日16:00~04月01日03:59";
    }

    @Override
    List<String> getUpSix() {
        return Arrays.asList("安洁丽娜", "刻俄柏");
    }

    @Override
    List<String> upFive() {
        return Arrays.asList("普罗旺斯", "絮语", "拉普兰德");
    }

    @Override
    List<String> upFour() {
        return Collections.singletonList("");
    }

    @Override
    List<String> normalSix() {
        return Arrays.asList("能天使", "推进之王", "伊芙利特", "艾雅法拉", "安洁丽娜", "闪灵", "夜莺", "星熊", "赛雷娅", "银灰", "斯卡蒂", "陈", "黑", "赫拉格", "麦哲伦", "莫斯提马", "煌", "阿", "刻俄柏", "风笛", "傀影", "温蒂", "早露", "铃兰", "棘刺", "森蚺", "史尔特尔", "瑕光", "泥岩", "山", "空弦", "巍峨");
    }

    @Override
    List<String> normalFive() {
        return Arrays.asList("白面鸮", "凛冬", "德克萨斯", "芙兰卡", "拉普兰德", "幽灵鲨", "蓝毒", "白金", "陨星", "天火", "梅尔", "赫默", "华法琳", "临光", "红", "雷蛇", "可颂", "普罗旺斯", "守林人", "崖心", "初雪", "真理", "空", "狮蝎", "食铁兽", "夜魔", "诗怀雅", "格劳克斯", "星极", "送葬人", "槐琥", "苇草", "布洛卡", "灰喉", "哞", "惊蛰", "慑砂", "巫恋", "极境", "石棉", "月禾", "莱恩哈特", "断崖", "蜜蜡", "贾维", "安哲拉", "燧石", "四月", "奥斯塔", "絮雨", "卡夫卡", "爱丽丝", "乌有");
    }

    @Override
    List<String> normalFour() {
        return Arrays.asList("夜烟", "远山", "杰西卡", "流星", "白雪", "清道夫", "红豆", "杜宾", "缠丸", "霜叶", "慕斯", "砾", "暗锁", "末药", "调香师", "角峰", "蛇屠箱", "古米", "深海色", "地灵", "阿消", "猎蜂", "格雷伊", "苏苏洛", "桃金娘", "红云", "梅", "安比尔", "宴", "刻刀", "波登可", "卡达", "孑", "酸糖", "芳汀", "泡泡", "杰克", "松果", "豆苗");
    }
}
/**
 * 起止时间：03月04日16:00~03月18日03:59
 * ★★★★★★：(6★出率: 2%):史尔特尔/黑
 * ★★★★★：雷蛇/奥斯塔/崖心
 */
@Data
@EqualsAndHashCode(callSuper = false)
class Normal20210304 extends ArkNightsPool {
    String name = "史尔特尔";

    String description() {
        return "03月04日16:00~03月18日03:59";
    }

    @Override
    List<String> getUpSix() {
        return Arrays.asList("史尔特尔", "黑");
    }

    @Override
    List<String> upFive() {
        return Arrays.asList("崖心", "奥斯塔", "雷蛇");
    }

    @Override
    List<String> upFour() {
        return Collections.singletonList("");
    }

    @Override
    List<String> normalSix() {
        return Arrays.asList("能天使", "推进之王", "伊芙利特", "艾雅法拉", "安洁丽娜", "闪灵", "夜莺", "星熊", "赛雷娅", "银灰", "斯卡蒂", "陈", "黑", "赫拉格", "麦哲伦", "莫斯提马", "煌", "阿", "刻俄柏", "风笛", "傀影", "温蒂", "早露", "铃兰", "棘刺", "森蚺", "史尔特尔", "瑕光", "泥岩", "山", "空弦", "巍峨");
    }

    @Override
    List<String> normalFive() {
        return Arrays.asList("白面鸮", "凛冬", "德克萨斯", "芙兰卡", "拉普兰德", "幽灵鲨", "蓝毒", "白金", "陨星", "天火", "梅尔", "赫默", "华法琳", "临光", "红", "雷蛇", "可颂", "普罗旺斯", "守林人", "崖心", "初雪", "真理", "空", "狮蝎", "食铁兽", "夜魔", "诗怀雅", "格劳克斯", "星极", "送葬人", "槐琥", "苇草", "布洛卡", "灰喉", "哞", "惊蛰", "慑砂", "巫恋", "极境", "石棉", "月禾", "莱恩哈特", "断崖", "蜜蜡", "贾维", "安哲拉", "燧石", "四月", "奥斯塔", "絮雨", "卡夫卡", "爱丽丝", "乌有");
    }

    @Override
    List<String> normalFour() {
        return Arrays.asList("夜烟", "远山", "杰西卡", "流星", "白雪", "清道夫", "红豆", "杜宾", "缠丸", "霜叶", "慕斯", "砾", "暗锁", "末药", "调香师", "角峰", "蛇屠箱", "古米", "深海色", "地灵", "阿消", "猎蜂", "格雷伊", "苏苏洛", "桃金娘", "红云", "梅", "安比尔", "宴", "刻刀", "波登可", "卡达", "孑", "酸糖", "芳汀", "泡泡", "杰克", "松果", "豆苗");
    }
}

@Getter
class ArkPools {
    private final Map<String, ArkNightsPool> hashMap = new HashMap<>();

    private static ArkPools instance = null;

    public static ArkPools getInstance() {
        if (instance == null) {
            instance = new ArkPools();
        }
        return instance;
    }

    public ArkPools() {
        register(new NormalPool(), new ArkPool20201112(), new JoinAction20201122(), new MoonIsObscure20210120(), new Normal20210317(),new Normal20210304());
    }

    private void register(ArkNightsPool... arkPools) {
        for (ArkNightsPool arkPool : arkPools) {
            hashMap.put(arkPool.getName(), arkPool);
        }
    }
}

