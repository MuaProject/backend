package Mua.Mua_backend.domain.notification.repository;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberOrderByCreatedAtDesc(Member member);

    void deleteByMember(Member member);
}
