package com.acciona;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.acciona.dto.Tweet;
import com.acciona.service.TweetService;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TweetListener implements StatusListener {

	ApplicationContext applicationContext = null;
	
	public static final int MAX_FOLLOWERS = 1500;
	public static final List<String> AVAILABLE_LANGUAGES = Arrays.asList("es", "fr", "it");
	
	
	public TweetListener(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void onException(Exception ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatus(Status status) {
		if(status.getUser().getFollowersCount() > MAX_FOLLOWERS && AVAILABLE_LANGUAGES.contains(status.getLang())) {
			Tweet tweet = new Tweet(status.getId(), status.getUser().getName(), status.getText(), status.getUser().getLocation());
    		applicationContext.getBean(TweetService.class).save(tweet);
    	}
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStallWarning(StallWarning warning) {
		// TODO Auto-generated method stub

	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	

}
