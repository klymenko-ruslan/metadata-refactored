package com.turbointernational.metadata;

import java.io.Serializable;
import java.lang.reflect.Method;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 *
 * @author jrodriguez
 */
public class MyInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        for (Method method : entity.getClass().getDeclaredMethods()) {
            if (method.getParameterTypes().length == 0
                && method.isAnnotationPresent(PrePersist.class)
                || method.isAnnotationPresent(PreUpdate.class)) {
                
                try {
                    method.invoke(entity);
                } catch (Exception e) {
                    throw new CallbackException(e);
                }
            }
        }
        
        return false;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        for (Method method : entity.getClass().getDeclaredMethods()) {
            if (method.getParameterTypes().length == 0
                && method.isAnnotationPresent(PreRemove.class)) {
                
                try {
                    method.invoke(entity);
                } catch (Exception e) {
                    throw new CallbackException(e);
                }
            }
        }
    }
    
}
