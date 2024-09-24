package com.example.stylescanner.item.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public enum Category {
    ALL_ALL("전체", null),
        WOMEN_ALL("여성", ALL_ALL),
            WOMEN_OUTER("아우터", WOMEN_ALL),
            WOMEN_TOP("상의", WOMEN_ALL),
            WOMEN_PANTS("팬츠", WOMEN_ALL),
            WOMEN_SKIRT("스커트", WOMEN_ALL),
            WOMEN_ONE_PIECE("원피스", WOMEN_ALL),
            WOMEN_SHOES("신발", WOMEN_ALL),
            WOMEN_BAG("가방", WOMEN_ALL),
            WOMEN_ACC("악세사리", WOMEN_ALL),
            WOMEN_ETC("기타", WOMEN_ALL),
        MEN_ALL("남성", ALL_ALL),
            MEN_OUTER("아우터", MEN_ALL),
            MEN_TOP("상의", MEN_ALL),
            MEN_PANTS("팬츠", MEN_ALL),
            MEN_SHOES("신발", MEN_ALL),
            MEN_BAG("가방", MEN_ALL),
            MEN_ACC("악세사리", MEN_ALL),
            MEN_ETC("기타", MEN_ALL);

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

    // 자식카테고리 재귀적으로 반환
    public List<Category> getAllSubCategories() {
        List<Category> subCategories = new ArrayList<>(childCategories);
        for (Category child : childCategories) {
            subCategories.addAll(child.getAllSubCategories());
        }
        return subCategories;
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
