package com.turbointernational.metadata.security;

import org.apache.commons.lang3.ObjectUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author jrodriguez
 */
@Component("passwordEncoder")
public class BCryptPasswordEncoder extends PlaintextPasswordEncoder {
    
    public String genSalt() {
        return BCrypt.gensalt();
    }

    @Override
    public String encodePassword(String rawPass, Object salt) {
        return BCrypt.hashpw(rawPass, ObjectUtils.toString(salt, BCrypt.gensalt()));
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String hashed = BCrypt.hashpw(rawPass, ObjectUtils.toString(salt, BCrypt.gensalt()));

        return BCrypt.checkpw(encPass, hashed);
    }
    
}
