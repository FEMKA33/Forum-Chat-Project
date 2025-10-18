package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Discussion;
import org.example.service.DiscussionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @GetMapping({"/discussions"})
    public String discussions(Model model, Principal principal) {
        model.addAttribute("discussions", discussionService.getAllDiscussionsDto());
        model.addAttribute("username", principal != null ? principal.getName() : null);
        return "discussions";
    }

    @GetMapping("/discussions/{id}")
    public String viewDiscussion(@PathVariable Long id, Model model, Principal principal) {
        Discussion discussion = discussionService.getDiscussion(id);

        model.addAttribute("discussion", discussion);
        model.addAttribute("username", principal != null ? principal.getName() : null);
        model.addAttribute("messages", discussionService.getMessages(id));

        return "discussion-view";
    }
}