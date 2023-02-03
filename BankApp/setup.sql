create schema if not exists users;

create table if not exists users.customers (
	customer_id serial primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	username varchar(20) not null,
	pass_word varchar(20) not null,
	email varchar(50) not null
);


create table if not exists users.employees (
	employee_id serial primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	username varchar(20) not null,
	pass_word varchar(20) not null,
	email varchar(50) not null
);

create schema if not exists bank;


create table if not exists bank.account (
	account_id serial primary key,
	acc_type varchar(20) not null check (acc_type in ('CHECKING', 'SAVINGS', 'ROTH IRA', 'TRADITIONAL IRA')),
	balance numeric(9,2) not null,
	acc_status varchar(20) not null check (acc_status in ('ACTIVE', 'PENDING', 'DENIED', 'CLOSED'))
);

create table if not exists bank.customer_accounts (
	customer_id integer not null,
	account_id integer not null,
	primary key (customer_id, account_id),
	foreign key (customer_id) references users.customers(customer_id),
	foreign key (account_id) references bank.account(account_id) 
);


create table if not exists bank.transactions (
	transaction_id serial primary key,
	account_id integer not null,
	amount numeric(9, 2) not null,
	transact_type varchar(20) not null check (transact_type in ('WITHDRAWAL', 'DEPOSIT', 'TRANSFER')),
	message varchar(255) not null,
	transaction_date_start timestamp with time zone not null default now(),
	transaction_date_complete timestamp with time zone default now(),
	foreign key (account_id) references bank.account(account_id)
);


create table if not exists bank.transfers (
	transaction_id integer primary key,
	account_to_transfer integer not null,
	trans_status varchar(20) not null check(trans_status in ('PENDING', 'COMPLETE', 'CANCELLED')),
	foreign key (transaction_id) references bank.transactions(transaction_id),
	foreign key (account_to_transfer) references bank.account(account_id) 
);


insert into users.customers (first_name, last_name, username, pass_word, email) values ('Chris', 'Napton', 'cnapton0', 'ddM6gCvf', 'cnapton0@google.cn');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Dela', 'Pretsell', 'dpretsell1', '2C3Bm2Jiq', 'dpretsell1@nyu.edu');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Joana', 'Gate', 'jgate2', 'PwTlLsH8x', 'jgate2@si.edu');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Lavena', 'Baselio', 'lbaselio3', 'HCuzYKU1aUL', 'lbaselio3@wordpress.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Eryn', 'Chrishop', 'echrishop4', 'ydkP0a3', 'echrishop4@ezinearticles.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Osmund', 'Orniz', 'oorniz5', 'zoCciryhdGwh', 'oorniz5@google.nl');


insert into users.employees (first_name, last_name, username, pass_word, email) values ('Brenna', 'Gealy', 'bgealy0', '6Lq6YOMWQk', 'bgealy0@economist.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Alfy', 'Noblet', 'anoblet1', 'zwBPFffryR', 'anoblet1@about.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Iver', 'Iacovazzi', 'iiacovazzi2', 'BNwCBxFK', 'iiacovazzi2@hibu.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Andriana', 'Kleinstern', 'akleinstern3', 'KN6fieQY0', 'akleinstern3@abc.net.au');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Hal', 'Shelf', 'hshelf4', '6hFflHRI', 'hshelf4@comcast.net');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Justinian', 'Gleeson', 'jgleeson5', 'GdwQdBAabMG', 'jgleeson5@who.int');


insert into bank.account (acc_type, balance, acc_status) values ('CHECKING', 2345.67, 'ACTIVE'),
								('SAVINGS', 245.67, 'ACTIVE'),
								('CHECKING', 58970.23, 'ACTIVE'),
								('ROTH IRA', 0.00, 'DENIED'),
								('TRADITIONAL IRA', 0.00, 'CLOSED'),
								('CHECKING', 0.00, 'PENDING'),
								('SAVINGS', 5000.00, 'ACTIVE');


insert into bank.customer_accounts (customer_id, account_id) values (1, 1), (2, 2), (2, 3), (3, 4), (4, 5), (5, 6), (5, 7);
														
insert into bank.transactions (account_id, amount, transact_type, message, transaction_date_start, transaction_date_complete) 
	values 	(1, 675.90, 'WITHDRAWAL', 'Transaction complete', '2016-06-23 19:10:25-07', '2016-06-23 19:10:25-07'),
			(1, 3456.67, 'DEPOSIT', 'Transaction complete', '2017-07-23 15:18:50-08', '2017-07-23 15:18:50-08'),
			(2, 456.90, 'DEPOSIT', 'Transaction complete', '2018-09-18 08:15:24-02', '2018-09-18 08:15:24-02'),
			(3, 5000.00, 'TRANSFER', 'Pending: awaiting approval', '2018-10-12 09:46:45-03', null),
			(3, 6765.98, 'DEPOSIT', 'Transaction complete', '2019-01-01 12:05:35-07', '2019-01-01 12:05:35-07'), 
			(7, 100.00, 'TRANSFER', 'Pending: awaiting approval', '2020-01-01 12:35:06-10', null);

insert into bank.transfers (transaction_id, account_to_transfer, trans_status) values (4, 1, 'PENDING'), (6, 2, 'PENDING');