package Mua.Mua_backend.domain.feed.controller;

import Mua.Mua_backend.domain.feed.controller.docs.FeedControllerDocs;
import Mua.Mua_backend.domain.feed.dto.request.FeedCreateRequest;
import Mua.Mua_backend.domain.feed.dto.request.FeedUpdateRequest;
import Mua.Mua_backend.domain.feed.dto.response.FeedDetailResponse;
import Mua.Mua_backend.domain.feed.dto.response.FeedResponse;
import Mua.Mua_backend.domain.feed.service.FeedService;
import Mua.Mua_backend.domain.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController implements FeedControllerDocs {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> getFeeds(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) LocalDateTime cursorCreatedAt,
            @RequestParam(defaultValue = "LATEST") String sort,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                feedService.getFeeds(
                        cursorId,
                        cursorCreatedAt,
                        sort,
                        latitude,
                        longitude,
                        size
                )
        );
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedDetailResponse> getFeed(
            @PathVariable Long feedId
    ) {
        return ResponseEntity.ok(
                feedService.getFeedDetail(feedId)
        );
    }

    @PostMapping
    public Long createFeed(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody FeedCreateRequest request
    ) {
        return feedService.createFeed(member, request);
    }

    @PutMapping("/{feedId}")
    public void updateFeed(
            @PathVariable Long feedId,
            @AuthenticationPrincipal Member member,
            @RequestBody FeedUpdateRequest request
    ) {
        feedService.updateFeed(feedId, member, request);
    }

    @DeleteMapping("/{feedId}")
    public void deleteFeed(
            @PathVariable Long feedId,
            @AuthenticationPrincipal Member member
    ) {
        feedService.deleteFeed(feedId, member);
    }
}
