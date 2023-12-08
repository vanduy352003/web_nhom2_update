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

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	public void sendMail(String fullname, String email, String subject, String content, MultipartFile multipartfile) throws MessagingException, UnsupportedEncodingException {
		
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
}
