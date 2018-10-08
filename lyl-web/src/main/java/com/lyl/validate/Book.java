package com.lyl.validate;//package com.lyl.demo.aliyun.validate;
//
//import lombok.Data;
//import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.NotBlank;
//import org.springframework.validation.annotation.Validated;
//
//import javax.validation.constraints.DecimalMin;
//import javax.validation.constraints.NotNull;
//import java.math.BigDecimal;
//
///**
// * @Author lyl
// * @Description
// * @Date 2018/9/30 下午4:22
// */
//@Validated
//@Data
//public class Book {
//
//    private Integer id;
//    @NotBlank(message = "name 不允许为空")
//    @Length(min = 2, max = 10, message = "name 长度必须在 {min} - {max} 之间")
//    private String name;
//    @NotNull(message = "price 不允许为空")
//    @DecimalMin(value = "0.1", message = "价格不能低于 {value}")
//    private BigDecimal price;
//
//}
