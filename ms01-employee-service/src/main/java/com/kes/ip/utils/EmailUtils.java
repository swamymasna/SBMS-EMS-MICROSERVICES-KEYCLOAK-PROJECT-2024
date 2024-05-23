package com.kes.ip.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class EmailUtils {

	private JavaMailSender javaMailSender;

	public String sendEmail(String subject, String toAddr, String body) {

		String message = null;

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

			messageHelper.setSubject(subject);
			messageHelper.setTo(toAddr);
			messageHelper.setText(body);

			javaMailSender.send(messageHelper.getMimeMessage());

			log.info("Email Sent Successfully");

			message = "EMAIL SENT";

		} catch (Exception e) {
			log.error("Exception Occured While Sending Email..??");
			e.printStackTrace();
		}

		return message;
	}
}
