package com.hongik.graduationproject.domain.dto.video;

import com.hongik.graduationproject.enums.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoSummaryInitiateMessage {
    String url;
    String videoCode;
    Platform platform;
}
