package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.LeaderBoardRequest;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.model.LeaderBoardEntity;
import com.seoultech.codemos.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/leaderBoard")
public class LeaderBoardController{
    @Autowired
    private LeaderBoardService service;
    @Autowired
    private TokenProvider tokenProvider;
    @PostMapping("/create")
    public LeaderBoardEntity create(@RequestBody LeaderBoardRequest dto,
                                    @RequestHeader(value = "Authorization") String token) {
        token = token.substring(7); // "Bearer " 부분 제거
        String username = tokenProvider.getUsernameFromToken(token);
        System.out.println("UNAME: "+username);
        return service.createLeaderBoard(dto, username);
    }
}
