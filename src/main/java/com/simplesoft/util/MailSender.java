package com.simplesoft.util;

import java.util.Map;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class MailSender {
	
	private static final Logger logger =  LoggerFactory.getLogger(MailSender.class);
	
	private static String FROM = "kscjje@naver.com";
	private static String FROMNAME  = "BSON";
	private static String SMTP_USERNAME = "kscjje@naver.com";
	private static String SMTP_PASSWORD="2BY1ZJNDU36R";
	private static String HOST = "smtp.naver.com";
	private static String PORT = "587";
	
	/**
	* 메일 발송 처리	
	* @methodName : send
	* @author     : JUN
	* @date       : 2021.06.24
	*
	* @param map to - 받는사람
	* 		     subject - 제목
	*            body    - 메일 내용 text/html
	* @throws Exception
	 */
	public static void send(HttpServletRequest request, Map<String,String> map) throws Exception {
		Properties props = System.getProperties();
		
		// Create a Properties object to contain connection configuration information.
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.host", HOST);
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.ssl.protocols","TLSv1.2");
        props.put("mail.smtp.ssl.trust", HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
    	
    	request.setAttribute("emailFrom", FROM);
    	
        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

    	// Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        
    	//멀티메일여부 확인
    	if (map.get("to").toString().contains(";")) {
    		
    		String[] arrEmail = map.get("to").split(";");
    		
    		if (arrEmail.length > 0) {
    			InternetAddress[] toAddr = new InternetAddress[arrEmail.length];
    			
    			for (int i=0; i<arrEmail.length; i++) {
					toAddr[i] = new InternetAddress (arrEmail[i]);
				}
    			
    			msg.setRecipients(Message.RecipientType.TO, toAddr);		    			
    		}
    	} else {
    		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(map.get("to")));	
    	}
        
        msg.setSubject(map.get("subject"),"UTF-8");
        msg.setContent(map.get("body"),"text/html;charset=UTF-8");
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
        //msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
    	
        Transport transport = session.getTransport();
        
	    try {	        
	        
	        System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
	        //Transport.send(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
			
            request.setAttribute("emailResult", "0");
            request.setAttribute("emailResultCd", GlobalVariable.LOG_RESULT_CODE_SUCCESS);
            
		} catch(HttpStatusCodeException e) {
			request.setAttribute("emailResult", "1");
			request.setAttribute("emailResultCd", GlobalVariable.LOG_RESULT_CODE_MAIL_SERVER_ERROR);
            logger.info(e.toString());
		} catch(AuthenticationFailedException e) {
//			e.printStackTrace();
			request.setAttribute("emailResult", "1");
			request.setAttribute("emailResultCd", GlobalVariable.LOG_RESULT_CODE_MAIL_SERVER_AUTH_FAIL);
			logger.info(e.toString());
		} catch(AddressException e) {
//			e.printStackTrace();
			request.setAttribute("emailResult", "1");
			request.setAttribute("emailResultCd", GlobalVariable.LOG_RESULT_CODE_MAIL_TO_ADDRESS_ERROR);
			logger.info(e.toString());
		} catch(MessagingException e) {
//			e.printStackTrace();
			request.setAttribute("emailResult", "1");
			request.setAttribute("emailResultCd", GlobalVariable.LOG_RESULT_CODE_MAIL_SERVER_NOTFOUND);
			logger.info(e.toString());
		} catch(Exception e) {
//			e.printStackTrace();
			request.setAttribute("emailResult", "1");
			request.setAttribute("emailResultCd", GlobalVariable.LOG_RESULT_CODE_MAIL_SERVER_ERROR);
			logger.info(e.toString());
		} 
		finally
        {
            // Close and terminate the connection.   
			transport.close();
        }
	}	
	
	public static boolean send(Map<String,String> map) throws Exception {
		
		boolean isSend = true;
		
		Properties props = System.getProperties();
		
		// Create a Properties object to contain connection configuration information.
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", HOST);
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.debug", "true");
    	
        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

    	// Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        
    	//멀티메일여부 확인
    	if (map.get("to").toString().contains(";")) {
    		
    		String[] arrEmail = map.get("to").split(";");
    		
    		if (arrEmail.length > 0) {
    			InternetAddress[] toAddr = new InternetAddress[arrEmail.length];
    			
    			for (int i=0; i<arrEmail.length; i++) {
					toAddr[i] = new InternetAddress (arrEmail[i]);
				}
    			
    			msg.setRecipients(Message.RecipientType.TO, toAddr);		    			
    		}
    	} else {
    		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(map.get("to")));	
    	}
        
        msg.setSubject(map.get("subject"),"UTF-8");
        msg.setContent(map.get("body"),"text/html;charset=UTF-8");
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
        //msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
    	
        Transport transport = session.getTransport();
        
	    try {	        
	        
	        //System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
	        
            //System.out.println("Email sent!");
            
		} catch(HttpStatusCodeException e) {
            logger.info(e.toString());
            isSend = false;
		} catch(AuthenticationFailedException e) {
			logger.info(e.toString());
			isSend = false;
		} catch(AddressException e) {
			logger.info(e.toString());
			isSend = false;
		} catch(MessagingException e) {
			logger.info(e.toString());
			isSend = false;
		} catch(Exception e) {
			logger.info(e.toString());
			isSend = false;
		} 
		finally
        {
            // Close and terminate the connection.   
			transport.close();
        }
	    
	    return isSend;
	}	
}
