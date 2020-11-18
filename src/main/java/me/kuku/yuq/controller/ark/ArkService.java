package me.kuku.yuq.controller.ark;

public class ArkService extends BaseCardService {

    @Override
    protected ArkNightsPool getPool(String pool) {
        return new ArkPools().getHashMap().get(pool);
    }

    @Override
    protected CardSettle invoke(UserRecord userRecord) {
        ArkNightsPool pool = getPool(userRecord.getPool());
        userRecord.setSixFloor(userRecord.getSixFloor() + 1);
        userRecord.setFiveFloor(userRecord.getFiveFloor() + 1);
        userRecord.setFourFloor(userRecord.getFourFloor() + 1);
      /*  //前十次抽卡
        if (userRecord.getBeforeTen() && userRecord.getBeforeTenCount() <= 10) {
            if (userRecord.getBeforeTenCount() == 10) {
                if (new Random().nextBoolean()) {
                    return new CardSettle(new Random().nextBoolean() ? 7 : 6, userRecord.getBeforeTenCount(), pool, false, false);
                }
            }
        }*/
        double random = Math.random();
        //这个是在六星池子概率
        if (random <= (pool.sixProbability + userRecord.getSixFloor() > 50 ? (userRecord.getSixFloor() - 50) * 0.02 : 0.0)) {
            int count = userRecord.getSixFloor();
            userRecord.setSixFloor(0);
            if (pool.getUpSixFloor() == null) {
                return new CardSettle(6, count, pool, false, userRecord.getSixFloor() > 50);
            }
            if (Math.random() > pool.getUpSixFloor()) {
                return new CardSettle(6, count, pool, false, userRecord.getSixFloor() > 50);
            } else {
                return new CardSettle(7, count, pool, false, userRecord.getSixFloor() > 50);

            }
        }
        //这是五星池子的概率
        else if (random < pool.fiveProbability) {
            int count = userRecord.getFiveFloor();
            userRecord.setFiveFloor(0);
            if (pool.getUpFiveFloor() == null) {
                return new CardSettle(4, count, pool, false, false);
            }
            if (Math.random() > pool.getUpFiveFloor()) {
                return new CardSettle(4, count, pool, false, false);
            } else {
                return new CardSettle(5, count, pool, false, false);
            }
        } else if (random < pool.fourProbability) {
            int count = userRecord.getFourFloor();
            userRecord.setFourFloor(0);
            if (pool.getUpFourFloor() == null) {
                return new CardSettle(2, count, pool, false, false);
            }
            if (Math.random() > pool.getUpFourFloor()) {
                return new CardSettle(2, count, pool, false, false);
            } else {
                return new CardSettle(3, count, pool, false, false);
            }
        } else {
            return new CardSettle(1, 0, pool, false, false);
        }
    }

}
