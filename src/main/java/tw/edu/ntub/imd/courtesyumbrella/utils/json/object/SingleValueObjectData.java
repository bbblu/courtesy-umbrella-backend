package tw.edu.ntub.imd.courtesyumbrella.utils.json.object;

import com.fasterxml.jackson.databind.JsonNode;
import tw.edu.ntub.imd.courtesyumbrella.utils.json.ResponseData;
import tw.edu.ntub.imd.utils.date.*;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SingleValueObjectData implements ResponseData {
    private ObjectData objectData = new ObjectData();

    private SingleValueObjectData() {

    }

    public static SingleValueObjectData create(String key, short value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, @Nullable Short value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, int value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, @Nullable Integer value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, long value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, @Nullable Long value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, float value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, @Nullable Float value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, double value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, @Nullable Double value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, boolean value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, @Nullable Boolean value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, BigDecimal value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, @Nullable BigInteger value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, String value) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, value);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, byte[] bytes) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, bytes);
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, DateUtils.DateWrapper<?> dateWrapper, DatePatternBuilder builder) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, dateWrapper.format(builder));
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, LocalDate localDate, DatePatternBuilder builder) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, LocalDateUtils.format(localDate, builder));
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, LocalTime localTime, DatePatternBuilder builder) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, LocalTimeUtils.format(localTime, builder));
        return singleValueObjectData;
    }

    public static SingleValueObjectData create(String key, LocalDateTime localDateTime, DatePatternBuilder builder) {
        SingleValueObjectData singleValueObjectData = new SingleValueObjectData();
        singleValueObjectData.objectData.add(key, LocalDateTimeUtils.format(localDateTime, builder));
        return singleValueObjectData;
    }

    @Override
    public JsonNode getData() {
        return objectData.getData();
    }
}
