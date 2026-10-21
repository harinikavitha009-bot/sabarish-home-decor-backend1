package home_decor.controller;

import home_decor.model.Contact;
import home_decor.repository.ContactRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactRepository contactRepository;
    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String senderEmail;

    @Value("${app.mail.to}")
    private String receiverEmail;

    public ContactController(
            ContactRepository contactRepository,
            JavaMailSender mailSender) {
        this.contactRepository = contactRepository;
        this.mailSender = mailSender;
    }

    @PostMapping
    public Contact saveContact(@RequestBody Contact contact) {

        Contact savedContact = contactRepository.save(contact);

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(senderEmail);
            mail.setTo(receiverEmail);
            mail.setSubject("New Website Enquiry - Sabarish Home Decor");

            mail.setText(
                "New enquiry received!\n\n" +
                "Name: " + contact.getName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Phone: " + contact.getPhone() + "\n" +
                "Service: " + contact.getService() + "\n" +
                "Message: " + contact.getMessage()
            );

            mailSender.send(mail);
            System.out.println("Enquiry notification email sent.");

        } catch (Exception e) {
            System.err.println(
                "Email notification failed: " + e.getMessage()
            );
        }

        return savedContact;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable Long id) {

        if (!contactRepository.existsById(id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Enquiry not found"
            );
        }

        contactRepository.deleteById(id);
        return "Enquiry deleted successfully";
    }
}