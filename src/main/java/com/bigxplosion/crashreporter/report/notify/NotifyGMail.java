package com.bigxplosion.crashreporter.report.notify;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import com.bigxplosion.crashreporter.config.Config;
import com.bigxplosion.crashreporter.report.base.INotificationProvider;

public class NotifyGMail implements INotificationProvider {

	@Override
	public void notify(String title, String text, String link) throws NotifyException {
		if (!Config.gmailEnabled)
			return;

		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Config.gmailFrom, Config.gmailPass);
			}
		});

		try {

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Config.gmailFrom));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Config.gmailTo));
			message.setSubject(title);

			String msgText = text;
			msgText += "\n\n\n\n";
			msgText += ("THIS CRASH REPORT HAS BEEN POSTED TO: " + link);

			message.setText(msgText);

			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
