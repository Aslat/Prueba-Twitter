package com.acciona.dao;

import org.springframework.data.repository.CrudRepository;

import com.acciona.dto.Tweet;

public interface TweetRepository extends CrudRepository<Tweet, Long>{

}
