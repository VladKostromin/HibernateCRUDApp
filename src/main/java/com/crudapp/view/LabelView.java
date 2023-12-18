package com.crudapp.view;

import com.crudapp.controller.LabelController;
import com.crudapp.model.Label;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LabelView {
    private final LabelController labelController;
    private final Scanner scanner;

    public LabelView(LabelController labelController) {
        this.labelController = labelController;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean flag = true;
        int inputOption;
        while (flag) {
            System.out.println("1. Создать новый Label");
            System.out.println("2. Получить Label");
            System.out.println("3. Обновить существующий Label");
            System.out.println("4. Получить все Label's");
            System.out.println("5. Удалить Label");
            System.out.println("6. Вернутся в меню программы");
            inputOption = scanner.nextInt();
            scanner.nextLine();
            switch (inputOption) {
                case 1 :
                    System.out.print("Введите название Label: ");
                    String name = scanner.nextLine();
                    labelController.createLabel(name);
                    System.out.println("Label создан");
                    break;
                case 2 :
                    System.out.print("Введите id Label: ");
                    Long labelId;
                    try {
                        labelId = scanner.nextLong();
                    } catch (Exception e) {
                        System.out.println("Неверный ввод");
                        break;
                    }
                    scanner.nextLine();
                    Label label;
                    try {
                        label = labelController.getLabel(labelId);
                    } catch (EntityNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println("Label получен:");
                    System.out.println(label);
                    break;
                case 3 :
                    System.out.print("Введите id Label для обновления: ");
                    Long labelIdForUpdate;
                    try {
                        labelIdForUpdate = scanner.nextLong();
                    } catch (Exception e) {
                        System.out.println("Неверный ввод");
                        break;
                    }
                    scanner.nextLine();
                    Label labelForUpdate = labelController.getLabel(labelIdForUpdate);
                    System.out.println("Введите новое название: ");
                    String labelNameForUpdate = scanner.nextLine();
                    labelForUpdate = labelController.updateLabel(labelNameForUpdate, labelIdForUpdate);
                    System.out.println("Обновленный Label: ");
                    System.out.println(labelForUpdate);
                    break;
                case 4 :
                    System.out.println("Список всех Label: ");
                    List<Label> labels = new ArrayList<>(labelController.getAllLabels());
                    for (Label l : labels) {
                        System.out.println(l);
                    }
                    break;
                case 5 :
                    System.out.print("Введите id для удаления: ");
                    Long labelIdForDelete;
                    try {
                        labelIdForDelete = scanner.nextLong();
                    } catch (Exception e) {
                        System.out.println("Неверный ввод");
                        break;
                    }
                    scanner.nextLine();
                    System.out.println("Вы уверенны?(Y/N):");
                    String confirmation = scanner.nextLine();
                    if(confirmation.equalsIgnoreCase("y")) {
                        Label deletedLabel = labelController.deleteLabel(labelIdForDelete);
                        System.out.println("Удаленный Label:");
                        System.out.println(deletedLabel);
                    } else if (confirmation.equalsIgnoreCase("n")) {
                        System.out.println("Отмена");
                        break;
                    } else {
                        System.out.println("Неверный ввод!");
                        break;
                    }
                    break;
                case 6 :
                    flag = false;
                    break;
            }
        }
    }
}
