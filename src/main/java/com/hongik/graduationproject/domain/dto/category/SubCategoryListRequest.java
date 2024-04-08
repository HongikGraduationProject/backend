package com.hongik.graduationproject.domain.dto.category;

import com.hongik.graduationproject.eum.MainCategory;
import lombok.Data;

public record SubCategoryListRequest(
        MainCategory mainCategory
) {
}
