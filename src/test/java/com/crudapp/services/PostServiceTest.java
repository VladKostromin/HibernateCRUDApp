package com.crudapp.services;

import com.crudapp.enums.PostStatus;
import com.crudapp.model.Post;
import com.crudapp.model.Writer;
import com.crudapp.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    public void setUpPost() {
        post = new Post(1L,
                "test content",
                PostStatus.ACTIVE,
                new Timestamp(new Date().getTime()),
                null,
                new Writer(),
                new ArrayList<>());
    }
    @Test
    public void testGetPostByID_Found() {
        when(postRepository.findByID(post.getId())).thenReturn(post);
        Post resultPost = postService.getPostByID(post.getId());
        assertEquals(post, resultPost);
        verify(postRepository).findByID(post.getId());
    }
    @Test
    public void testGetPostByID_NotFound() {
        when(postRepository.findByID(post.getId())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> postService.getPostByID(post.getId()));
    }
    @Test
    public void testCreatePost() {
        when(postRepository.save(post)).thenReturn(post);
        Post resultPost = postService.createPost(post);
        assertEquals(post, resultPost);
        verify(postRepository).save(post);
    }

    @Test
    public void testUpdatePost_Found() {
        Post updatedPost = new Post(1L,
                "test updated content",
                PostStatus.UNDER_REVIEW,
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                new Writer(),
                new ArrayList<>());
        when(postRepository.update(post)).thenReturn(updatedPost);
        Post resultPost = postService.updatePost(post);
        assertEquals(updatedPost, resultPost);
        assertEquals(resultPost.getPostStatus(), PostStatus.UNDER_REVIEW);
        verify(postRepository).update(post);
    }

    @Test
    public void testUpdatePost_NotFound() {
        when(postRepository.update(post)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(post));
    }

    @Test
    public void testGetAllPostsWithoutDeletedStatus() {
        List<Post> allPosts = List.of(post, post, post, post, post, post, post);
        allPosts.get(0).setPostStatus(PostStatus.DELETED);
        allPosts.get(1).setPostStatus(PostStatus.UNDER_REVIEW);
        allPosts.get(3).setPostStatus(PostStatus.DELETED);
        allPosts.get(4).setPostStatus(PostStatus.DELETED);
        allPosts.get(6).setPostStatus(PostStatus.UNDER_REVIEW);
        when(postRepository.getAll()).thenReturn(allPosts);
        List<Post> filteredList = allPosts
                .stream()
                .filter(post -> post.getPostStatus() != PostStatus.DELETED).collect(Collectors.toList());
        List<Post> resultList = postService.getAllValidPosts();
        assertEquals(filteredList, resultList);
        verify(postRepository).getAll();

    }

    @Test
    public void testDeletePost() {
        when(postRepository.deleteByID(post.getId())).thenReturn(post);
        Post deletedPost = postService.deletePostByID(post.getId());
        assertEquals(post, deletedPost);
        verify(postRepository).deleteByID(post.getId());
    }
}
