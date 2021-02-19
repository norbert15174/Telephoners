package pl.telephoners.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.telephoners.google.GmailCredentials;
import pl.telephoners.google.GmailService;
import pl.telephoners.google.GmailServiceImpl;
import pl.telephoners.models.MailContact;
import pl.telephoners.repositories.MailContactRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@Service
public class MailSenderService {


    private MailContactRepository mailContactRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public MailSenderService(MailContactRepository mailContactRepository, JavaMailSender javaMailSender) {
        this.mailContactRepository = mailContactRepository;
        this.javaMailSender = javaMailSender;
    }

    public void sendMailByGoogleMailApi(String to,

                                        String subject,

                                        String text){

            try {
                GmailService gmailService = new GmailServiceImpl(GoogleNetHttpTransport.newTrustedTransport());
                gmailService.setGmailCredentials(GmailCredentials.builder()
                        .userEmail("faronnorbertkrk@gmail.com")
                        .clientId("31835092658-bn83gh7p9vdl2gn1bkja52sbe4snqdka.apps.googleusercontent.com")
                        .clientSecret("cjINZAfOAuS0xSThUmllGrjn")
                        .accessToken("ya29.A0AfH6SMC0zOr3SktxgXiVD2OlqcMDCMRSgs1TRVYYMFMUseMSauZLUsA1Wb6Iph4t4RxrTd0EqA9fTP5feSz")
                        .refreshToken("1//04itvqBO_nNyDCgYIARAAGAQSNwF-L9Irlex7pk3zAHl7mGSAp8MJ5fUMBGs8AKCAyknozxNtTh71fX9a4KrtuFEzRAfzEBbv4VU")
                        .build());

                gmailService.sendMessage(to, subject, text);
            } catch (GeneralSecurityException | IOException | MessagingException e) {
                e.printStackTrace();
            }
    }


    public void sendMail(String to,

                         String subject,

                         String text,

                         boolean isHtmlContent) throws MessagingException {



        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(to);

        mimeMessageHelper.setSubject(subject);

        mimeMessageHelper.setText(text, isHtmlContent);

        javaMailSender.send(mimeMessage);

    }

    public MailContact addSubscriberMail(MailContact mailContact) {
        if (!mailContactRepository.findFirstByMailAddress(mailContact.getMailAddress()).isPresent())
            return mailContactRepository.save(mailContact);
        return null;
    }

    public boolean deleteSubscriber(long id) {
        if (mailContactRepository.findById(id).isPresent()) {
            mailContactRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public MailContact findSubscriberMail(String name) {
        Optional<MailContact> mailContact = mailContactRepository.findFirstByMailAddress(name);
        if (mailContact.isPresent()) {
            return mailContact.get();
        }
        return null;
    }

    public MailContact findSubscriberMailById(long id) {
        Optional<MailContact> mailContact = mailContactRepository.findById(id);
        if (mailContact.isPresent()) {
            return mailContact.get();
        }
        return null;
    }

    public List<MailContact> getAllSubscriber() {
        List<MailContact> mailContacts = mailContactRepository.findAll();
        if (mailContacts.isEmpty()) {
            return null;
        }
        return mailContacts;

    }
}


