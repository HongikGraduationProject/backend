package com.hongik.graduationproject.eum;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum MainCategory {
    SCIENCE_TECHNOLOGY("과학/기술"),
    FASHION_BEAUTY("패션/뷰티"),
    COOK("요리"),
    LIVING("리빙"),
    HEALTH("건강"),
    TRAVEL("여행"),
    ART("예술"),
    NEWS("뉴스"),
    ENTERTAINMENT("엔터테인먼트"),
    OTHER("기타");

    private final String name;

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
