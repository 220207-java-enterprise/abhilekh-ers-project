create table users(
	id			VARCHAR primary key,
	username	VARCHAR unique not null,
	email		VARCHAR unique not null,
	password	VARCHAR not null,
	given_name	VARCHAR unique not null,
	surname		VARCHAR unique not null,
	is_active 	BOOLEAN,
	--FK
	role_id		VARCHAR 
);

create table user_roles(
	id 			VARCHAR primary key,
	role 		VARCHAR unique,
)

create table reimbursements(
	id 			VARCHAR primary key,
	amount		NUMBER(6,2) not null,
	submitted	TIMESTAMP not null,
	resolved	TIMESTAMP,
	description	VARCHAR not null,
	receipt		BLOB,
	payment_id	VARCHAR,
	--FKs
	author_id	VARCHAR not null,
	resolver_id	VARCHAR,
	status_id 	VARCHAR not null,
	type_id		VARCHAR not null
);

create table reimbursement_statuses(
	id			VARCHAR primary key,
	status		VARCHAR unique
);

create table reimbursement_types(
	id			VARCHAR primary key,
	type		VARCHAR unique
);

