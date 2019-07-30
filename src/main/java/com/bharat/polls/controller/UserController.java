package com.bharat.polls.controller;

import com.bharat.polls.exceptions.ResourceNotFoundException;
import com.bharat.polls.model.User;
import com.bharat.polls.payload.*;
import com.bharat.polls.repository.PollRepository;
import com.bharat.polls.repository.UserRepository;
import com.bharat.polls.repository.VoteRepository;
import com.bharat.polls.security.CurrentUser;
import com.bharat.polls.security.UserPrincipal;
import com.bharat.polls.service.PollService;
import com.bharat.polls.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value="username") String username){
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value="email") String email){
        Boolean isAvailable = !userRepository.existsByUsername(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/{username}")
    public UserProfile getUserProfile(@RequestParam(value="username") String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User","username",username));
        Long pollCount = pollRepository.countByCreatedBy(user.getId());
        Long voteCount = voteRepository.countByUserId(user.getId());

        return new UserProfile(user.getId(),user.getUsername(),user.getName(),user.getCreatedAt(),pollCount,voteCount);

    }

    @GetMapping("/user/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
                                                         ){
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/user/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value="page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value="size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return pollService.getPollsVotedBy(username,currentUser,page,size);
    }
}
