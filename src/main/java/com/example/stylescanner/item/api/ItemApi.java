package com.example.stylescanner.item.api;

import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.entity.Item;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/item")
@Tag(name = "Item", description = "아이템 API")
public interface ItemApi {
    @GetMapping("")
    @Operation(summary = "아이템 목록 조회 메서드", description = "카테고리별 아이템 목록을 조회하기 위한 메서드입니다.")
    List<ItemDto> list(@RequestParam String category);

    @GetMapping("/ranking")
    @Operation(summary = "아이템 랭킹 조회 메서드", description = "아이템의 랭킹을 조회하기 위한 메서드입니다.")
    List<ItemDto> ranking(@RequestParam String category, @RequestParam int timeFilter);

    @GetMapping("/{id}")
    @Operation(summary = "아이템 상세 조회 메서드", description = "아이템의 상세 내용을 조회하기 위한 메서드입니다.")
    ItemDto read(@PathVariable Integer id);
}
