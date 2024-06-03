package com.example.stylescanner.itemLike.controller;

import com.example.stylescanner.error.StateResponse;
import com.example.stylescanner.item.dto.ItemDto;
import com.example.stylescanner.itemLike.api.ItemLikeApi;
import com.example.stylescanner.itemLike.entity.ItemLike;
import com.example.stylescanner.itemLike.service.ItemLikeService;
import com.example.stylescanner.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ItemLikeController implements ItemLikeApi {

    private final ItemLikeService itemLikeService;
    private final JwtProvider jwtProvider;

    @Override
    public Boolean liked(Integer itemId, HttpServletRequest request) {
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return itemLikeService.liked(itemId, currentUserEmail);
    }

    @Override
    public List<ItemDto> me(HttpServletRequest request){
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return itemLikeService.me(currentUserEmail);
    }

    @Override
    public ResponseEntity<StateResponse> create(Integer itemId, HttpServletRequest request){
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return itemLikeService.create(itemId, currentUserEmail);
    }

    @Override
    public ResponseEntity<StateResponse> delete(Integer itemId, HttpServletRequest request){
        String currentUserEmail = jwtProvider.getAccount(jwtProvider.resolveToken(request).substring(7));
        return itemLikeService.delete(itemId, currentUserEmail);
    };

}