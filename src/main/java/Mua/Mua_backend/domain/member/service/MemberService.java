package Mua.Mua_backend.domain.member.service;

import Mua.Mua_backend.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    public void updateFcmToken(Member member, String fcmToken) {
        member.updateFcmToken(fcmToken);
    }

    public void removeFcmToken(Member member) {
        member.updateFcmToken(null);
    }
}
