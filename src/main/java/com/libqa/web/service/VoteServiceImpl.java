package com.libqa.web.service;

import com.libqa.web.domain.QaReply;
import com.libqa.web.domain.Vote;
import com.libqa.web.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by yong on 2015-05-25.
 *
 * @author yong
 */
@Slf4j
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Override
    public boolean isNotVote(QaReply qaReply, Integer userId) {
        boolean isCancel = false;
        List<Vote> voteList = voteRepository.findByReplyIdAndUserIdAndIsCancel(qaReply.getReplyId(), userId, isCancel);

        return CollectionUtils.isEmpty(voteList);
    }

    @Override
    public void saveByQaReply(QaReply qaReply, Integer userId) {
        Vote vote = new Vote();
        vote.setReplyId(qaReply.getReplyId());
        vote.setUserId(userId);

        voteRepository.save(vote);
    }
}