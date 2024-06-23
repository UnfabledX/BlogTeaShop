package com.leka.blogteashop.repository;

import com.leka.blogteashop.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "post_entity-graph")
    Page<Post> findAll(Pageable pageable);
}
