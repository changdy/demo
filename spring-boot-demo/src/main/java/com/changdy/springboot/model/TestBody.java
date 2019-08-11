package com.changdy.springboot.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Created by Changdy on 2019/8/11.
 */
@Data
public class TestBody {
    /**
     * 关于验证的注解有:
     *
     * @AssertFalse,@AssertTrue,@DecimalMax,@DecimalMin,@Digits,@Email,@Future,@FutureOrPresent,@Max,@Min,@Negative,@NegativeOrZero,@NotBlank,@NotEmpty,@NotNull,@Null,@Past,@PastOrPresent,@Pattern,@Positive,@PositiveOrZero,@Size ;
     * 常用的有以下:
     * @DecimalMax,@DecimalMin,@Max,@Min,@Digits, 数值相关  另外 @DecimalMax,@DecimalMin 规范本身不支持 Double与Float 但是 spring 支持了
     * @NotBlank,@NotEmpty 字符串处理
     * @Size 字符串, 集合, map, 数组
     * @NotNull 是否可以为null, 以上列出的碰到null跳过校验
     * @Valid 嵌套校验 需要用 @Valid 而不能用@Validated
     */
    @NotEmpty
    private String id;
    @NotEmpty
    // @Size 并不会针对 null生效
    @Size(min = 3, max = 8)
    private String name;
    @Valid
    private TestBody children;
}
