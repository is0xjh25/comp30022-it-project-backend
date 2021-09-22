package tech.crm.crmserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.service.MailService;

/**
 * <p>
 *  serviceImpl for mail sending server
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-22
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String from;


    /**
     * send text email
     * @param to send email to
     * @param subject title of the email
     * @param content content of email
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content, String... cc) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setCc(cc);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

}
