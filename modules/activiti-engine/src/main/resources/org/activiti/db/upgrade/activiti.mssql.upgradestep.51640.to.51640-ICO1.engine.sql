update ACT_GE_PROPERTY set VALUE_ = '5.16.4-ICO.1' where NAME_ = 'schema.version';

-- Update existing instances of attachments with the task's START_TIME_ + 1 second

UPDATE
    A
SET
    A.TIME_ = DATEADD(SECOND, 1, TI.START_TIME_)
FROM
    ACT_HI_ATTACHMENT AS A
    INNER JOIN ACT_HI_TASKINST AS TI ON TI.ID_ = A.TASK_ID_
