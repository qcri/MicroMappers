package org.qcri.micromappers.utility;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.exception.MicromappersException;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.service.AccountService;
import org.qcri.micromappers.service.DataFeedService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Util
{
	private static Logger logger = Logger.getLogger(Util.class);
	// final private static long MAX_CHECK_TIME_MILLIS = 10800000;  // 3 hours for production
	final private static long MAX_CHECK_TIME_MILLIS = 3600000; // 1hr for testing
	private static long timeOfLastTranslationProcessingMillis = System.currentTimeMillis(); //initialize at startup


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
		// every 3hours
		if ((currentTimeMillis - timeOfLastTranslationProcessingMillis) >= MAX_CHECK_TIME_MILLIS) {
			return true;
		}
		return false;
	}

	public Account getAuthenticatedUser() throws MicromappersException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null){
			return accountService.findByUserName(authentication.getName());
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
	
}
