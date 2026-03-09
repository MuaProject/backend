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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // 댓글, 대댓글 생성
    public void createComment(Long feedId, Long memberId, CommentCreateRequest request) {

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        if (request.parentId() != null) {
            Comment parent = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));

            if (parent.getDepth() == 1) {
                throw new IllegalArgumentException("대댓글에는 댓글을 달 수 없습니다.");
            }
        }

        Comment comment = Comment.createUserComment(
                request.description(),
                feed,
                member,
                request.parentId()
        );

        commentRepository.save(comment);
    }

    // 댓글 조회
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

    // 시스템 댓글 생성
    public void createEventComment(
            Long feedId,
            Long participationId,
            String message,
            CommentType type
    ) {

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));

        Comment comment = Comment.createEventComment(
                message,
                feed,
                participationId,
                type
        );

        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (comment.getCommentType() != CommentType.USER) {
            throw new IllegalArgumentException("이벤트 댓글은 삭제할 수 없습니다.");
        }

        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        comment.delete();
    }
}
