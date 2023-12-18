package com.crudapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Label")
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
