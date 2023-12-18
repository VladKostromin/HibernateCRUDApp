package com.crudapp.services;

import com.crudapp.enums.PostStatus;
import com.crudapp.model.Post;
import com.crudapp.model.Writer;
import com.crudapp.repository.WriterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WriterServiceTest {
    @Mock
    private WriterRepository writerRepository;
    @InjectMocks
    private WriterService writerService;

    private Writer writer;

    @BeforeEach
    public void setUpPost() {
        List<Post> postList = List.of(new Post(1L,
                "test content",
                PostStatus.ACTIVE,
                new Timestamp(new Date().getTime()),
                null,
                new Writer(),
                new ArrayList<>()), new Post(2L,
                "test updated contet",
                PostStatus.UNDER_REVIEW,
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                new Writer(),
                new ArrayList<>()), new Post(3L,
                "test deleted content",
                PostStatus.DELETED,
                new Timestamp(new Date().getTime()),
                null,
                new Writer(),
                new ArrayList<>()));
        writer = new Writer(1L, "testName", "testLastName", postList);
    }
    @Test
    public void testGetWriterByIdNoDeletedPosts_Found() {
        when(writerRepository.findByID(writer.getId())).thenReturn(writer);
        List<Post> filteredPosts = writer.getPosts()
                .stream()
                .filter(post -> post.getPostStatus() != PostStatus.DELETED)
                .collect(Collectors.toList());
        Writer writerWithFilteredPosts = new Writer(1L, "testName", "testLastName", filteredPosts);
        Writer result = writerService.getWriterByIdWithoutDeletedPosts(writer.getId());
        System.out.println(result);
        assertEquals(writerWithFilteredPosts, result);
        verify(writerRepository).findByID(writer.getId());
    }
    @Test
    public void testGetWriterByIdNoDeletedPosts_NotFound() {
        when(writerRepository.findByID(writer.getId())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> writerService.getWriterByIdWithoutDeletedPosts(writer.getId()));
    }
    @Test
    public void testCreateWriter() {
        when(writerRepository.save(writer)).thenReturn(writer);
        Writer resultWriter = writerService.createWriter(writer);
        assertEquals(writer, resultWriter);
        verify(writerRepository).save(writer);
    }

    @Test
    public void testUpdateWriter_Found() {
        when(writerRepository.update(writer)).thenReturn(writer);
        Writer resultWriter = writerService.updateWriter(writer);
        assertEquals(writer, resultWriter);
    }

    @Test
    public void testUpdateWriter_NotFound() {
        when(writerRepository.update(writer)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> writerService.updateWriter(writer));
    }

    @Test
    public void testGetAllWriters() {
        List<Writer> writerList = List.of(writer, writer, writer, writer, writer);
        when(writerRepository.getAll()).thenReturn(writerList);
        List<Writer> resultList = writerService.getAllWriters();
        assertEquals(writerList, resultList);
        verify(writerRepository).getAll();
    }

    @Test
    public void testDeleteWriter() {
        when(writerRepository.deleteByID(writer.getId())).thenReturn(writer);
        Writer resultWriter = writerService.deleteWriterByID(writer.getId());
        assertEquals(writer, resultWriter);
        verify(writerRepository).deleteByID(writer.getId());
    }
}
