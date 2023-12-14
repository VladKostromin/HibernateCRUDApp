package com.crudapp.repository;

import com.crudapp.model.Post;

public interface PostRepository extends GenericRepository<Post, Long> {
    Post addLabelToPost(Long labelId, Long postId);
}
