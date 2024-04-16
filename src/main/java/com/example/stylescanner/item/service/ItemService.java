package com.example.stylescanner.item.service;

import com.example.stylescanner.item.entity.Item;
import com.example.stylescanner.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;


    public List<Item> list(){
        return itemRepository.findAll();
    }

    public Item read(Integer id){
        return itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다. id= "+ id));
    }
}
