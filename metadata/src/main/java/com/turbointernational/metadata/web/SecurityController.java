package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.security.User;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/security")
public class SecurityController {
    
    private static final Logger log = Logger.getLogger(SecurityController.class.toString());
    
    @Autowired(required=true)
    MailSender mailer;
    
    @Value("${email.metadata.url}")
    String metadataUrl;
    
    @Value("${email.metadata.from}")
    String metadataFrom;
    @Transactional
    @RequestMapping("password/reset/token/{token}")
    public @ResponseBody void token(@PathVariable("token") String token, @RequestParam String password) {
        User user = User.findByPasswordResetToken(token);
        
        if (StringUtils.isNotBlank(password)) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }
        
        user.setPasswordResetToken(null);
        
        user.merge();
    }
    
    @Transactional
    @RequestMapping("password/reset/request")
    public @ResponseBody void request(@RequestParam String email) {
        
        // Generate a UUID
        UUID uuid = UUID.randomUUID();
        // Add a field to the User object of the String type called "passwordResetToken" and save the UUID as a string in it.
        // Get a user with:
        User user = User.findUserByEmail(email);
        
        user.setPasswordResetToken(uuid.toString());
        
        // Save the user
        user.merge();
        
        // Send the user an email with the reset link
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(metadataFrom);
        message.setSubject("TI Metadata Password Reset Link");
        message.setText(metadataUrl + user.getPasswordResetToken());
        
        mailer.send(message);
    }
    
    @RequestMapping("denied")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody void denied() {
    }
    
    @RequestMapping("login/success")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody void loginSuccess() {
    }
    
    @RequestMapping("login/failed")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody String loginFailed() {
        return "\"Login failed\"";
    }
    
    @RequestMapping("login/required")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody void loginRequired() {
    }
}
