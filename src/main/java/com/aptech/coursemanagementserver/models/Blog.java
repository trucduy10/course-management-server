package com.aptech.coursemanagementserver.models;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;
    @Column(columnDefinition = "varchar(100)")
    private String name;
    @Column(unique = true, columnDefinition = "varchar(255)")
    private String slug;
    @Column(columnDefinition = "tinyint default 2") // Always default value if end-user not input = 2 (PROCESSING)
    private int status;
    @Column(columnDefinition = "varchar(MAX)")
    private String description;
    @Column(columnDefinition = "int")
    private int view_count;

    @Column(columnDefinition = "datetime")
    private LocalDateTime created_at;
    @Column(columnDefinition = "datetime")
    private LocalDateTime updated_at;

    @Column(columnDefinition = "nvarchar(100)")
    private String updatedBy;

    // Modified - START
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Blog_User"))
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Blog_Category"))
    private Category category;
    // Modified - E N D
}
