package com.crudapp.repository.hibernate;

import com.crudapp.enums.PostStatus;
import com.crudapp.model.Label;
import com.crudapp.model.Post;
import com.crudapp.repository.PostRepository;
import com.crudapp.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.*;
import java.util.List;

public class HibernatePostImpl implements PostRepository {

    private static final String FIND_HQL = "FROM Post p LEFT JOIN FETCH p.labels WHERE p.id = :id";
    private static final String GET_ALL_HQL = "FROM Post p LEFT JOIN FETCH p.labels LEFT JOIN FETCH p.writer";

    @Override
    public Post findByID(Long id) {
        try(Session session = HibernateUtils.getSession()) {
            return session.createQuery(FIND_HQL, Post.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }

    @Override
    public List<Post> getAll() {
        try(Session session = HibernateUtils.getSession()) {
            return session.createQuery(GET_ALL_HQL, Post.class)
                    .list();
        }
    }

    @Override
    public Post save(Post post) {
        try (Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(post);
                transaction.commit();
                return post;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Post update(Post post) {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Post postToUpdate = findByID(post.getId());
                postToUpdate.setUpdated(post.getUpdated());
                postToUpdate.setContent(post.getContent());
                session.merge(postToUpdate);
                transaction.commit();
                return postToUpdate;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Post deleteByID(Long id) {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Post post = session.find(Post.class, id);
                if(post != null) {
                    post.setPostStatus(PostStatus.DELETED);
                    session.persist(post);
                    transaction.commit();
                    return post;
                } else {
                    throw new EntityNotFoundException("Post with id " + id + " not found");
                }
            } catch (Throwable e) {
                throw e;
            }
        }
    }


    @Override
    public Post addLabelToPost(Long postId, Long labelId) {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Post post = findByID(postId);
                Label label = session.get(Label.class, labelId);
                if(post == null && label == null) throw new EntityNotFoundException("Post and Label not found");
                if(post == null) throw new EntityNotFoundException("Post not found");
                if(label == null) throw new EntityNotFoundException("Label not found");
                post.getLabels().add(label);
                session.merge(post);
                transaction.commit();
                return post;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }
}
