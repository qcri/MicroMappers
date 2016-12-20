package org.qcri.micromappers.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	MailSender mailSender;
	
	@Autowired
	AccountService accountService;
	
	private static Logger logger = Logger.getLogger(EmailService.class.getName());
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();
	
	public void sendErrorMail(String subject, String body)
	{    
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		subject = configProperties.getProperty(MicromappersConfigurationProperty.SERVER_NAME) + " : " + subject;
		simpleMailMessage.setSubject(subject);
		
		String text = (new Date()) + "\n\n" + body;
		simpleMailMessage.setText(text);
		
		List<Account> recipientAccounts = accountService.findMailEnabled();
		String[] recipients = recipientAccounts.stream().map(p -> p.getUserName()).toArray(String[]::new);
		
		simpleMailMessage.setTo(recipients);
		
		try {
			mailSender.send(simpleMailMessage);
		} catch (MailException e) {
			logger.error("Exception while sending mail", e);
		}
	}
}
