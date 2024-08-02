package com.hongik.graduationproject.domain.dto.category;

import com.hongik.graduationproject.enums.MainCategory;

public record MainCategoryRankingResponse(

        MainCategory mainCategory,
        Double percentOfTotal,
        Integer summaryCount) {
}