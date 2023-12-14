package com.crudapp.services;

import com.crudapp.model.Label;
import com.crudapp.repository.LabelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {

    @Mock
    private LabelRepository labelRepository;
    @InjectMocks
    private LabelService labelService;

    private Label label;

    @BeforeEach
    public void setUpLabel() {
        label = new Label(1L, "test label");
    }

    @Test
    public void testGetLabelById_Found() {
        when(labelRepository.findByID(label.getId())).thenReturn(label);
        Label resultLabel = labelService.getLabelByID(label.getId());
        Assertions.assertEquals(label, resultLabel);
        verify(labelRepository).findByID(label.getId());
    }

    @Test
    public void testGetLabelById_NotFound() {
        when(labelRepository.findByID(label.getId())).thenReturn(null);
        Assertions.assertThrows(EntityNotFoundException.class, () -> labelService.getLabelByID(label.getId()));
    }

    @Test
    public void testCreateLabel() {
        Label labelToSave = new Label(1L, "test label for save");
        when(labelRepository.save(label)).thenReturn(labelToSave);
        Label resultLabel = labelService.createLabel(label);
        Assertions.assertEquals(labelToSave, resultLabel);
        verify(labelRepository).save(label);
    }

    @Test
    public void testUpdateLabel_Found() {
        when(labelRepository.update(label)).thenReturn(label);
        Label resultLabel = labelService.updateLabel(label);
        Assertions.assertEquals(label, resultLabel);
        verify(labelRepository).update(label);
    }

    @Test
    public void testUpdateLabel_NotFound() {
        when(labelRepository.update(label)).thenReturn(null);
        Assertions.assertThrows(EntityNotFoundException.class, () -> labelService.updateLabel(label));
    }

    @Test
    public void testGetAllLabels() {
        List<Label> labels = List.of(label, label, label, label);
        when(labelRepository.getAll()).thenReturn(labels);
        List<Label> resultList = labelService.getAllLabels();
        Assertions.assertEquals(labels, resultList);
        verify(labelRepository).getAll();
    }

    @Test
    public void testDeleteLabel() {
        when(labelRepository.deleteByID(label.getId())).thenReturn(label);
        Label resultLabel = labelService.deleteLabelById(label.getId());
        Assertions.assertEquals(label, resultLabel);
    }

}
