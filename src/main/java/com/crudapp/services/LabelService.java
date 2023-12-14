package com.crudapp.services;

import com.crudapp.model.Label;
import com.crudapp.model.Writer;
import com.crudapp.repository.LabelRepository;
import com.crudapp.repository.WriterRepository;
import com.crudapp.repository.hibernate.HibernateLabelImpl;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService() {
        this.labelRepository = new HibernateLabelImpl();
    }

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public Label getLabelByID(Long id) {
        Label label = labelRepository.findByID(id);
        if(label == null) throw new EntityNotFoundException("Label not found");
        return label;
    }

    public List<Label> getAllLabels() {
        return labelRepository.getAll();
    }

    public Label createLabel(Label label) {
        return labelRepository.save(label);
    }

    public Label updateLabel(Label label) {
        Label updatedLabel = labelRepository.update(label);
        if(updatedLabel == null) throw new EntityNotFoundException("Label not found");
        return updatedLabel;
    }

    public Label deleteLabelById(Long id) {
        return labelRepository.deleteByID(id);
    }
}
