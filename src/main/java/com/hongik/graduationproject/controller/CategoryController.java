package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.category.SubCategoryListRequest;
import com.hongik.graduationproject.domain.dto.category.SubCategoryListResponse;
import com.hongik.graduationproject.eum.MainCategory;
import com.hongik.graduationproject.service.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/categories")
    public Response<SubCategoryListResponse> getSubCategoryList(@RequestParam MainCategory mainCategory) {
        return Response.createSuccess(categoryService.getSubCategoryList(mainCategory));
    }
}
