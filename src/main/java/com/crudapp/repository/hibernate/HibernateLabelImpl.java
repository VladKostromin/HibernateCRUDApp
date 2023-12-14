package com.crudapp.repository.hibernate;

import com.crudapp.model.Label;
import com.crudapp.repository.LabelRepository;
import com.crudapp.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityNotFoundException;
import java.util.List;


public class HibernateLabelImpl implements LabelRepository {


    @Override
    public Label findByID(Long id) {
        try(Session session = getSession()) {
            return session.find(Label.class, id);
        }
    }

    @Override
    public List<Label> getAll() {
        try(Session session = getSession()) {
            return session.createQuery("FROM Label", Label.class).list();
        }
    }

    @Override
    public Label save(Label label) {
        try(Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(label);
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
        try(Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(Label.class);
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
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Label label = session.find(Label.class, id);
                if(label != null) {
                    session.delete(label);
                    transaction.commit();
                    return label;
                } else {
                    transaction.rollback();
                    throw new EntityNotFoundException("Label with id " + id + " not found");
                }
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    private Session getSession() {
        return HibernateUtils.getSessionFactory().openSession();
    }
}
