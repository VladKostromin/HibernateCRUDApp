package com.crudapp.repository.hibernate;

import com.crudapp.model.Label;
import com.crudapp.model.Post;
import com.crudapp.repository.LabelRepository;
import com.crudapp.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.*;
import java.util.List;


public class HibernateLabelImpl implements LabelRepository {

    private static final String DELETE_HQL = "FROM Post p JOIN p.labels l WHERE l.id = :id";


    @Override
    public Label findByID(Long id) {
        try(Session session = HibernateUtils.getSession()) {
            return session.find(Label.class, id);
        }
    }

    @Override
    public List<Label> getAll() {
        try(Session session = HibernateUtils.getSession()) {
            return session.createQuery("FROM Label", Label.class).list();
        }
    }

    @Override
    public Label save(Label label) {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(label);
                transaction.commit();
                return label;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Label update(Label label) {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(Label.class);
                transaction.commit();
                return label;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Label deleteByID(Long id) {
        try (Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();

            List<Post> postList = session.createQuery(DELETE_HQL, Post.class)
                    .setParameter("id", id)
                    .list();
            for (Post p : postList) {
                p.getLabels().removeIf(label -> label.getId().equals(id));
                session.merge(p);
            }
            Label label = session.get(Label.class, id);
            if (label != null) {
                session.remove(label);
            } else {
                transaction.rollback();
                throw new EntityNotFoundException("Label with id " + id + " not found");
            }
            transaction.commit();
            return label;
        }
    }
}
