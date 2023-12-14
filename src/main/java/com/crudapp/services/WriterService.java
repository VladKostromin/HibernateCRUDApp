package com.crudapp.services;

import com.crudapp.enums.PostStatus;
import com.crudapp.model.Post;
import com.crudapp.model.Writer;
import com.crudapp.repository.WriterRepository;
import com.crudapp.repository.hibernate.HibernateWriterImpl;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class WriterService {

    private final WriterRepository writerRepository;

    public WriterService () {
        this.writerRepository = new HibernateWriterImpl();
    }

    public WriterService(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    public Writer getWriterByIdWithoutDeletedPosts(Long id) {
        Writer writer = writerRepository.findByID(id);
        if(writer == null) throw new EntityNotFoundException("Writer not found");
        List<Post> filteredPosts = writer.getPosts()
                .stream()
                .filter(post -> post.getPostStatus() != PostStatus.DELETED).collect(Collectors.toList());
        writer.setPosts(filteredPosts);
        return writer;
    }

    public List<Writer> getAllWriters() {
        return writerRepository.getAll();
    }

    public Writer createWriter(Writer writer) {
        return writerRepository.save(writer);
    }

    public Writer updateWriter(Writer writer) {
        Writer updatedWriter = writerRepository.update(writer);
        if(updatedWriter == null) throw new EntityNotFoundException("Writer not found");
        return updatedWriter;
    }

    public Writer deleteWriterByID(Long id) {
        return writerRepository.deleteByID(id);
    }
}
