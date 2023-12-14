package com.crudapp.controller;
import com.crudapp.model.Label;
import com.crudapp.services.LabelService;

import java.util.List;

public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    public Label getLabel(Long id) {
        return labelService.getLabelByID(id);
    }

    public Label createLabel(String labelName) {
        return labelService.createLabel(new Label(null, labelName));
    }
    public Label updateLabel(String labelName, Long labelId) {
        return labelService.updateLabel(new Label(labelId, labelName));
    }
    public void deleteLabel(Long id) {
        labelService.deleteLabelById(id);
    }
    public List<Label> getAllLabels(){
        return labelService.getAllLabels();
    }

}
