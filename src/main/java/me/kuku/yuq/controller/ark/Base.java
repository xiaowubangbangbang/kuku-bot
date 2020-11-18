package me.kuku.yuq.controller.ark;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Base {

    private List<String> levelPrizePool = new ArrayList<>();

    public String invoke(List<String> levelPrizePool) {
        return levelPrizePool.get(new Random().nextInt() * levelPrizePool.size());
    }
}

interface CardPool {
    String getName();

    String invoke(CardSettle cardSettle);
}

@Data
@AllArgsConstructor
class CardSettle {
    private int level;
    private int count;
    private CardPool cardPool;
    private Boolean isFloor;
    private Boolean isUp;
}
