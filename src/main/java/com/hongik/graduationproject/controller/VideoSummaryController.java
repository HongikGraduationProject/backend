package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.video.*;
import com.hongik.graduationproject.service.VideoSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "영상", description = "영상 또는 요약과 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class VideoSummaryController {
    private final VideoSummaryService videoSummaryService;

    @Operation(summary = "영상 요약 요청", description = "영상 요약 요청을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryInitiateResponse.class)))
    @PostMapping("/summaries/initiate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response<VideoSummaryInitiateResponse> initiateSummarizing(@RequestBody VideoSummaryInitiateRequest videoSummaryInitiateRequest,
                                                                      @AuthenticationPrincipal Long userId) {
        log.info("summarize initiate video url={}", videoSummaryInitiateRequest.getUrl());
        return Response.createSuccess(videoSummaryService.initiateSummarizing(videoSummaryInitiateRequest, userId));
    }

    @Operation(summary = "영상 요약 상태", description = "영상 요약 상태 확인을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryStatusResponse.class)))
    @GetMapping("/summaries/status/{videoCode}")
    @ResponseStatus(HttpStatus.OK)
    public Response<VideoSummaryStatusResponse> getSummarizeStatus(@PathVariable(name = "videoCode")
                                                                   @Parameter(name = "videoCode", description = "영상 요약 요청에서 응답받은 비디오 코드", example = "INSTAGRAM_C4kWXhEuQpD")
                                                                   String videoCode,
                                                                   @AuthenticationPrincipal Long userId) {
        log.info("summarize status request videoCode = {}", videoCode);
        return Response.createSuccess(videoSummaryService.getStatus(videoCode, userId));
    }

    @Operation(summary = "영상 요약 조회", description = "영상 요약 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryDto.class)))
    @GetMapping("/summaries/{videoSummaryId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<VideoSummaryDto> getSummaryByVideoSummaryId(@PathVariable(name = "videoSummaryId")
                                                                @Parameter(name = "videoSummaryId", description = "영상 요약 상태에서 응답받은 videoSummaryId", example = "3")
                                                                Long videoSummaryId,
                                                                @AuthenticationPrincipal Long userId
                                                                ) {
        log.info("summary requested videoSummaryId = {}", videoSummaryId);
        return Response.createSuccess(videoSummaryService.getVideoSummaryById(videoSummaryId, userId));
    }

    @Operation(summary = "영상 요약 목록 조회", description = "categoryId로 영상 요약 목록 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryListResponse.class)))
    @GetMapping("/summaries")
    @ResponseStatus(HttpStatus.OK)
    public Response<VideoSummaryListResponse> getAllSummariesByCategoryId(@Parameter(required = true) @RequestParam Long categoryId) {
        log.info("get all summaries for categoryId = {}", categoryId);
        return Response.createSuccess(videoSummaryService.getAllSummariesByCategoryId(categoryId));
    }

    @Operation(summary = "검색 결과 조회", description = "제목 또는 내용에서 검색어를 포함하는 숏폼 조회를 위한 API")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("/summaries/search")
    public List<Long> getAllVideoIdsBySearchWord(@Parameter(name = "searchWord", description = "검색어" ) String searchWord){
        List<Long> videoIds = videoSummaryService.getAllVideoIdsBySearchWord(searchWord);
        log.info("검색어 '{}' 에 대한 모든 videoIds 조회 결과: {}", searchWord, videoIds);
        return Response.createSuccess(videoIds).getData();
    }

    @Operation(summary = "숏폼 삭제", description = "사용자가 원하는 숏폼 삭제하는 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/summaries/{videoSummaryId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> deleteVideoSummary(@PathVariable Long videoSummaryId) {
        log.info("Deleting video summary with ID = {}", videoSummaryId);
        videoSummaryService.deleteVideoSummary(videoSummaryId);
        return Response.createSuccess("숏폼 삭제 완료");
    }
}