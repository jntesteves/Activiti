/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.cmd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.activiti.engine.cfg.S3Utils;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;


/**
 * @author Tom Baeyens
 * @author Denis Giovan Marques
 */
public class GetAttachmentContentCmd implements Command<InputStream>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String attachmentId;
  
  public GetAttachmentContentCmd(String attachmentId) {
    this.attachmentId = attachmentId;
  }

  public InputStream execute(CommandContext commandContext) {
    DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
    AttachmentEntity attachment = dbSqlSession.selectById(AttachmentEntity.class, attachmentId);
    
    String contentId = attachment.getContentId();

    if (contentId==null) {
      if (!commandContext.getProcessEngineConfiguration().isS3AsAttachmentStorage()) {
        return null;
      }

      // Try to get object content from S3 Object Storage
      S3Object object = commandContext.getProcessEngineConfiguration().getS3Client().getObject(commandContext.getProcessEngineConfiguration().getS3Bucket(), S3Utils.generateAttachmentKey(attachment));
      byte[] byteArray;

      try {
        byteArray= IOUtils.toByteArray(object.getObjectContent());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      return new ByteArrayInputStream(byteArray);
    }
    
    ByteArrayEntity byteArray = dbSqlSession.selectById(ByteArrayEntity.class, contentId);
    byte[] bytes = byteArray.getBytes();
    
    return new ByteArrayInputStream(bytes);
  }

}
