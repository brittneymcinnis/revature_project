create schema users;

show search_path;

set search_path to users;

create table customers (
	customer_id serial primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	username varchar(20) not null,
	pass_word varchar(20) not null,
	email varchar(50) not null
);


create table employees (
	employee_id serial primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	username varchar(20) not null,
	pass_word varchar(20) not null,
	email varchar(50) not null
);

create schema bank;

show search_path;

set search_path to bank;

create type bank.account_type as enum ('CHECKING', 'SAVINGS', 'ROTH IRA', 'TRADITIONAL IRA');

create type account_status as enum ('ACTIVE', 'PENDING', 'DENIED', 'CLOSED');

create table account (
	account_id serial primary key,
	acc_type account_type not null,
	balance numeric(9,2) not null,
	acc_status account_status not null;
);

create table customer_accounts (
	customer_id integer references users.customers(customer_id) not null,
	account_id integer references account(account_id) not null,
	primary key (customer_id, account_id)
);

create type transaction_type as enum ('WITHDRAWAL', 'DEPOSIT', 'TRANSFER');


create table transactions (
	transaction_id serial primary key,
	account_id integer references account(account_id) not null,
	amount numeric(9, 2) not null,
	transact_type transaction_type not null,
	message varchar(255) not null,
	transaction_date_start timestamp with time zone not null default now(),
	transaction_date_complete timestamp with time zone default now()
);

create type transfer_status as enum ('PENDING', 'COMPLETE', 'CANCELLED');

create table transfers (
	transaction_id integer references transactions(transaction_id) primary key,
	account_to_transfer integer references account(account_id) not null,
	trans_status transfer_status not null
);

create or replace function create_new_customer_account(acct_type bank.account_type, cust_id integer)
returns void
language plpgsql
as $$
declare
	accountID integer;
begin
	insert into bank.account (acc_type, balance, acc_status) values (acct_type, 0.00, 'PENDING') returning account_id into accountID;
	insert into bank.customer_accounts (account_id, customer_id) values (accountID, cust_id);
end
$$


create or replace function makeTransaction(trans_type transaction_type, acct_id integer, amt numeric)
returns void
language plpgsql
as $$
declare 
	bal numeric;
begin 
	select balance into bal from bank.account where account_id = acct_id;
	if trans_type = 'WITHDRAWAL' then
		bal = bal - amt;
	elsif trans_type = 'DEPOSIT' then
		bal = bal + amt;
	end if;
	update bank.account set balance = bal where account_id = acct_id;
	insert into bank.transactions (account_id, amount, transact_type, message) values (acct_id, amt, trans_type, 'TRANSACTION COMPLETE');
end
$$

create or replace function post_transfer(account_from_id integer, account_to_id integer, amt numeric)
returns void
language plpgsql
as $$
declare 
	trans_id integer;
begin 
	insert into bank.transactions (account_id, amount, transact_type, message, transaction_date_complete) 
		values (account_from_id, amt, 'TRANSFER', 'AWAITING TRANSFER APPROVAL', null) returning transaction_id into trans_id;
	insert into bank.transfers (transaction_id, account_to_transfer, trans_status) values (trans_id, account_to_id, 'PENDING');
end
$$

create or replace function accept_transfer(transact_id integer, acc_to_id integer, acc_from_id integer, bal_to numeric, bal_from numeric)
returns void
language plpgsql
as $$
begin 
	update bank.account set balance = bal_to where account_id = acc_to_id;
	update bank.account set balance = bal_from where account_id = acc_from_id;
	update bank.transfers set trans_status = 'COMPLETE' where transaction_id = transact_id;
	update bank.transactions set transaction_date_complete = now(), message = 'TRANSACTION COMPLETE' where transaction_id = transact_id;
end
$$

create or replace function bank.deny_transfer(transact_id integer, msg varchar(50))
returns void
language plpgsql
as $$
begin 
	update bank.transactions set message = msg, transaction_date_complete = now() where transaction_id = transact_id;
	update bank.transfers set trans_status = 'CANCELLED' where transaction_id = transact_id;
end
$$

insert into account (acc_type, balance, acc_status) values 	('CHECKING', 2345.67, 'ACTIVE'),
															('SAVINGS', 245.67, 'ACTIVE'),
															('CHECKING', 58970.23, 'ACTIVE'),
															('ROTH IRA', 0.00, 'DENIED'),
															('TRADITIONAL IRA', 0.00, 'CLOSED');

														
insert into transactions (account_id, amount, transact_type, message, transaction_date_start) 
	values 	(1, 675.90, 'WITHDRAWAL', 'Transaction complete', '2016-06-23 19:10:25-07'),
			(1, 3456.67, 'DEPOSIT', 'Transaction complete', '2017-07-23 15:18:50-08'),
			(2, 456.90, 'DEPOSIT', 'Transaction complete', '2018-09-18 08:15:24-02'),
			(3, 5000.00, 'TRANSFER', 'Pending: awaiting approval', '2018-10-12 09:46:45-03'),
			(3, 6765.98, 'DEPOSIT', 'Transaction complete', '2019-01-01 12:05:35-07');
			
select * from transactions t inner join account a on t.account_id = a.account_id; 

select * from transactions;

insert into transfers (transaction_id, account_to_transfer, trans_status) values (4, 1, 'PENDING');

select * from transfers;

SELECT tf.transfer_id, tf.trans_status,
	tr.transaction_id, tr.amount, tr.transact_type, tr.message, tr.transaction_date_start, tr.transaction_date_complete,
	acc_from.account_id as "account_from_id", acc_from.acc_type as "account_from_type", acc_from.acc_status as "account_from_status", acc_from.balance as "account_from_balance",
	acc_to.account_id as "account_to_id", acc_to.acc_type as "account_to_type", acc_to.acc_status as "account_to_status", acc_to.balance as "account_to_balance"	
FROM transactions tr INNER JOIN account acc_from ON tr.account_id = acc_from.account_id
INNER JOIN transfers tf ON tr.transaction_id = tf.transaction_id INNER JOIN account acc_to ON tf.account_to_transfer = acc_to.account_id;

insert into users.customers (first_name, last_name, username, pass_word, email) values ('Chris', 'Napton', 'cnapton0', 'ddM6gCvf', 'cnapton0@google.cn');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Dela', 'Pretsell', 'dpretsell1', '2C3Bm2Jiq', 'dpretsell1@nyu.edu');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Joana', 'Gate', 'jgate2', 'PwTlLsH8x', 'jgate2@si.edu');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Lavena', 'Baselio', 'lbaselio3', 'HCuzYKU1aUL', 'lbaselio3@wordpress.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Eryn', 'Chrishop', 'echrishop4', 'ydkP0a3', 'echrishop4@ezinearticles.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Osmund', 'Orniz', 'oorniz5', 'zoCciryhdGwh', 'oorniz5@google.nl');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Nealson', 'Camellini', 'ncamellini6', 'c4kovEBFJdH', 'ncamellini6@google.co.uk');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Lucian', 'Jirek', 'ljirek7', 'DinKZSjBKzqa', 'ljirek7@wisc.edu');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Melany', 'McIlwaine', 'mmcilwaine8', 'Y7pm58RF1V7', 'mmcilwaine8@amazon.de');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Dianne', 'Emerton', 'demerton9', 'CIF97L', 'demerton9@pinterest.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Silvan', 'Aharoni', 'saharonia', 'yOCaMDMrjt', 'saharonia@youtube.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Ellswerth', 'Titchard', 'etitchardb', 'cgcDFdom8avG', 'etitchardb@example.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Gina', 'Blaydon', 'gblaydonc', 'WdiI48LZSDI9', 'gblaydonc@unicef.org');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Simone', 'Lukasik', 'slukasikd', 'TXsvyJY', 'slukasikd@alibaba.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Norrie', 'Frome', 'nfromee', 'fmfp6A9', 'nfromee@bandcamp.com');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Isac', 'Durno', 'idurnof', 'unjhIjY', 'idurnof@ca.gov');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Tybie', 'Gisburn', 'tgisburng', 'euRCX2', 'tgisburng@dion.ne.jp');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Cloris', 'Cullagh', 'ccullaghh', 'cjSKhg', 'ccullaghh@cornell.edu');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Sam', 'Iannelli', 'siannellii', 'rPok2fY9vdj', 'siannellii@hhs.gov');
insert into users.customers (first_name, last_name, username, pass_word, email) values ('Hinze', 'Mort', 'hmortj', 'xyTkMwPxOUcC', 'hmortj@dell.com');


insert into users.employees (first_name, last_name, username, pass_word, email) values ('Brenna', 'Gealy', 'bgealy0', '6Lq6YOMWQk', 'bgealy0@economist.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Alfy', 'Noblet', 'anoblet1', 'zwBPFffryR', 'anoblet1@about.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Iver', 'Iacovazzi', 'iiacovazzi2', 'BNwCBxFK', 'iiacovazzi2@hibu.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Andriana', 'Kleinstern', 'akleinstern3', 'KN6fieQY0', 'akleinstern3@abc.net.au');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Hal', 'Shelf', 'hshelf4', '6hFflHRI', 'hshelf4@comcast.net');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Justinian', 'Gleeson', 'jgleeson5', 'GdwQdBAabMG', 'jgleeson5@who.int');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Celie', 'Stooders', 'cstooders6', 'ZM3XEQC5g', 'cstooders6@godaddy.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Colan', 'Budibent', 'cbudibent7', '03FeT6m', 'cbudibent7@goo.gl');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Lauren', 'Ashworth', 'lashworth8', 'QNMlXUFQMMS', 'lashworth8@businessweek.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Orazio', 'Christauffour', 'ochristauffour9', '83maxU', 'ochristauffour9@yelp.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Wallie', 'Stansall', 'wstansalla', 'P1HFZl', 'wstansalla@microsoft.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Paola', 'Scammell', 'pscammellb', 'BVJyj4R3SsV', 'pscammellb@scientificamerican.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Gina', 'Regi', 'gregic', 'QJ0fwBF', 'gregic@eventbrite.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Terry', 'Mackelworth', 'tmackelworthd', 'YcEYyFTwv6Mb', 'tmackelworthd@addthis.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Dilan', 'Treffry', 'dtreffrye', '4HOBEIis', 'dtreffrye@aol.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Morgana', 'Pettit', 'mpettitf', 'h8ltvBZ', 'mpettitf@studiopress.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Bobinette', 'Maskill', 'bmaskillg', 'BKAR7PO34', 'bmaskillg@soundcloud.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Richart', 'Songer', 'rsongerh', 'C4gYsR9JmG', 'rsongerh@netvibes.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Mikaela', 'Darcey', 'mdarceyi', 'U4yrXw', 'mdarceyi@icq.com');
insert into users.employees (first_name, last_name, username, pass_word, email) values ('Culver', 'Thorneloe', 'cthorneloej', 'xyg7HZV', 'cthorneloej@csmonitor.com');

insert into bank.customer_accounts (customer_id, account_id) values (1, 1);

SELECT * FROM bank.account acc INNER JOIN bank.customer_accounts ca
ON acc.account_id = ca.account_id
INNER JOIN users.customers c ON c.customer_id = ca.customer_id
WHERE c.customer_id = 1;

insert into customer_accounts (customer_id, account_id) values (2, 2);

SELECT acc.account_id, acc.acc_type, acc.balance FROM bank.transfers tf INNER JOIN bank.account acc
	ON tf.account_to_transfer = acc.account_id
	INNER JOIN bank.customer_accounts ca ON ca.account_id = tf.account_to_transfer
	INNER JOIN users.customers cust ON ca.customer_id = cust.customer_id
	WHERE cust.customer_id = 2;
	
SELECT * FROM bank.account WHERE acc_type = 'PENDING';

SELECT acct_to.account_id as account_to_id, acct_to.acc_type as account_to_type, acct_to.balance as acct_to_balance, acct_to.acc_status as acct_to_status,
acct_from.account_id as acct_from_id, acct_from.acc_type as acct_from_type, acct_from.balance as acct_from_balance, acct_from.acc_status as acct_from_status,
tr.transaction_id, tr.amount, tr.transact_type, tr.message, tr.transaction_date_start, tr.transaction_date_complete,
tf.trans_status FROM users.customers c INNER JOIN bank.customer_accounts ca ON c.customer_id = ca.customer_id
INNER JOIN bank.account acct_to ON acct_to.account_id = ca.account_id
INNER JOIN bank.transfers tf ON acct_to.account_id = tf.account_to_transfer
INNER JOIN bank.transactions tr ON tf.transaction_id = tr.transaction_id
INNER JOIN bank.account acct_from ON tr.account_id = acct_from.account_id
WHERE c.customer_id = 2;

insert into transfers (transaction_id, account_to_transfer, trans_status) values (20, 2, 'PENDING');
