package com.crudapp.controller;

import com.crudapp.model.Writer;
import com.crudapp.services.WriterService;

import java.util.ArrayList;
import java.util.List;

public class WriterController {
    private final WriterService writerService;

    public WriterController(WriterService writerService) {
        this.writerService = writerService;
    }

    public Writer getWriter (Long id) {
        return writerService.getWriterByIdWithoutDeletedPosts(id);
    }

    public List<Writer> getAllWriters() {
        return writerService.getAllWriters();
    }

    public Writer createWriter(String firstName, String lastName) {
        return writerService.createWriter(new Writer(null, firstName, lastName, new ArrayList<>()));
    }

    public Writer updateWriter(String firstName, String lastname, Long id) {
        return writerService.updateWriter(new Writer(id, firstName, lastname, null));
    }

    public Writer deleteWriter(Long id) {
        return writerService.deleteWriterByID(id);
    }
}
