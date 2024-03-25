package com.hongik.graduationproject.domain.dto.video;

import com.fasterxml.jackson.annotation.JsonValue;
import com.hongik.graduationproject.eum.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "영상 요약 요청을 위한 Body")
public class VideoSummaryInitiateRequest {
    @Schema(description = "영상의 url", example = "https://www.instagram.com/reel/C4kWXhEuQpD/?utm_source=ig_web_copy_link")
    String url;
    @Schema(hidden = true)
    String videoCode;
    @Schema(hidden = true)
    Platform platform;
}
