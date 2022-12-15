package com.heypixel.germfashionaddon.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {

    /**
     *
     * @param day 传入的天数
     * @return 计算后的timestamp
     */
    public static Timestamp getExpire_time(int day) {
        LocalDateTime now = LocalDateTime.now();
//        System.out.println("当前时间："+now);
        LocalDateTime plusTime = now.plusDays(day);
        long instant = plusTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return new Timestamp(instant);

    }
    /**
     * @param timestamp 传入的时间
     * @param day 传入的天数
     * @return 计算后的timestamp
     */
    public static Timestamp getAdd_Expire_time(Timestamp timestamp ,int day) {
        LocalDateTime now = timestamp.toLocalDateTime();
//        System.out.println("当前时间："+now);
        LocalDateTime plusTime = now.plusDays(day);
        long instant = plusTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return new Timestamp(instant);

    }


}
