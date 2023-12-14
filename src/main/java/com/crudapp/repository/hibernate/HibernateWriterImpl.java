package com.crudapp.repository.hibernate;

import com.crudapp.model.Writer;
import com.crudapp.repository.WriterRepository;
import com.crudapp.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class HibernateWriterImpl implements WriterRepository {
    @Override
    public Writer findByID(Long id) {
        try(Session session = getSession()) {
            return session.find(Writer.class, id);
        }
    }

    @Override
    public List<Writer> getAll() {
        try(Session session = getSession()) {
            return session.createQuery("FROM Writer", Writer.class).list();
        }
    }

    @Override
    public Writer save(Writer writer) {
        try(Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(writer);
                transaction.commit();
                return writer;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Writer update(Writer writer) {
        try(Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                session.update(writer);
                transaction.commit();
                return writer;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Writer deleteByID(Long id) {
        try(Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Writer writer = session.find(Writer.class, id);
                if(writer != null) {
                    session.delete(writer);
                    transaction.commit();
                    return writer;
                } else {
                    transaction.rollback();
                    throw new EntityNotFoundException("Writer with id " + id + "not found");
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
