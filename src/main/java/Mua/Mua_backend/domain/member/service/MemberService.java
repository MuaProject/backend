package Mua.Mua_backend.domain.member.service;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.member.repository.MemberRepository;
import Mua.Mua_backend.global.exception.member.AlreadyNicknameException;
import Mua.Mua_backend.global.exception.member.MemberNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void updateFcmToken(Member member, String fcmToken) {
        member.updateFcmToken(fcmToken);
    }

    public void removeFcmToken(Member member) {
        member.updateFcmToken(null);
    }

    public void updateNickname(Long memberId, String nickname) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());

        // 중복 체크
        if (memberRepository.existsByNickname(nickname)) {
            throw new AlreadyNicknameException();
        }

        member.updateNickname(nickname);
    }
}
