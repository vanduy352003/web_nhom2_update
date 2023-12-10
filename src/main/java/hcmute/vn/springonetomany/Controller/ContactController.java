package hcmute.vn.springonetomany.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hcmute.vn.springonetomany.Service.EmailService;


@Controller
@RequestMapping("/contact")
public class ContactController {
	@Autowired
	private EmailService emailService;
	@GetMapping("")
	public String showContactPage() {
		
		return "contact/contact";
	}
	
	@PostMapping("/send") 
	public String submitContact(@RequestParam("fullname") String fullname,
			@RequestParam("email") String email,
			@RequestParam("subject") String subject,
			@RequestParam("message") String content,
			@RequestParam("attachment") MultipartFile multipartfile) throws MessagingException, UnsupportedEncodingException {
		
		emailService.sendContactMail(fullname, email, subject, content, multipartfile);
		
		
		return "redirect:/contact?status=success";
	}
}
