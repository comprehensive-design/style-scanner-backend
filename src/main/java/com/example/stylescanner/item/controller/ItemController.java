package com.example.stylescanner.item.controller;

import com.example.stylescanner.item.api.ItemApi;
import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.item.service.ItemService;
import com.example.stylescanner.post.dto.PostDto;
import com.example.stylescanner.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemController implements ItemApi {

    private final ItemService itemService;

    @Override
    public List<ItemDto> list() {
        List<Item> items = itemService.list();
        return items.stream()
                .map(ItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> ranking() {
        List<Item> items = itemService.list();
        return items.stream()
                .map(ItemDto::fromEntity)
                .sorted((dto1, dto2) -> Integer.compare(dto2.getLikeCount(), dto1.getLikeCount()))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto read(Integer id) {
        Item item = itemService.read(id);
        return ItemDto.fromEntity(item);
    }
}
