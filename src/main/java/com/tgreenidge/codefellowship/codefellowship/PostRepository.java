package com.tgreenidge.codefellowship.codefellowship;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
    Post findById(long id);
}