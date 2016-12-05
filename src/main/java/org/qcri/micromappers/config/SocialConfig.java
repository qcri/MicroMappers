package org.qcri.micromappers.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.qcri.micromappers.controller.FacebookConnectInterceptor;
import org.qcri.micromappers.controller.TwitterConnectInterceptor;
import org.qcri.micromappers.social.CustomConnectController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

//import qa.qcri.mm.controller.FacebookConnectInterceptor;
//import qa.qcri.mm.controller.TwitterConnectInterceptor;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter
{
	@Value("${facebook.appKey}")
	private String facebookAppKey;
	@Value("${facebook.appSecret}")
	private String facebookAppSecret;
	@Value("${twitter.appKey}")
	private String twitterAppKey;
	@Value("${twitter.appSecret}")
	private String twitterAppSecret;
	@Value("${google.appKey}")
	private String googleAppKey;
	@Value("${google.appSecret}")
	private String googleAppSecret;
	
	@Value("${application.url}")
	private String applicationURL;
	
	@Inject
	private DataSource dataSource;
	@Autowired
	UserConnectionSignUp userConnectionSignUp;
  
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env)
	{
		cfConfig.addConnectionFactory(facebookConnectionFactory());
		cfConfig.addConnectionFactory(twitterConnectionFactory());
		cfConfig.addConnectionFactory(googleConnectionFactory());
	}
  
	@Override
	@Bean
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {			
			@Override
			public String getUserId() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null) {
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				}
				return authentication.getName();
			}
		};
	}
  
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator)
	{
		JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		jdbcUsersConnectionRepository.setConnectionSignUp(userConnectionSignUp);
		return jdbcUsersConnectionRepository;
	}
  
	@Bean
	public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource)
	{
		return new ReconnectFilter(usersConnectionRepository, userIdSource);
	}
  
	private ConnectionFactory<Facebook> facebookConnectionFactory()
	{
		FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(facebookAppKey, facebookAppSecret);
		return facebookConnectionFactory;
	}
  
	private ConnectionFactory<Twitter> twitterConnectionFactory()
	{
		TwitterConnectionFactory twitterConnectionFactory = new TwitterConnectionFactory(twitterAppKey, twitterAppSecret);
		return twitterConnectionFactory;
	}
	
	private ConnectionFactory<Google> googleConnectionFactory() {
		GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(googleAppKey, googleAppSecret);
		return googleConnectionFactory;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repository) {
		Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Twitter twitter(ConnectionRepository repository) {
		Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
		return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	public TwitterConnectInterceptor twitterConnectInterceptor(){
		return new TwitterConnectInterceptor();
	}
	
	@Bean
	public FacebookConnectInterceptor facebookConnectInterceptor(){
		return new FacebookConnectInterceptor();
	}
	
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
		ConnectController connectController = new CustomConnectController(connectionFactoryLocator, connectionRepository);
		connectController.setApplicationUrl(applicationURL);
		connectController.addInterceptor(facebookConnectInterceptor());
		connectController.addInterceptor(twitterConnectInterceptor());
		return connectController;
	}
}
