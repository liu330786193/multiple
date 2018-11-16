package com.lyl.validate;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 下午4:20
 */
@Validated
@RestController
@RequestMapping("/validate")
public class ValidateController {


    @GetMapping("/test1")
    public String test2(@NotBlank(message = "name 不能为空") @Length(min = 2, max = 10, message = "name 长度必须在 {min} - {max} 之间") String name) {
        return "success";
    }

    @PostMapping("/test2")
    public String test3(@RequestBody @Validated Book book) {
        return "success";
    }

}
