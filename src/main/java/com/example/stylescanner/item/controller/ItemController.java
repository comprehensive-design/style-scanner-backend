package com.example.stylescanner.item.controller;

import com.example.stylescanner.item.api.ItemApi;
import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController implements ItemApi {

    private final ItemService itemService;

    @Override
    public List<Item> list() {
        return itemService.list();
    }

    @Override
    public Item read(Integer id) {
        return itemService.read(id);
    }
}
