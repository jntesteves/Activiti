update ACT_GE_PROPERTY set VALUE_ = '5.16.4-ICO.1' where NAME_ = 'schema.version';

-- Update existing instances of attachments with the task's START_TIME_ + 1 second

UPDATE
    ACT_HI_ATTACHMENT AS A
    INNER JOIN ACT_HI_TASKINST AS TI ON TI.ID_ = A.TASK_ID_
SET
    A.TIME_ = DATE_ADD(TI.START_TIME_, INTERVAL 1 SECOND)