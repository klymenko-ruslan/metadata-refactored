package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.domain.security.UserDao;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/security")
public class SecurityController {
    
    private static final Logger log = Logger.getLogger(SecurityController.class.toString());
    
    @Autowired(required=true)
    UserDao userDao;
    
    @Autowired(required=true)
    MailSender mailer;
    
    @Value("${email.metadata.url}")
    String metadataUrl;
    
    @Value("${email.metadata.from}")
    String metadataFrom;
    
    @RequestMapping("unauthorized")
    public void unauthorized(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
    
    @Transactional
    @RequestMapping("password/reset/token/{token}")
    public @ResponseBody void token(@PathVariable("token") String token, @RequestParam String password) {
        User user = userDao.findByPasswordResetToken(token);
        
        if (StringUtils.isNotBlank(password)) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }
        
        user.setPasswordResetToken(null);
        
        userDao.merge(user);
    }
    
    @Transactional
    @RequestMapping(value="password/reset/request", method=RequestMethod.POST)
    public @ResponseBody void request(@RequestParam String email) {
        
        // Generate a UUID
        UUID uuid = UUID.randomUUID();
        // Add a field to the User object of the String type called "passwordResetToken" and save the UUID as a string in it.
        // Get a user with:
        User user = userDao.findUserByEmail(email);
        
        user.setPasswordResetToken(uuid.toString());
        
        // Save the user
        userDao.merge(user);
        
        // Send the user an email with the reset link
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(metadataFrom);
        message.setSubject("TI Metadata Password Reset Link");
        message.setText(metadataUrl + user.getPasswordResetToken());
        
        mailer.send(message);
    }
}
