package com.acciona.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acciona.dao.TweetRepository;
import com.acciona.dto.Tweet;

@Service
public class TweetService {

	@Autowired
	TweetRepository tweetRepository;
	
	public Tweet save(Tweet tweet) {
		return tweetRepository.save(tweet);
	}
	
	public List<Tweet> getTweets(){
		return (List<Tweet>) tweetRepository.findAll();
	}
	
	public Optional<Tweet> getTweetById(long id){
		return tweetRepository.findById(id);
	}
	
	public Tweet markAsValid(long id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		tweet.get().setValido(true);
		return tweetRepository.save(tweet.get());
	}
	
	public List<Tweet> getValidTweetsByUser(String user){
		List<Tweet> completeList = (List<Tweet>) tweetRepository.findAll();
		return completeList.stream().filter(tweet -> tweet.isValido() && tweet.getUser().equals(user)).collect(Collectors.toList());	
	}
}