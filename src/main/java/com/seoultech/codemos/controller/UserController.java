package com.seoultech.codemos.controller;


import com.seoultech.codemos.dto.MypageResponseDTO;
import com.seoultech.codemos.dto.UserRequestDTO;
import com.seoultech.codemos.dto.UserResponseDTO;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.model.LeaderBoardEntity;
import com.seoultech.codemos.model.RankingEntity;
import com.seoultech.codemos.repository.LeaderBoardRepository;
import com.seoultech.codemos.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final LeaderBoardRepository leaderBoardRepository;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyMemberInfo() {
        UserResponseDTO myInfoBySecurity = userService.getMyInfoBySecurity();
        String nickname = myInfoBySecurity.getNickname();
        Map<String, Object> profileData = userService.getUserProfileByNickname(nickname);
        return ResponseEntity.ok(profileData);
        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String nickname) {
        Map<String, Object> profileData = userService.getUserProfileByNickname(nickname);
        return ResponseEntity.ok(profileData);
    }

    @PutMapping("/nickname")
    public ResponseEntity<UserResponseDTO> changeMemberNickname(@RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.changeMemberNickname(request.getEmail(), request.getNickname());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<UserResponseDTO> changeProfilePicture(@RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.changeProfilePicture(request.getEmail(), request.getProfilePicURL());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDTO> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername(); //사용자 ID를 UserDetails에서 추출
        System.out.println("Uid: " + userId);
        MypageResponseDTO mypageResponseDTO = userService.getUserMypage(userId);
        return ResponseEntity.ok(mypageResponseDTO);
    }

    @PostMapping("/updateRanking/{id}")
    public ResponseEntity<RankingEntity> copyLeaderBoardToRanking(@PathVariable int id, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String tokenLoginId = tokenProvider.getUsernameFromToken(token); // 토큰에서 loginId 추출
        Long tokenId = Long.parseLong(tokenLoginId);
        System.out.println("TOKENNICKNAME: "+ tokenLoginId);
        Optional<LeaderBoardEntity> leaderBoard = leaderBoardRepository.findById(id);
        if (leaderBoard.isPresent()) {
            Long leaderBoardLoginId = leaderBoard.get().getUser().getId(); // LeaderBoardEntity에서 닉네임추출
            System.out.println("LeaderBoardLoginId: "+ leaderBoardLoginId);
            if (!tokenId.equals(leaderBoardLoginId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // loginId가 일치하지 않으면 접근 거부
            }
        }
        RankingEntity rankingEntity = userService.copyLeaderBoardToRanking(id);
        if (rankingEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(rankingEntity);
    }
}