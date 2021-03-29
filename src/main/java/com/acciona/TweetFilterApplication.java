package com.acciona;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@SpringBootApplication
public class TweetFilterApplication {

	public static void main(String[] args) throws TwitterException, IOException{
		SpringApplication springApplication=new SpringApplication(TweetFilterApplication.class);
		ApplicationContext applicationContext = springApplication.run(args);
		
		TweetListener listener = new TweetListener(applicationContext);
	
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    twitterStream.addListener(listener);
	    twitterStream.sample();
	}

}
