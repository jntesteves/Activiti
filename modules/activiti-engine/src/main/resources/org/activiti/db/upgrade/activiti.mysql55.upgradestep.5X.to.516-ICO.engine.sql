alter table ACT_RU_TASK
	modify TASK_FORM_KEY_ varchar(2000);
	
	update ACT_GE_PROPERTY set VALUE_ = '5.16-ICO' where NAME_ = 'schema.version';