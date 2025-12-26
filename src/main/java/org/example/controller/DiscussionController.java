package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Discussion;
import org.example.model.DiscussionDto;
import org.example.service.DiscussionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @GetMapping("/discussions")
    public String discussions(@RequestParam(required = false) String q,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, Principal principal) {

        model.addAttribute("username", principal != null ? principal.getName() : null);
        model.addAttribute("query", q);

        if (q != null && !q.isBlank()) {
            model.addAttribute("discussions", discussionService.searchDiscussions(q));
            return "discussions";
        }

        List<DiscussionDto> all = discussionService.getAllDiscussionsDto();
        int total = all.size();
        int totalPages = (int) Math.ceil((double) total / size);

        int from = (page - 1) * size;
        int to = Math.min(from + size, total);

        List<DiscussionDto> pageContent = all.subList(from, to);

        model.addAttribute("discussions", pageContent);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

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

    @PostMapping("/discussions/add")
    public String addDiscussion(@RequestParam String title,
                                @RequestParam String description,
                                Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        discussionService.createDiscussion(title, description, principal.getName());
        return "redirect:/discussions";
    }
}