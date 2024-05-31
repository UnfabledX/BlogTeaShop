package com.leka.blogteashop.model;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Builder
@Setter
@Getter
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;



}
