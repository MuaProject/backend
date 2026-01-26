package Mua.Mua_backend.domain.feed.service;

import Mua.Mua_backend.domain.feed.dto.request.FeedCreateRequest;
import Mua.Mua_backend.domain.feed.dto.request.FeedUpdateRequest;
import Mua.Mua_backend.domain.feed.dto.response.FeedDetailResponse;
import Mua.Mua_backend.domain.feed.dto.response.FeedResponse;
import Mua.Mua_backend.domain.feed.dto.response.WriterResponse;
import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.feed.repository.FeedRepository;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.exception.example.FeedNotFoundException;
import Mua.Mua_backend.global.exception.example.FeedUpdateForbiddenException;
import Mua.Mua_backend.global.exception.example.LocationRequiredException;
import Mua.Mua_backend.global.exception.example.NoFeedUpdateContentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional(readOnly = true)
    public List<FeedResponse> getFeeds(
            Long cursorId,
            LocalDateTime cursorCreatedAt,
            String sort,
            Double latitude,
            Double longitude,
            int size
    ) {
        List<Feed> feeds;

        if ("DISTANCE".equals(sort)) {
            if (latitude == null || longitude == null) {
                throw new LocationRequiredException();
            }

            feeds = feedRepository.findNearestFeeds(
                    cursorId,
                    latitude,
                    longitude,
                    PageRequest.of(0, size)
            );
        } else {
            feeds = feedRepository.findLatestFeeds(
                    cursorId,
                    cursorCreatedAt,
                    PageRequest.of(0, size)
            );
        }

        return feeds.stream()
                .map(feed -> new FeedResponse(
                        feed.getId(),
                        feed.getTitle(),
                        feed.getImage(),
                        feed.getPlayGround(),
                        feed.getPlayDate(),
                        feed.getPlayCount(),
                        feed.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public FeedDetailResponse getFeedDetail(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException());

        return new FeedDetailResponse(
                feed.getId(),
                feed.getImage(),
                feed.getTitle(),
                feed.getPlayGround(),
                feed.getPlayDate(),
                feed.getRound(),
                feed.getPlayCount(),
                feed.getDescription(),
                feed.getTimer(),
                new WriterResponse(
                        feed.getWriter().getId(),
                        feed.getWriter().getNickname()
                )
        );
    }

    public Long createFeed(Member writer, FeedCreateRequest request) {
        Feed feed = Feed.builder()
                .writer(writer)
                .image(request.image())
                .title(request.title())
                .playGround(request.playGround())
                .playDate(request.playDate())
                .round(request.round())
                .playCount(request.playCount())
                .description(request.description())
                .timer(request.timer())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();

        return feedRepository.save(feed).getId();
    }

    public void updateFeed(Long feedId, Member writer, FeedUpdateRequest request) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException());
        validateWriter(feed, writer);

        if (request.isAllNull()) {
            throw new NoFeedUpdateContentException();
        }

        feed.update(request);
    }

    public void deleteFeed(Long feedId, Member writer) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException());
        validateWriter(feed, writer);
        feedRepository.delete(feed);
    }

    private void validateWriter(Feed feed, Member writer) {
        if (!feed.getWriter().getId().equals(writer.getId())) {
            throw new FeedUpdateForbiddenException();
        }
    }
}
