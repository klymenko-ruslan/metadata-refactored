package com.turbointernational.metadata.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

/**
 *
 * @author jrodriguez
 */
public class BCryptPasswordEncoder extends MessageDigestPasswordEncoder {
    public BCryptPasswordEncoder() {
        super("BCrypt");
    }

    @Override
    public String encodePassword(String rawPass, Object salt) {
        return BCrypt.hashpw(rawPass, salt.toString());
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String hashed = BCrypt.hashpw(rawPass, salt.toString());

        return BCrypt.checkpw(encPass, hashed);
    }
    
}
