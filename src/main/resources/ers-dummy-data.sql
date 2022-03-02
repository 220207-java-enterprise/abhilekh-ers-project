-- create base user roles
insert into user_roles 
values
		('1', 'ADMIN'),
		('2', 'FINANCE_MANAGER'),
		('3', 'EMPLOYEE');
		
-- create new ERS user 
insert into users 
values 
		('1', '4bhilekh', 'abhilekh390@revature.net', 'p4$$word', 'Abhilekh', 'Adhikari', true, '1'),
		('2', 'mscott33', 'michael543@revature.net', 'p4$$word', 'Michael', 'Scott', true, '2'),
		('3', 'schrutefarms', 'schrute399@revature.net', 'p4$$word', 'Dwight', 'Shrute', true, '3');
-- create reimbursement_statuses
insert into reimbursement_statuses 
values 
		('1', 'PENDING'),
		('2', 'APPROVED'),
		('3', 'DENIED');
		
-- create reimbursement_types
insert into reimbursement_types 
values 
		('1', 'LODGING'),
		('2', 'TRAVEL'),
		('3', 'FOOD'),
		('4', 'OTHER');
		

-- create reimbursements
insert into reimbursements 
values 
		('1', 300.00, NOW(), null, 'Motel invoice from trip to Buffalo, NY', null, null, '3', null, '1', '1');  


SELECT u.id, u.given_name, u.surname, u.email, u.username, u.password, u.role_id, ur.role, u.is_active 
FROM users u 
JOIN user_roles ur 
ON u.role_id = ur.id;

	
select * from reimbursements r ;
select * from reimbursement_types rt where id='2';
select * from reimbursement_statuses rs ;

SELECT u.id, u.given_name, u.surname, u.email, u.username, u.password, u.role_id, ur.role, u.is_active 
FROM users u 
JOIN user_roles ur 
ON u.role_id = ur.id
where u.id='3';