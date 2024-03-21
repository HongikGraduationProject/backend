package com.hongik.graduationproject.domain.dto.video;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "영상 요약 요청에 대한 응답")
public class VideoSummaryInitiateResponse {
    @Schema(description = "비디오 코드, {플랫폼_고유번호} 같은 형태로 만들어짐", example = "INSTAGRAM_C4kWXhEuQpD")
    String videoCode;
}
