package com.hongik.graduationproject.domain.dto.video;

import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "영상 요약 상태에 대한 응답")
public class VideoSummaryStatusResponse {
    @Schema(description = "현재 상태를 의미함", example = "PROCESSING", allowableValues = {"PROCESSING", "COMPLETE"})
    String status;

    @Schema(description = "영상 요약이 DB에서 가지는 PK, 요약이 완료되기 전까진 -1", example = "3")
    Long videoSummaryId;

    public static VideoSummaryStatusResponse from(VideoSummaryStatusCache videoSummaryStatusCache) {
        return new VideoSummaryStatusResponse(videoSummaryStatusCache.getStatus(), videoSummaryStatusCache.getVideoSummaryId());
    }
}
