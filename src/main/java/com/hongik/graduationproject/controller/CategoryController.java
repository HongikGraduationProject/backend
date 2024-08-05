package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.category.MainCategoryRankingListResponse;
import com.hongik.graduationproject.domain.dto.category.SubCategoryCreateRequest;
import com.hongik.graduationproject.domain.dto.category.SubCategoryListResponse;
import com.hongik.graduationproject.enums.MainCategory;
import com.hongik.graduationproject.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
	private final CategoryService categoryService;

	@Operation(summary = "서브 카테고리 목록 조회", description = "사용자의 특정 메인 카테고리에 해당하는 서브 카테고리 목록 조회를 위한 메소드")
	@ApiResponse(content = @Content(schema = @Schema(implementation = SubCategoryListResponse.class)))
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/categories")
	public Response<SubCategoryListResponse> getSubCategoryList(@RequestParam MainCategory mainCategory,
		@AuthenticationPrincipal Long userId) {
		return Response.createSuccess(categoryService.getSubCategoryList(mainCategory, userId));
	}

	@Operation(summary = "새로운 서브 카테고리 생성", description = "사용자가 원하는 서브 카테고리를 생성하는 메소드")
	@ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/categories")
	public Response<?> createSubCategory(@RequestParam MainCategory mainCategory,
		@RequestBody SubCategoryCreateRequest request,
		@AuthenticationPrincipal Long userId) {
		categoryService.createSubCategory(mainCategory, request.subCategoryName(), userId);
		log.info("새롭게 생성된 서브 카테고리 = {}", request.subCategoryName());
		return Response.createSuccess("서브 카테고리 생성 완료");
	}

	@Operation(summary = "메인 카테고리 랭킹 가져오기", description = "숏폼이 많이 분류된 메인 카테고리 1,2위를 가져오는 메소드")
	@ApiResponse(content = @Content(schema = @Schema(implementation = MainCategoryRankingListResponse.class)))
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/categories/rankings")
	public Response<MainCategoryRankingListResponse> getMainCategoryRanking(@AuthenticationPrincipal Long userId) {
		MainCategoryRankingListResponse rankingList = categoryService.getMainCategoryRanking(userId);
		return Response.createSuccess(rankingList);
	}
}