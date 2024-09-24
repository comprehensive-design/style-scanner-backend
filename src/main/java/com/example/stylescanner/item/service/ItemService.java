package com.example.stylescanner.item.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.stylescanner.item.category.Category;
import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.item.repository.ItemRepository;
import com.example.stylescanner.itemLike.repository.ItemLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    @Value("${cloud.aws.s3.bucket}")
    private String buketName;

    private final AmazonS3 amazonS3;

    private final ItemRepository itemRepository;
    private final ItemLikeRepository itemLikeRepository;


    public List<Item> list(String category){
        return getItemsByCategoryName(category);
    }

    public List<Item> ranking(String category, int timeFilter){
        List<Item> items = getItemsByCategoryName(category);

        if (timeFilter == 0) {
            // 전체 기간
            return items;
        }
        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        if (timeFilter == 1) {
            // 오늘 하루 동안 생성된 itemLike
            startDate = LocalDateTime.of(endDate.toLocalDate(), LocalTime.MIDNIGHT);
        } else if (timeFilter == 2) {
            // 일주일 동안 생성된 itemLike
            startDate = endDate.minusDays(7);
        } else {
            throw new IllegalArgumentException("Invalid time filter");
        }
        List<Object[]> likeCounts = itemLikeRepository.findItemLikeCountsBetweenDates(startDate, endDate);

        System.out.println("asdf");
        System.out.println(likeCounts);
        Map<Long, Long> likeCountMap = likeCounts.stream()
                .collect(Collectors.toMap(
                        entry -> (Long) entry[0],
                        entry -> (Long) entry[1]
                ));

        // 좋아요 수 기준으로 아이템 정렬
        items.sort(Comparator.comparingLong(item -> -likeCountMap.getOrDefault(item.getId(), 0L)));

        return items;
    }

    public Item read(Integer id){
        return itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다. id= "+ id));
    }

    public List<Item> getItemsByCategories(List<Category> categories) {
        return itemRepository.findByCategoryInOrderByLikeCount(categories);
    }
    public List<Item> getItemsByCategoryName(String categoryName) {
        Category category = Category.valueOf(categoryName);
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        categories.addAll(category.getAllSubCategories());

        return getItemsByCategories(categories);
    }

    @Transactional
    public void saveAllItems(List<ItemDto> itemDtoList) {
        List<Item> items = itemDtoList.stream().map(dto -> Item.builder()
                .feedUrl(dto.getFeedUrl())
                .name(dto.getName())
                .price(dto.getPrice())
                .itemUrl(dto.getItemUrl())
                .category(dto.getCategory())
                .itemOption(dto.getItemOption())
                .brand(dto.getBrand())
                .shoppingLink(dto.getShoppingLink())
                .build()).toList();

        itemRepository.saveAll(items);
    }
}
