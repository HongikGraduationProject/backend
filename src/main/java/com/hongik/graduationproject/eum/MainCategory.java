package com.hongik.graduationproject.eum;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum MainCategory {
    TECHNOLOGY("기술", 0),
    BEAUTY("뷰티", 1),
    COOK("요리", 2),
    LIVING("리빙", 3),
    HEALTH("건강", 4),
    TRAVEL("여행", 5),
    ART("예술", 6),
    NEWS("뉴스", 7),
    ENTERTAINMENT("엔터테인먼트", 8),
    OTHER("기타", 9);

    private final String name;
    private final int index;

    public static MainCategory find(String name) {
        for (MainCategory categoryName : MainCategory.values()) {
            if(categoryName.name.equals(name)){
                return categoryName;
            }
        }
        return OTHER;
//        if (name == null) {
//            return MainCategory.OTHER;
//        }
//        return Arrays.stream(values())
//                .filter(accountStatus -> accountStatus.name.equals(name))
//                .findAny()
//                .orElseThrow(RuntimeException::new);
    }
}
