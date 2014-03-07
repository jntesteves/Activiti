alter table ACT_RU_TASK 
    add CATEGORY_ NVARCHAR2(255);

update ACT_GE_PROPERTY set VALUE_ = '5.15.7-SIMULATION' where NAME_ = 'schema.version';
