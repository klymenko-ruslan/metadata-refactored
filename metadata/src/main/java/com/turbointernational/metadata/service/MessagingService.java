package com.turbointernational.metadata.service;

import java.io.IOException;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public interface MessagingService {

    /**
     * @param groupId This argument is mainly for debug purpose. Some operations could generate several notification
     *        messages, such messages could be logically joined in a chunk by this parameter. It could be null.
     * @param message
     * @throws IOException
     */
    void bomChanged(String groupId, byte[] message) throws IOException;

    /**
     * @param groupId This argument is mainly for debug purpose. Some operations could generate several notification
     *        messages, such messages could be logically joined in a chunk by this parameter. It could be null.
     * @param message
     * @throws IOException
     */
    void interchangeChanged(String groupId, byte[] message) throws IOException;

}
