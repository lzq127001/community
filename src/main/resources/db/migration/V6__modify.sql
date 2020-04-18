alter table QUESTION drop column AVATAR_URI;
alter table USER
	add avatar_url varchar(100);