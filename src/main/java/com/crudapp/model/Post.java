package com.crudapp.model;

import com.crudapp.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Column(name = "created")
    private Timestamp created;

    @Column(name = "updated")
    private Timestamp updated;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Writer writer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_labels",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<Label> labels;

    @Override
    public String toString() {
        String writerName = writer != null ? writer.getFirstName() : "null";
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", postStatus=" + postStatus +
                ", created=" + created +
                ", updated=" + updated +
                ", writer=" + writerName +
                ", labels=" + labels +
                '}';
    }

}
