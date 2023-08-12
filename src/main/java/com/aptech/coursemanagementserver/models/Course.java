package com.aptech.coursemanagementserver.models;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(indexes = {
                @Index(name = "IDX_Course_Name", columnList = "name DESC"),
                @Index(name = "IDX_Course_Price", columnList = "price ASC"),
                @Index(name = "IDX_Course_CreatedAt", columnList = "created_at DESC"),
})

public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(columnDefinition = "bigint")
        private long id;
        @Column(columnDefinition = "nvarchar(100)")
        @EqualsAndHashCode.Include
        private String name;
        @Column(columnDefinition = "nvarchar(MAX)")
        private String description;
        private String slug;
        private String image;
        @Column(columnDefinition = "decimal(10,2)")
        private double price;
        @Column(columnDefinition = "decimal(10,2)")
        private double net_price;
        @Column(columnDefinition = "tinyint")
        private int level = 0;
        @Column(columnDefinition = "tinyint")
        private int status = 0;
        private int duration = 0;
        @Column(columnDefinition = "decimal(3,2)")
        private double rating = 0;
        @Column(columnDefinition = "datetime")
        private Date published_at;
        // @Column(columnDefinition = "bigint")
        // private long category_id;
        @Column(columnDefinition = "nvarchar(100)")
        private String requirement;

        @Column(columnDefinition = "nvarchar(100)")
        private String updatedBy;

        @CreationTimestamp
        private Instant created_at = Instant.now();
        @UpdateTimestamp
        private Instant updated_at;

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
        private Set<Section> sections = new HashSet<>();

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
        private Set<Enrollment> enrollments = new HashSet<>();

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
        private Set<Orders> orders = new HashSet<>();

        @ManyToOne(cascade = CascadeType.MERGE)
        @JoinColumn(name = "author_id", nullable = true, foreignKey = @ForeignKey(name = "FK_Course_Author"))
        private Author author;

        @ManyToOne(cascade = CascadeType.MERGE)
        @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Course_Category"))
        private Category category;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "CourseTag", joinColumns = { @JoinColumn(name = "course_id") }, inverseJoinColumns = {
                        @JoinColumn(name = "tag_id") })
        private Set<Tag> tags = new HashSet<>();

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "CourseAchievement", joinColumns = { @JoinColumn(name = "course_id") }, inverseJoinColumns = {
                        @JoinColumn(name = "achievement_id") })
        private Set<Achievement> achievements = new HashSet<>();

}
