package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.RankingDTO;
import com.seoultech.codemos.service.LeaderBoardService;
import com.seoultech.codemos.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaderboard")
public class PublicAccessController {
    @Autowired
    private LeaderBoardService leaderBoardService;
    @Autowired
    private RankingService rankingService;
    @GetMapping
    public Page<RankingDTO> getLeaderBoard(@RequestParam(defaultValue = "1") int pageno) {
        return rankingService.getRanking(pageno);
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getCodeByLeaderBoardId(@PathVariable int id) {
        String code = leaderBoardService.getCodeByLeaderBoardId(id);
        if (code != null) {
            return ResponseEntity.ok(code);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
