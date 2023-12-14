package com.crudapp.controller;

import com.crudapp.enums.PostStatus;
import com.crudapp.model.Post;
import com.crudapp.services.PostService;
import com.crudapp.services.WriterService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostController {
    private final PostService postService;
    private final WriterService writerService;

    public PostController(PostService postService) {
        this.postService = postService;
        this.writerService = new WriterService();
    }

    public Post getPost(Long id) {
        return postService.getPostByID(id);
    }

    public Post createNewPost(String content, Long writerId) {
        return postService.createPost(new Post(null, content, PostStatus.ACTIVE, new Timestamp(new Date().getTime()), null, writerService.getWriterByIdWithoutDeletedPosts(writerId), new ArrayList<>()));
    }
    public Post updatePostContent(String content, Long postIdForUpdate) {
        return postService.updatePost(new Post(postIdForUpdate, content, PostStatus.UNDER_REVIEW, null, new Timestamp(new Date().getTime()), null, null));
    }
    public void deletePost(Long id) {
        postService.deletePostByID(id);
    }

    public List<Post> getAllPosts(){
        return postService.getAllValidPosts();
    }

    public Post addLabelToPost(Long postIdForLabel, Long labelId) {
        return postService.attachLabelToPost(postIdForLabel, labelId);
    }
}
