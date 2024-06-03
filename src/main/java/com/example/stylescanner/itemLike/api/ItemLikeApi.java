package com.example.stylescanner.itemLike.api;

import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.item.dto.ItemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/itemLike")
@Tag(name = "ItemLike", description = "아이템 좋아요 API")
public interface ItemLikeApi {
    @GetMapping("/{itemId}")
    @Operation(summary = "아이템 좋아요 여부 확인 메서드", description = "사용자가 아이템에 좋아요를 했는지 확인하기 위한 메서드입니다.")
    Boolean liked(@PathVariable Integer itemId, HttpServletRequest request);

    @GetMapping("/me")
    @Operation(summary = "사용자가 좋아요한 아이템 목록 조회 메서드", description = "사용자가 좋아요한 아이템 목록을 조회하기 위한 메서드입니다.")
    List<ItemDto> me(HttpServletRequest request);

    @PostMapping("/{itemId}")
    @Operation(summary = "아이템 좋아요 기능 구현 메서드", description = "사용자가 아이템에 좋아요를 하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> create(@PathVariable Integer itemId, HttpServletRequest request);


    @DeleteMapping("/{itemId}")
    @Operation(summary = "아이템 좋아요 취소 기능 구현 메서드", description = "사용자가 아이템에 단 좋아요를 취소하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> delete(@PathVariable Integer itemId, HttpServletRequest request);
}
