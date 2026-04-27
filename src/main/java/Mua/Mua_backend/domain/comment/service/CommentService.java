package Mua.Mua_backend.domain.comment.service;

import Mua.Mua_backend.domain.comment.dto.request.CommentCreateRequest;
import Mua.Mua_backend.domain.comment.dto.response.CommentResponse;
import Mua.Mua_backend.domain.comment.entity.Comment;
import Mua.Mua_backend.domain.comment.entity.CommentType;
import Mua.Mua_backend.domain.comment.repository.CommentRepository;
import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.feed.repository.FeedRepository;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.member.repository.MemberRepository;
import Mua.Mua_backend.global.exception.comment.CommentFeedMismatchException;
import Mua.Mua_backend.global.exception.comment.CommentNotFoundException;
import Mua.Mua_backend.global.exception.feed.FeedNotFoundException;
import Mua.Mua_backend.global.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;

    public void createComment(Long feedId, Long memberId, CommentCreateRequest request) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Comment comment;
        if(request.parentId() == null) {
            comment = Comment.createRootComment(
                    request.description(),
                    feed,
                    member
            );
        } else {
            Comment parent = findParentInSameFeed(feedId, request.parentId());
            comment = Comment.createReplyComment(
                    request.description(),
                    feed,
                    member,
                    parent
            );
        }

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long feedId) {
        List<Comment> comments =
                commentRepository.findByFeedIdOrderByCreatedAtAsc(feedId);

        Map<Long, CommentResponse> commentMap = new LinkedHashMap<>();
        List<CommentResponse> result = new ArrayList<>();

        for (Comment comment : comments) {
            commentMap.put(comment.getId(), CommentResponse.from(comment));
        }

        for (Comment comment : comments) {
            CommentResponse dto = commentMap.get(comment.getId());

            if (comment.getParentId() == null) {
                result.add(dto);
            } else {
                CommentResponse parent = commentMap.get(comment.getParentId());
                parent.children().add(dto);
            }
        }

        return result;
    }

    public void createEventComment(
            Long feedId,
            Long participationId,
            String message,
            CommentType type
    ) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        Comment comment = Comment.createEventComment(
                message,
                feed,
                participationId,
                type
        );

        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        comment.deleteBy(memberId);
    }

    public void updateEventComment(Long participationId, CommentType type) {
        Comment comment = commentRepository.findByParticipationId(participationId)
                .orElseThrow(CommentNotFoundException::new);

        comment.changeType(type);
    }

    private Comment findParentInSameFeed(Long feedId, Long parentId) {
        return commentRepository.findByIdAndFeedId(parentId, feedId)
                .orElseThrow(CommentNotFoundException::new);
    }
}
