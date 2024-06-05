package com.example.stylescanner.item.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public enum Category {
    ROOT("전체", null),
        FASHION_WOMEN("여성", ROOT),
            WOMEN_OUTER("아우터", FASHION_WOMEN),
            WOMEN_TOP("상의", FASHION_WOMEN),
            WOMEN_PANTS("팬츠", FASHION_WOMEN),
            WOMEN_SKIRT("스커트", FASHION_WOMEN),
            WOMEN_ONE_PIECE("원피스", FASHION_WOMEN),
            WOMEN_SHOES("신발", FASHION_WOMEN),
            WOMEN_BAG("가방", FASHION_WOMEN),
            WOMEN_ACC("악세사리", FASHION_WOMEN),
            WOMEN_ETC("기타", FASHION_WOMEN),
        FASHION_MEN("남성", ROOT),
            MEN_OUTER("아우터", FASHION_MEN),
            MEN_TOP("상의", FASHION_MEN),
            MEN_PANTS("팬츠", FASHION_MEN),
            MEN_SHOES("신발", FASHION_MEN),
            MEN_BAG("가방", FASHION_MEN),
            MEN_ACC("악세사리", FASHION_MEN),
            MEN_ETC("기타", FASHION_MEN);

    private final String title;

    // 부모 카테고리
    private final Category parentCategory;

    // 자식카테고리
    private final List<Category> childCategories;

    Category(String title, Category parentCategory) {
        this.childCategories = new ArrayList<>();
        this.title = title;
        this.parentCategory = parentCategory;
        if(Objects.nonNull(parentCategory)) {
            parentCategory.childCategories.add(this);
        }
    }
    // 부모카테고리 Getter
    public Optional<Category> getParentCategory() {
        return Optional.ofNullable(parentCategory);
    }

    // 자식카테고리 Getter
    public List<Category> getChildCategories() {
        return Collections.unmodifiableList(childCategories);
    }

    // 마지막 카테고리(상품추가 가능)인지 반환
    public boolean isLeafCategory() {
        return childCategories.isEmpty();
    }

    // 마지막 카테고리(상품추가 가능)들 반환
    public List<Category> getLeafCategories() {
        return Arrays.stream(Category.values())
                .filter(category -> category.isLeafCategoryOf(this))
                .collect(Collectors.toList());
    }

    private boolean isLeafCategoryOf(Category category) {
        return this.isLeafCategory() && category.contains(this);
    }

    private boolean contains(Category category) {
        if(this.equals(category)) return true;

        return Objects.nonNull(category.parentCategory) &&
                this.contains(category.parentCategory);
    }
}
