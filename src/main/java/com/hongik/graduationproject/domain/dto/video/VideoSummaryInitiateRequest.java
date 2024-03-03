package com.hongik.graduationproject.domain.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoSummaryInitiateRequest {
    String uuid;
    String url;
}
