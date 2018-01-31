package com.turbointernational.metadata.util;

/**
 * Created by dmytro.trunykov@zorallabs.com on 31.01.18.
 */
public class SerializationUtils {

    /**
     * Get JSON object as string which contains info about entity before and after update.
     * 
     * The application tracks changes of entities. Changes are tracked in JSON form.
     * This method represents an entity before and after change.
     * 
     * @param jsonOriginal
     * @param jsonUpdated
     * @return
     */
    public static String update(String jsonOriginal, String jsonUpdated) {
        return "{original: " + jsonOriginal + ",updated: " + jsonUpdated + "}";
    }

}
