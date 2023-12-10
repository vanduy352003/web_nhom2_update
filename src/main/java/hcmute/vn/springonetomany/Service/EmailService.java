package hcmute.vn.springonetomany.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import hcmute.vn.springonetomany.Entities.User;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	public void sendContactMail(String fullname, String email, String subject, String content, MultipartFile multipartfile) throws MessagingException, UnsupportedEncodingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		
		String mailSubject = fullname + " has sent a message";
		String mailContent = "<p><b>Sender Name: </b>" + fullname + "</p>";
		mailContent += "<p><b>Sender Email: </b>" + email + "</p>";
		mailContent += "<p><b>Subject Email: </b>" + subject + "</p>";
		mailContent += "<p><b>Content Email: </b>" + content + "</p>";
		mailContent += "<hr><img src='cid:logoImage'/>";

		helper.setFrom("ltweb2023nhom2@gmail.com", "Shop contact");
		helper.setTo("ltweb2023nhom2@gmail.com");
		helper.setSubject(mailSubject);
		helper.setText(mailContent, true);
		
		//Chèn logo shop vào cuối mail
		ClassPathResource resource = new ClassPathResource("/static/img/user.jpg");
		helper.addInline("logoImage", resource);
		
		if (!multipartfile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartfile.getOriginalFilename());
			InputStreamSource source = new InputStreamSource() {
				@Override
				public InputStream getInputStream() throws IOException  {
					return multipartfile.getInputStream();
				}
			};
			
			helper.addAttachment(fileName, source);
		}
		
		mailSender.send(message);
	}
	
	public void sendRegisterMail(User user) throws MessagingException, UnsupportedEncodingException {
		
		
		  MimeMessage message = mailSender.createMimeMessage(); 
		  MimeMessageHelper helper = new MimeMessageHelper(message, true);
		  
		  String mailSubject = "EShopper kính chào quý khách " + user.getFullName(); 
		  String mailContent = "<p>Xin chào bạn,</p>"; 
		  mailContent += "<p>Chúc mừng bạn đã đăng ký tài khoản thành công trên của hàng của chúng tôi</p>"; 
		  mailContent += "<p>Chúng tôi hy vọng sẽ đáp ứng được các nhu cầu của bạn trong tương lai</p>"; 
		  mailContent += "<p>Trân trọng,</p>"; 
		  mailContent += "<p>EShopper.</p>"; 

		  mailContent += "<hr><img width='100px' src='cid:logoImage'/>";
		  
		  helper.setFrom("ltweb2023nhom2@gmail.com", "Shop contact");
		  helper.setTo(user.getEmail()); 
		  helper.setSubject(mailSubject);
		  helper.setText(mailContent, true);
		  
		  //Chèn logo shop vào cuối mail 
		  ClassPathResource resource = new ClassPathResource("/static/img/EShopper.png"); 
		  helper.addInline("logoImage", resource);
		  mailSender.send(message);
		 
	}
	
//	public void sendOrderMail(Orders order) throws MessagingException, UnsupportedEncodingException {
//		
//		
//		  MimeMessage message = mailSender.createMimeMessage(); 
//		  MimeMessageHelper helper = new MimeMessageHelper(message, true);
//		  
//		  String mailSubject = "Đơn hàng hoàn thành"; 
//		  String mailContent = "<p>Cảm ơn bạn đã đặt tại cửa hàng của chúng tôi.</p>"; 
//		  mailContent += "<p>Trân trọng,</p>"; 
//		  mailContent += "<p>EShopper.</p>"; 
//		  mailContent += "<hr><img width='100px' src='cid:logoImage'/>";
//		  
//		  helper.setFrom("ltweb2023nhom2@gmail.com", "Shop contact");
//		  helper.setTo(user.getEmail()); 
//		  helper.setSubject(mailSubject);
//		  helper.setText(mailContent, true);
//		  
//		  //Chèn logo shop vào cuối mail 
//		  ClassPathResource resource = new ClassPathResource("/static/img/EShopper.png"); 
//		  helper.addInline("logoImage", resource);
//		  mailSender.send(message);
//		 
//	}
}
