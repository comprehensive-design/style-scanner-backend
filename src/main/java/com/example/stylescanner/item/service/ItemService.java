package com.example.stylescanner.item.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.item.category.Category;
import com.example.stylescanner.item.dto.ItemCreateDto;
import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.item.repository.ItemRepository;
import com.example.stylescanner.itemLike.repository.ItemLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
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
    private String bucketName;

    private final AmazonS3 s3Client;

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
    public ResponseEntity<StateResponse> create(ItemCreateDto itemCreateDto) throws Exception {
        Item item = Item.builder()
                .feedUrl("test")
                .name(itemCreateDto.getTitle())
                .price(itemCreateDto.getCost())
                .itemUrl(itemCreateDto.getImageUrl())
                .shoppingLink(itemCreateDto.getShoppingLink())
                .platform(itemCreateDto.getSellerName())
                .category(itemCreateDto.getCategory())
                .build();

        String s3Path;

        try {
            item = itemRepository.save(item);
            s3Path = uploadImageToS3(itemCreateDto.getImageUrl(), item.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StateResponse.builder()
                            .code("FAILURE")
                            .message("이미지 업로드 실패: " + e.getMessage())
                            .build());
        }

        item.setItemUrl("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + s3Path);

        itemRepository.save(item);

        return ResponseEntity.ok(
                StateResponse.builder()
                        .code("SUCCESS")
                        .message("아이템을 성공적으로 추가했습니다. ID: " + item.getId() + " 이미지 경로: " + s3Path)
                        .build()
        );
    }

    public String uploadImageToS3(String imageUrl, Long id) throws Exception {
        URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {
            byte[] imageBytes = inputStream.readAllBytes();

            // S3에 이미지 업로드
            String fileName = id + ".jpg";
            s3Client.putObject(bucketName, "items/" + fileName, new ByteArrayInputStream(imageBytes), null);

            return "items/" + fileName; // S3에서의 경로
        }
    }
}
