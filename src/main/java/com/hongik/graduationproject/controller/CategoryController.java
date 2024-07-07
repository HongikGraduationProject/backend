package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.category.SubCategoryListResponse;
import com.hongik.graduationproject.enums.MainCategory;
import com.hongik.graduationproject.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "서브 카테고리 목록 조회", description = "사용자의 특정 메인 카테고리에 해당하는 서브 카테고리 목록 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = SubCategoryListResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/categories")
    public Response<SubCategoryListResponse> getSubCategoryList(@RequestParam String mainCategory,
                                                                @AuthenticationPrincipal Long userId) {
        if ("ALL".equalsIgnoreCase(mainCategory)) {
            return Response.createSuccess(categoryService.getAllSubCategoryList(userId));
        } else {
            return Response.createSuccess(categoryService.getSubCategoryList(MainCategory.find(mainCategory), userId));
        }
    }
}