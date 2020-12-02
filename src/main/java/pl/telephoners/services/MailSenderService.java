package pl.telephoners.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.telephoners.models.MailContact;
import pl.telephoners.repositories.MailContactRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
        if(!mailContactRepository.findFirstByMailAddress(mailContact.getMailAddress()).isPresent()) return mailContactRepository.save(mailContact);
        return null;
    }

    public boolean deleteSubscriber(long id) {
        if(mailContactRepository.findById(id).isPresent()){
            mailContactRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public MailContact findSubscriberMail(String name) {
        Optional<MailContact> mailContact = mailContactRepository.findFirstByMailAddress(name);
        if(mailContact.isPresent()){
            return mailContact.get();
        }
        return null;
    }

    public MailContact findSubscriberMailById(long id) {
        Optional<MailContact> mailContact = mailContactRepository.findById(id);
        if(mailContact.isPresent()){
            return mailContact.get();
        }
        return null;
    }

    public List<MailContact> getAllSubscriber() {
        List<MailContact> mailContacts = mailContactRepository.findAll();
        if(mailContacts.isEmpty()){
            return null;
        }
        return mailContacts;

    }
}


