package com.crudapp.services;

import com.crudapp.enums.PostStatus;
import com.crudapp.model.Post;
import com.crudapp.repository.PostRepository;
import com.crudapp.repository.hibernate.HibernatePostImpl;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private final PostRepository postRepository;

    public PostService() {
        this.postRepository = new HibernatePostImpl();
    }

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPostByID(Long id) {
        Post post = postRepository.findByID(id);
        if(post == null) throw new EntityNotFoundException("Post not found");
        return post;
    }

    public List<Post> getAllValidPosts() {
        return postRepository.getAll()
                .stream()
                .filter(post -> post.getPostStatus() != PostStatus.DELETED).collect(Collectors.toList());
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Post post) {
        Post updatedPost = postRepository.update(post);
        if(updatedPost == null) throw new EntityNotFoundException("Post not exist");
        return updatedPost;
    }

    public Post deletePostByID(Long id) {
        return postRepository.deleteByID(id);
    }

    public Post attachLabelToPost(Long postIdForLabel, Long labelId) {
        return postRepository.addLabelToPost(postIdForLabel, labelId);
    }
}
