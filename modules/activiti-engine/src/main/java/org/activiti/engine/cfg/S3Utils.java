package org.activiti.engine.cfg;

import org.activiti.engine.task.Attachment;

/**
 *
 * This class is used to create a S3 Object Storage Client
 *
 * @author Denis Giovan Marques
 *
 */
public class S3Utils {

    public static String generateAttachmentKey(Attachment attachment) {
        StringBuilder sb = new StringBuilder();
        sb.append(attachment.getProcessInstanceId());
        sb.append("/");

        if (attachment.getId().contains(":")) {
            sb.append(attachment.getId().split(":")[1]);
        } else {
            sb.append(attachment.getTaskId());
        }
        sb.append("/");
        sb.append(attachment.getName());

        return sb.toString();
    }

}
