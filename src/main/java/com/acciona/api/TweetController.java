package com.acciona.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acciona.Regex;
import com.acciona.dto.Tweet;
import com.acciona.service.TweetService;


@RestController
public class TweetController {

	@Autowired
	TweetService tweetService;
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public Tweet updateOrSave(@RequestBody Tweet tweet) {
		return tweetService.save(tweet);
	}
	
	@RequestMapping(value="/getTweets", method=RequestMethod.GET)
	public List<Tweet> getTweets() {
		return tweetService.getTweets();
	}
	
	@RequestMapping(value="/validate/{id}", method=RequestMethod.POST)
	public Tweet validate(@PathVariable long id) {
		return tweetService.markAsValid(id);
	}
	
	@RequestMapping(value="/getValidByUser/{user}", method=RequestMethod.POST)
	public List<Tweet> getValidTweetByUser(@PathVariable String user){
		return tweetService.getValidTweetsByUser(user);
	}
	
	@RequestMapping(value="/getHashtags", method=RequestMethod.POST)
	public List<String> getMostUsedHashtags(@RequestParam(defaultValue = "10") int number){
		List<String> completeList = tweetService.getTweets().stream().map(tweet -> tweet.getTexto()).collect(Collectors.toList());
		return filterHashTags(completeList, number);
	}

	private List<String> filterHashTags(List<String> completeList, int number) {
		
		Map<String, Integer> hashTags = new HashMap<String, Integer>();
		
		for(String tweet : completeList) {
			for( String hashtag : extractHashtags(tweet, false)){
				if(!hashTags.containsKey(hashtag)) {
					hashTags.put(hashtag, 1);
				} else {
					hashTags.put(hashtag, hashTags.get(hashtag)+1);
				}
			}
		}
		
		Map<String, Integer> sortedMap = hashTags.entrySet().stream()
		        .sorted(Comparator.comparingInt(e -> -e.getValue()))
		        .collect(Collectors.toMap(
		                Map.Entry::getKey,
		                Map.Entry::getValue,
		                (a, b) -> { throw new AssertionError(); },
		                LinkedHashMap::new
		        ));

		List<String> result = sortedMap.keySet().stream().collect(Collectors.toList());
		
		return result.size() < number ? result : result.subList(0, number);
	}
	
	public List<String> extractHashtags(final String text, final boolean exclude_duplicate) { 
		  if (text == null || text.length() == 0) return Collections.emptyList(); 
		 
		  final ArrayList<String> extracted = new ArrayList<String>(); 
		  for (final String entity : extractHashtagsWithIndices(text)) { 
		   if (!exclude_duplicate || !extracted.contains(entity)) { 
		    extracted.add(entity); 
		   } 
		  } 
		 
		  return extracted; 
		 } 
	
	 private static final char FULLWIDTH_NUMBER_SIGN = '\uff03'; 

	
	private List<String> extractHashtagsWithIndices(final String text) { 
		  if (text == null || text.length() == 0) return Collections.emptyList(); 
		 
		  // Performance optimization. 
		  // If text doesn't contain # at all, text doesn't contain 
		  // hashtag, so we can simply return an empty list. 
		  boolean found = false; 
		  for (final char c : text.toCharArray()) { 
		   if (c == '#' || c == FULLWIDTH_NUMBER_SIGN) { 
		    found = true; 
		    break; 
		   } 
		  } 
		  if (!found) return Collections.emptyList(); 
		 
		  final ArrayList<String> extracted = new ArrayList<String>(); 
		  final Matcher matcher = Regex.VALID_HASHTAG.matcher(text); 
		 
		  while (matcher.find()) { 
		   final String after = text.substring(matcher.end()); 
		   if (!Regex.INVALID_HASHTAG_MATCH_END.matcher(after).find()) { 
		    extracted.add(matcher.group(Regex.VALID_HASHTAG_GROUP_TAG)); 
		   } 
		  } 
		 
		  return extracted; 
		 } 
}
