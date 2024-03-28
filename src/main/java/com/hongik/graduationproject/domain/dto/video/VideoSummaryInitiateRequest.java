package com.hongik.graduationproject.domain.dto.video;

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
    @Schema(description = "(임시) 10가지 상위 카테고리 중 하나를 문자열로 첨부", nullable = true, example = "여행", allowableValues = {"과학/기술", "패션/뷰티", "리빙"})
    String mainCategory;
    @Schema(description = "(임시) 사용자가 카테고리를 지정했는지 여부", example = "true")
    boolean isCategoryIncluded;
}
