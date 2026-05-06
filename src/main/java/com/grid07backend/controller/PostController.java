package com.grid07backend.controller;

import com.grid07backend.entity.Comment;
import com.grid07backend.entity.Post;
import com.grid07backend.repository.PostRepository;
import com.grid07backend.repository.CommentRepository;
import com.grid07backend.service.ViralityService;
import com.grid07backend.service.GuardrailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final ViralityService viralityService;
    private final GuardrailService guardrailService;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepo.save(post);
    }
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId,
                                        @RequestBody Comment comment) {

        if (comment.getDepthLevel() > 20)
            return ResponseEntity.badRequest().body("Depth exceeded");

        if (comment.getAuthorType().equals("BOT")) {
            if (!guardrailService.checkHorizontal(postId))
                return ResponseEntity.status(429).body("Bot limit reached");
        }

        comment.setPostId(postId);
        commentRepo.save(comment);

        viralityService.updateScore(postId,
                comment.getAuthorType().equals("BOT") ? "BOT_REPLY" : "COMMENT");

        return ResponseEntity.ok(comment);
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable Long postId) {
        viralityService.updateScore(postId, "LIKE");
        return "Liked!";
    }

}
