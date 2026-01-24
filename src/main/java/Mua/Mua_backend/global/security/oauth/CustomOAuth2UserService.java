package Mua.Mua_backend.global.security.oauth;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		// 여기서 사용자 정보 가공 또는 DB 저장 가능
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String providerId = oAuth2User.getName();

        Member member = memberRepository
                .findByProviderAndProviderId(AuthProvider.KAKAO, providerId)
                .orElseGet(() -> memberRepository.save(
                        Member.createOAuthUser(email, AuthProvider.KAKAO, providerId)
                ));

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
	}
}
