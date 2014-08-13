alter table ACT_RU_TASK
	alter column FORM_KEY_ nvarchar(2000)
	
	update ACT_GE_PROPERTY set VALUE_ = '5.16.4-ICO' where NAME_ = 'schema.version';