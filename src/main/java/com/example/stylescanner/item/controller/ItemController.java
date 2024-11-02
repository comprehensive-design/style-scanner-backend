package com.example.stylescanner.item.controller;

import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.item.api.ItemApi;
import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.dto.ItemCreateDto;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.item.service.ItemService;
import com.example.stylescanner.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemController implements ItemApi {

    private final ItemService itemService;
    private final JwtProvider jwtProvider;

    @Override
    public List<ItemDto> list(String category) {
        List<Item> items = itemService.list(category);
        return items.stream()
                .map(ItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> ranking(String category, int timeFilter) {
        List<Item> items = itemService.ranking(category, timeFilter);
        List<ItemDto> itemDtos = items.stream()
                .map(ItemDto::fromEntity)
                .toList();
        return itemDtos.size() > 100 ? itemDtos.subList(0, 100) : itemDtos;
    }

    @Override
    public ItemDto read(Integer id) {
        Item item = itemService.read(id);
        return ItemDto.fromEntity(item);
    }

    @Override
    public ResponseEntity<StateResponse> create(ItemCreateDto itemCreateDto) throws Exception {
        return itemService.create(itemCreateDto);
    }

}
