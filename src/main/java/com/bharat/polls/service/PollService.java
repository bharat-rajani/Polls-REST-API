package com.bharat.polls.service;

import com.bharat.polls.exceptions.BadRequestException;
import com.bharat.polls.model.Poll;
import com.bharat.polls.payload.PagedResponse;
import com.bharat.polls.payload.PollResponse;
import com.bharat.polls.repository.PollRepository;
import com.bharat.polls.repository.UserRepository;
import com.bharat.polls.repository.VoteRepository;
import com.bharat.polls.security.UserPrincipal;
import com.bharat.polls.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    private static final Logger logger = LoggerFactory.getLogger(PollService.class);

    private void validatePageNumberAndSize(int page,int size){
        if(page<0){
            throw new BadRequestException("Page number cannot be less than zero");
        }

        if(size<0 || size> AppConstants.MAX_PAGE_SIZE){
            throw new BadRequestException("Page size must not be greater than "+AppConstants.MAX_PAGE_SIZE);
        }
    }

    public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser,int page,int size){
        validatePageNumberAndSize(page,size);

        Pageable pageable = PageRequest.of(page,size, Sort.Direction.DESC,"createdAt");
        Page<Poll> polls = pollRepository.findAll(pageable);

        if(polls.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(),polls.getNumber(),
                    polls.getSize(),polls.getTotalElements(),polls.getTotalPages(),polls.isLast());
        }

        List<Long> pollIds = polls.map(Poll::getId).getContent();
        Map<Long,Long> choiceVoteCount = getChoiceVoteCountMap(pollIds);
        
    }
}
