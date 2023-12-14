package com.crudapp.repository.hibernate;

import com.crudapp.enums.PostStatus;
import com.crudapp.model.Label;
import com.crudapp.model.Post;
import com.crudapp.repository.PostRepository;
import com.crudapp.utils.HibernateUtils;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class HibernatePostImpl implements PostRepository {

    private static final String updateHQL = "update Post set content = :content, postStatus = :status, updated = :updated where id = :postId";
    private static final String addLabelToPostHQL = "update Post set postId =: post_id, labelId =:label_id";
    @Override
    public Post findByID(Long id) {
        try(Session session = getSession()) {
            return session.find(Post.class, id);
        }
    }

    @Override
    public List<Post> getAll() {
        try(Session session = getSession()) {
            return session.createQuery("FROM Post", Post.class).list();
        }
    }

    @Override
    public Post save(Post post) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(post);
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
        try(Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.createQuery(updateHQL)
                        .setParameter("content", post.getContent())
                        .setParameter("status", post.getPostStatus())
                        .setParameter("updated", post.getUpdated())
                        .setParameter("postId", post.getId())
                        .executeUpdate();
                return session.find(Post.class, post.getId());
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Post deleteByID(Long id) {
        try(Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Post post = session.find(Post.class, id);
                if(post != null) {
                    post.setPostStatus(PostStatus.DELETED);
                    session.update(post);
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

    private Session getSession () {
        return HibernateUtils.getSessionFactory().openSession();
    }

    @Override
    public Post addLabelToPost(Long postId, Long labelId) {
        try(Session session = getSession()) {
            Post post = session.get(Post.class, postId);
            Label label = session.get(Label.class, labelId);
            if(post == null && label == null) throw new EntityNotFoundException("Post and Label not found");
            if(post == null) throw new EntityNotFoundException("Post not found");
            if(label == null) throw new EntityNotFoundException("Label not found");
            post.getLabels().add(label);
            session.beginTransaction();
            session.saveOrUpdate(post);
            session.getTransaction().commit();
            return post;
        }
    }
}
