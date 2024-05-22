package com.hongik.graduationproject.domain.dto.category;

import java.time.LocalDateTime;

public record SubCategoryResponse (
        String categoryName,
        Long categoryId,
        Integer summaryCount,
        LocalDateTime updateAt
){
}