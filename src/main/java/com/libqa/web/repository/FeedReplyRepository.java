package com.libqa.web.repository;

import com.libqa.web.domain.Feed;
import com.libqa.web.domain.FeedReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedReplyRepository extends JpaRepository<FeedReply, Integer> {
    List<FeedReply> findByFeedFeedId(Integer feedId);
    Integer countByFeed(Feed feed);
    Integer countByFeedAndIsDeletedFalse(Feed feed);

}
