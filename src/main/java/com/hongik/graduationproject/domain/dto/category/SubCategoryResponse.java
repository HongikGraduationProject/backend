package com.hongik.graduationproject.domain.dto.category;

public record SubCategoryResponse (
        String categoryName,
        Long categoryId,
        Integer summaryCount,
        java.time.LocalDateTime updateAt
){
}