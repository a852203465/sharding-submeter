package cn.darkjrong.submeter.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时器表达式
 *
 * @author Rong.Jia
 * @date 2021/10/18
 */
@Getter
@AllArgsConstructor
public enum ScheduledCronEnum {

    // 年月
    YYYY_MM("0 21,40,55,58 23 28,29,30,31 * ? "),

    // 年月日
    YYYY_MM_DD("* 10,20,40,50,58,59 23 * * ? "),

    // 年
    YYYY("* 10,20,40,50,58,59 23 31 12 ? *"),










    ;


    private String value;


}
