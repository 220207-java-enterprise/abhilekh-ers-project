-- create base user roles
insert into user_roles 
values
		('7c3521f5-ff75-4e8a-9913-01d15ee4da01', 'ADMIN'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4da02', 'FINANCE_MANAGER'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4da03', 'EMPLOYEE');
		
-- create new ERS user 
insert into users 
values 
		('7c3521f5-ff75-4e8a-9913-01d15ee4db01', '4bhilekh', 'abhilekh390@revature.net', 'p4$$word', 'Abhilekh', 'Adhikari', true, '7c3521f5-ff75-4e8a-9913-01d15ee4da01'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4db02', 'mscott33', 'michael543@revature.net', 'p4$$word', 'Michael', 'Scott', true, '7c3521f5-ff75-4e8a-9913-01d15ee4da02'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4db03', 'schrutefarms', 'schrute399@revature.net', 'p4$$word', 'Dwight', 'Shrute', true, '7c3521f5-ff75-4e8a-9913-01d15ee4da03');
-- create reimbursement_statuses
insert into reimbursement_statuses 
values 
		('7c3521f5-ff75-4e8a-9913-01d15ee4dc01', 'PENDING'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4dc02', 'APPROVED'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4dc03', 'DENIED');
		
-- create reimbursement_types
insert into reimbursement_types 
values 
		('7c3521f5-ff75-4e8a-9913-01d15ee4dd01', 'LODGING'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4dd02', 'TRAVEL'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4dd03', 'FOOD'),
		('7c3521f5-ff75-4e8a-9913-01d15ee4dd04', 'OTHER');
		

-- create reimbursements
insert into reimbursements 
values 
		('7c3521f5-ff75-4e8a-9913-01d15ee4de01', 300.00, NOW(), null, 'Motel invoice from trip to Buffalo, NY', null, null, '7c3521f5-ff75-4e8a-9913-01d15ee4db03', null, '7c3521f5-ff75-4e8a-9913-01d15ee4dc01', '7c3521f5-ff75-4e8a-9913-01d15ee4dd01');  
