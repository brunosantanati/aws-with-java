package com.dynamodb.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dynamodb.dto.Movie;

@Repository
@EnableScan
public interface MovieRepository extends CrudRepository<Movie, String> {

}
