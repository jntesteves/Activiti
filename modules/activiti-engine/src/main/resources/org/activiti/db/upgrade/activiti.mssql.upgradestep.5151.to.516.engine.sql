alter table ACT_RU_TASK
	add FORM_KEY_ nvarchar(255);
	
alter table ACT_RU_EXECUTION
	add NAME_ nvarchar(255);
	
	
update ACT_GE_PROPERTY set VALUE_ = '5.15.2' where NAME_ = 'schema.version';
