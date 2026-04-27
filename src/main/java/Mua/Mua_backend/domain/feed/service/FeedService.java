package Mua.Mua_backend.domain.feed.service;

import Mua.Mua_backend.domain.feed.dto.request.FeedCreateRequest;
import Mua.Mua_backend.domain.feed.dto.request.FeedUpdateRequest;
import Mua.Mua_backend.domain.feed.dto.response.FeedCursorResponse;
import Mua.Mua_backend.domain.feed.dto.response.FeedDetailResponse;
import Mua.Mua_backend.domain.feed.dto.response.FeedResponse;
import Mua.Mua_backend.domain.feed.dto.response.WriterResponse;
import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.feed.entity.Location;
import Mua.Mua_backend.domain.feed.repository.FeedRepository;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.exception.feed.FeedNotFoundException;
import Mua.Mua_backend.global.exception.feed.LocationRequiredException;
import Mua.Mua_backend.global.exception.feed.NoFeedUpdateContentException;
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
    public FeedCursorResponse getFeeds(
            Long cursorId,
            LocalDateTime cursorCreatedAt,
            String sort,
            Double latitude,
            Double longitude,
            int size
    ) {
        List<Feed> feeds;
        PageRequest pageable = PageRequest.of(0, size + 1);

        if ("DISTANCE".equals(sort)) {
            if (latitude == null || longitude == null) {
                throw new LocationRequiredException();
            }

            feeds = feedRepository.findNearestFeeds(
                    cursorId,
                    latitude,
                    longitude,
                    pageable
            );
        } else {
            feeds = feedRepository.findLatestFeeds(
                    cursorId,
                    cursorCreatedAt,
                    pageable
            );
        }

        boolean hasNext = feeds.size() > size;

        if (hasNext) {
            feeds = feeds.subList(0, size);
        }

        List<FeedResponse> responses = feeds.stream()
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

        Long nextCursorId = null;
        LocalDateTime nextCursorCreatedAt = null;

        if (!feeds.isEmpty()) {
            Feed lastFeed = feeds.get(feeds.size() - 1);
            nextCursorId = lastFeed.getId();
            nextCursorCreatedAt = lastFeed.getCreatedAt();
        }

        return new FeedCursorResponse(
                responses,
                nextCursorId,
                nextCursorCreatedAt,
                hasNext
        );
    }

    @Transactional(readOnly = true)
    public FeedDetailResponse getFeedDetail(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

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
                .location(Location.of(
                        request.address(),
                        request.latitude(),
                        request.longitude()
                ))
                .build();

        return feedRepository.save(feed).getId();
    }

    public void updateFeed(Long feedId, Member writer, FeedUpdateRequest request) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);
        feed.assertWrittenBy(writer);

        if (request.isAllNull()) {
            throw new NoFeedUpdateContentException();
        }

        Location location = hasLocationUpdate(request)
                ? mergeLocation(feed, request)
                : null;

        feed.update(
                request.title(),
                request.image(),
                request.description(),
                request.timer(),
                request.playGround(),
                request.playDate(),
                request.round(),
                location
        );
    }

    private boolean hasLocationUpdate(FeedUpdateRequest request) {
        return request.address() != null
                || request.latitude() != null
                || request.longitude() != null;
    }

    private Location mergeLocation(Feed feed, FeedUpdateRequest request) {
        Location current = feed.getLocation();

        String address = request.address() != null
                ? request.address()
                : current != null ? current.getAddress() : null;

        Double latitude = request.latitude() != null
                ? request.latitude()
                : current != null ? current.getLatitude() : null;

        Double longitude = request.longitude() != null
                ? request.longitude()
                : current != null ? current.getLongitude() : null;

        return Location.of(address, latitude, longitude);
    }

    public void deleteFeed(Long feedId, Member writer) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);
        feed.assertWrittenBy(writer);
        feedRepository.delete(feed);
    }
}
