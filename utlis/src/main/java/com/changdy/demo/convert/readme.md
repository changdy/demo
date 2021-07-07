# 缓存转换工具类

## 介绍

一个小工具 , 可以递归进行字典值转换  ,  内部有反射缓存 ,以及循环依赖检测.

## 关键类介绍

* `CacheEntity` `CacheField`

  均为`FIELD`级别注解 , 一个放在内部的实体类上 ,一个放在字典的key上面

* `ConvertedAble`

  接口 ,  实现该接口方可被转换

* `CacheUtil`

  根据属性获取字典值 ,推荐使用 change data capture 进行通知

## demo

```java
@Data
public class OtherEntity implements ConvertedAble {
    @CacheField("other-key")
    private String key;
    private String keyName;
}

@Data
public class BaseEntity implements ConvertedAble {
    @CacheField("secondField-key")
    private String secondField;
    // secondFieldName = secondField + Name
    private String secondFieldName;
    // 被嵌套的实体类 
    @CacheEntity
    private OtherEntity otherEntity;
}

public class TestConvert {
    public static void main(String[] args) throws NoSuchFieldException, JsonProcessingException, IllegalAccessException {
        BaseEntity baseEntity = new BaseEntity();
        OtherEntity otherEntity = new OtherEntity();
        baseEntity.setBaseField("--2");
        baseEntity.setOtherEntity(otherEntity);
        otherEntity.setKey("key");
        System.out.println(ConvertUtil.convert(baseEntity));
    }
}
```



