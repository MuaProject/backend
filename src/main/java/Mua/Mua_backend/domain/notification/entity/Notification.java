package Mua.Mua_backend.domain.notification.entity;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String message;

    @Column(name = "is_read")
    private boolean read;

    @Enumerated(EnumType.STRING)
    private NotificationType targetType;

    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Notification(Member member, String message,
                        NotificationType targetType, Long targetId) {
        this.member = member;
        this.message = message;
        this.targetType = targetType;
        this.targetId = targetId;
        this.read = false;
    }
}
