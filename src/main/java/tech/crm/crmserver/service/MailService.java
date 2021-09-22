package tech.crm.crmserver.service;

/**
 * <p>
 *  Mail sending service <br>
 *  Modified form https://blog.csdn.net/zyw_java/article/details/81635375
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-22
 */
public interface MailService {

    /**
     * send text email
     * @param to send email to
     * @param subject title of the email
     * @param content content of email
     */
    public void sendSimpleMail(String to, String subject, String content);

    public void sendSimpleMail(String to, String subject, String content, String... cc);
}
