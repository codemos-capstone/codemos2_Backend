package com.seoultech.codemos.service;

import com.seoultech.codemos.config.SecurityUtil;
import com.seoultech.codemos.dto.MypageResponseDTO;
import com.seoultech.codemos.dto.UserResponseDTO;
import com.seoultech.codemos.model.LeaderBoardEntity;
import com.seoultech.codemos.model.RankingEntity;
import com.seoultech.codemos.model.UserEntity;
import com.seoultech.codemos.repository.LeaderBoardRepository;
import com.seoultech.codemos.repository.RankingRepository;
import com.seoultech.codemos.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private LeaderBoardRepository leaderBoardRepository;

    @Autowired
    private RankingRepository rankingRepository;

    public UserResponseDTO getMyInfoBySecurity() {
        return userRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(UserResponseDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
    @Transactional
    public UserResponseDTO changeMemberNickname(String email, String nickname) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        user.setNickname(nickname);
        return UserResponseDTO.of(userRepository.save(user));
    }
    public UserEntity getLoginUserByLoginId(String userIndex) {
        Long id = Long.parseLong(userIndex); // String을 Long으로 변환
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 사용자가 없습니다: " + userIndex));
    }
    public MypageResponseDTO getUserMypage(String userId) {
        Long id = Long.valueOf(userId);

        Optional<UserEntity> userOptional = userRepository.findById(id); // 유저 정보 조회
        if (userOptional.isEmpty()) {//optional 처리
            throw new EntityNotFoundException("해당 id값에 맞는 유저가 없습니다." + id);
        }

        UserEntity user = userOptional.get();
        List<MypageResponseDTO.LeaderBoardInfo> leaderBoardInfos = user.getLeaderBoardEntries()
                .stream()
                .map(leaderBoardEntity -> new MypageResponseDTO.LeaderBoardInfo(
                        leaderBoardEntity.getId(),
                        leaderBoardEntity.getCodeEntity() != null ? leaderBoardEntity.getCodeEntity().getCode() : null))
                .collect(Collectors.toList());
        return new MypageResponseDTO(
                user.getEmail(),
                user.getNickname(),
                user.getAuthority().toString(),
                leaderBoardInfos
        );
    }



    @Transactional(readOnly = false)
    public RankingEntity copyLeaderBoardToRanking(int id) {
        Optional<LeaderBoardEntity> leaderBoard = leaderBoardRepository.findById(id);
        if (leaderBoard.isEmpty()) {
            return null;
        }

        LeaderBoardEntity leaderBoardEntity = leaderBoard.get();
        Optional<RankingEntity> existingRanking = rankingRepository.findByEmail(leaderBoardEntity.getEmail());

        // 기존 랭킹 데이터가 있는 경우 삭제
        existingRanking.ifPresent(ranking -> rankingRepository.deleteByEmail(ranking.getEmail()));

        RankingEntity rankingEntity = new RankingEntity();
        rankingEntity.setUser(leaderBoardEntity.getUser());
        rankingEntity.setCode(leaderBoardEntity.getCodeEntity());
        rankingEntity.setScore(leaderBoardEntity.getScore());
        rankingEntity.setTime(leaderBoardEntity.getTime());
        rankingEntity.setLeaderBoardId(leaderBoardEntity.getId());
        rankingEntity.setNickname(leaderBoardEntity.getNickname());
        rankingEntity.setEmail(leaderBoardEntity.getEmail());

        rankingRepository.save(rankingEntity);
        return rankingEntity;
    }

}