package org.qcri.micromappers.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.validator.UrlValidator;
import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.exception.MicromappersException;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.service.AccountService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class Util
{
	private static Logger logger = Logger.getLogger(Util.class);
	final private static long MAX_CHECK_SNOPES_TIME_MILLIS = 300000; // 5min for local.
	//final private static long MAX_CHECK_SNOPES_TIME_MILLIS = 21600000;  // 6 hours for production
	final private static long MAX_CHECK_GOOGLE_NEWS_TIME_MILLIS = 300000;
	//final private static long MAX_CHECK_TIME_MILLIS = 3600000; // 1hr for testing
	public static long timeOfLastSnopesProcessingMillis = System.currentTimeMillis(); //initialize at startup
	public static long timeOfLastGoogleNewsProcessingMillis = System.currentTimeMillis(); //initialize at startup
	private static UrlValidator urlValidator = new UrlValidator();

	@Inject
	private AccountService accountService;

	public static long getDurationInMinutes(Date currentTime, Date oldTime)
	{
		long diff = currentTime.getTime() - oldTime.getTime();
		long diffMinutes = diff / 60000L;
		return diffMinutes;
	}

	public static boolean isTimeToSnopesFetchRun(){

		long currentTimeMillis = System.currentTimeMillis();
		logger.info("currentTimeMillis : " + currentTimeMillis);
		long diff = currentTimeMillis - timeOfLastSnopesProcessingMillis;
		logger.info("currentTimeMillis differ : " + diff);
		logger.info("MAX_CHECK_TIME_MILLIS : " + MAX_CHECK_SNOPES_TIME_MILLIS);
		// every 6hours
		if ((currentTimeMillis - timeOfLastSnopesProcessingMillis) >= MAX_CHECK_SNOPES_TIME_MILLIS) {
			return true;
		}
		return false;
	}

	public static boolean isTimeToGoogleNewsFetchRun(){

		long currentTimeMillis = System.currentTimeMillis();
		logger.info("currentTimeMillis : " + currentTimeMillis);
		long diff = currentTimeMillis - timeOfLastGoogleNewsProcessingMillis;
		logger.info("currentTimeMillis differ : " + diff);
		logger.info("MAX_CHECK_TIME_MILLIS : " + MAX_CHECK_GOOGLE_NEWS_TIME_MILLIS);
		// every 6hours
		if ((currentTimeMillis - timeOfLastGoogleNewsProcessingMillis) >= MAX_CHECK_GOOGLE_NEWS_TIME_MILLIS) {
			return true;
		}
		return false;
	}



	public Account getAuthenticatedUser() throws MicromappersException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null){
			return accountService.getByUserName(authentication.getName());
		}else{
			throw new MicromappersException("No user logged in ");
		}
	}

	public String getAuthenticatedUserName() throws MicromappersException{
		try{
			Account authenticatedUser = getAuthenticatedUser();
			return authenticatedUser.getUserName();
		}catch (MicromappersException e) {
			throw new MicromappersException("No user logged in ", e);
		}
	}
	
	public Boolean writeToFile(Path path, String message) throws MicromappersServiceException{
		try (BufferedWriter writer = Files.newBufferedWriter(path)) 
		{
			writer.write(message);
			return true;
		}catch (IOException e) {
			logger.error("Exception while writing to file", e);
			throw new MicromappersServiceException("Exception while writing to file", e);
		}
	}
	
	public Boolean createDirectories(Path path) throws MicromappersServiceException{
		if(Files.notExists(path)){
			try {
				Files.createDirectories(path);
				return true;
			} catch (IOException e) {
				logger.error("Exception while creating directories to persist feed", e);
				throw new MicromappersServiceException("Exception while creating directories to persist feed", e);
			}
		}else{
			return true;
		}
	}

	public static String getFileContent(Path filePath){
		StringBuffer buffer = new StringBuffer();
		try{
			List<String> list= new ArrayList<>();

			try (BufferedReader br = Files.newBufferedReader(filePath, Charset.forName("UTF-8")))
			{
				list = br.lines().collect(Collectors.toList());

				for(String a : list){
					buffer.append(a);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e){
			logger.error(e);
		}

		return buffer.toString();
	}

	public static boolean isVidateURL(String urlAddress){
		return urlValidator.isValid(urlAddress);
	}
	
}
