package Mua.Mua_backend.global.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FcmService {

    public void send(String fcmToken, String message) {
        if (fcmToken == null || fcmToken.isBlank()) {
            log.warn("FCM 토큰 없음 - 전송 스킵");
            return;
        }

        Message firebaseMessage = Message.builder()
                .setToken(fcmToken)
                .setNotification(
                        Notification.builder()
                                .setTitle("MUA 알림")
                                .setBody(message)
                                .build()
                )
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(firebaseMessage);
            log.info("FCM 전송 성공: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 전송 실패", e);
        }
    }
}
