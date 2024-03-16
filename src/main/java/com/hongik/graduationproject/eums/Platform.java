package com.hongik.graduationproject.eums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    YOUTUBE("YOUTUBE"),
    INSTAGRAM("INSTAGRAM"),
    TIKTOK("TIKTOK");
    private final String platform;

}
