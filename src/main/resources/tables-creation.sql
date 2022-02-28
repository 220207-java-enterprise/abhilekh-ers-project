drop table if exists users;
drop table if exists user_roles;
drop table if exists reimbursements;
drop table if exists  reimbursement_types;
drop table if exists reimbursement_statuses;

create table users(
	id			VARCHAR,
	username	VARCHAR unique not null,
	email		VARCHAR unique not null,
	password	VARCHAR not null,
	given_name	VARCHAR not null,
	surname		VARCHAR not null,
	is_active 	BOOLEAN,
	--FK
	role_id		VARCHAR, 
	
	constraint "pk_users" primary key ("id")
);

-- fk constraint between users table and user_roles table
alter table users add constraint"fk_users_roles"
foreign key (role_id)
references user_roles;

create table user_roles(
	id 			VARCHAR,
	role 		VARCHAR unique,
	
	constraint "pk_user_roles" primary key ("id")
);

create table reimbursements(
	id 			VARCHAR,
	amount		NUMERIC(6,2) not null,
	submitted	TIMESTAMP not null,
	resolved	TIMESTAMP,
	description	VARCHAR not null,
	receipt		BYTEA,
	payment_id	VARCHAR,
	--FKs
	author_id	VARCHAR not null,
	resolver_id	VARCHAR,
	status_id 	VARCHAR not null,
	type_id		VARCHAR not null,
	
	constraint "pk_reimbursements" primary key ("id")
);

-- fk between reimbursements table and users table 
alter table reimbursements add constraint "fk_reimbursements_author"
foreign key (author_id)
references users;

-- fk between reimbursements table and users table 
alter table reimbursements add constraint "fk_reimbursements_resolver"
foreign key (resolver_id)
references users;

-- fk between reimbursements table and reimbursement_statuses table 
alter table reimbursements add constraint "fk_reimbursements_status"
foreign key (status_id)
references reimbursement_statuses; 

-- fk between reimbursements table and reimbursement_types table 
alter table reimbursements add constraint "fk_reimbursements_types"
foreign key (type_id)
references reimbursement_types 

create table reimbursement_statuses(
	id			VARCHAR,
	status		VARCHAR unique,
	
	constraint "pk_reimbursement_statuses" primary key ("id")
);

create table reimbursement_types(
	id			VARCHAR,
	type		VARCHAR unique,
	
	constraint "pk_reimbursement_types" primary key ("id")
);

