package com.dotcom.subscription;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private final String login = "20fb7ec2de91";
	private final String password = "d190866359a69eaa";
	//private final String endpoint = "http://localhost:8080/callback";
	private final String endpoint = "http://dfe21041.ngrok.io/subscription/callback";
	private final String api = "https://www.callfire.com/api/1.1/rest/subscription.json";
	
								
	@PostConstruct
	public void createEndpointSubscription() throws IOException {
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(login, password));
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

		JSONObject json = new JSONObject();
		//json.put("Enabled", true);
		json.put("Endpoint", endpoint);
		json.put("NotificationFormat", "JSON");
		json.put("TriggerEvent", "OUTBOUND_TEXT_FINISHED");
		json.put("ToNumber", "14243876936");
		StringEntity params = new StringEntity(json.toString());
		try {
		HttpPost post = new HttpPost(api);
		post.addHeader("content-type", "application/json");
		
		post.setEntity(params);
		HttpResponse response;
		response = httpClient.execute(post);

		List<Header> httpHeaders = Arrays.asList(post.getAllHeaders());        
	    for (Header header : httpHeaders) {
	        System.out.println("Headers.. name,value:"+header.getName() + "," + header.getValue());
	    }

		logger.info(EntityUtils.toString(post.getEntity()));
		logger.info(EntityUtils.toString(response.getEntity()));

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("subscription failed");
			}finally {
				httpClient.close();}
	}

	 
	
	@RequestMapping(value = "/callback",method= RequestMethod.POST , consumes="application/json")
	public ResponseEntity<String> showResult(@RequestBody String body) {

		System.out.println(body.toString());
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/")
	public String home() {
		return "home";
	}

	
}
