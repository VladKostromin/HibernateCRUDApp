package com.crudapp.repository.hibernate;

import com.crudapp.model.Writer;
import com.crudapp.repository.WriterRepository;
import com.crudapp.utils.HibernateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateWriterImpl implements WriterRepository {

    private static final String FIND_HQL = "FROM  Writer w LEFT JOIN FETCH w.posts WHERE w.id = :id";
    private static final String GET_ALL_HQL = "FROM Writer w LEFT JOIN FETCH w.posts";

    @Override
    public Writer findByID(Long id) {
        try(Session session = HibernateUtils.getSession()) {
            Writer writer = session.createQuery(FIND_HQL, Writer.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (writer != null) {
                writer.getPosts().forEach(post -> Hibernate.initialize(post.getLabels()));
            }
            return writer;
        }
    }

    @Override
    public List<Writer> getAll() {
        try(Session session = HibernateUtils.getSession()) {
            List<Writer> writerList = session.createQuery(GET_ALL_HQL, Writer.class).list();
            writerList.stream().forEach(writer -> writer.getPosts().forEach(post -> Hibernate.initialize(post.getLabels())));
            return writerList;
        }
    }

    @Override
    public Writer save(Writer writer) {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(writer);
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
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                Writer writerToUpdate = findByID(writer.getId());
                writerToUpdate.setFirstName(writer.getFirstName());
                writerToUpdate.setLastName(writer.getLastName());
                session.merge(writerToUpdate);
                transaction.commit();
                return writerToUpdate;
            } catch (Throwable e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Writer deleteByID(Long id) {
        try(Session session = HibernateUtils.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Writer writer = findByID(id);
                if(writer != null) {
                    session.remove(writer);
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

}
