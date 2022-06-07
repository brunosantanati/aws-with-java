package com.dynamodb.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.dynamodb.dto.Movie;

@EnableScan
public interface MovieRepository extends CrudRepository<Movie, String> {

}
