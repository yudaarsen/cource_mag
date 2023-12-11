CREATE TABLE department (
	id SERIAL,
	name VARCHAR(50) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (name)
);

CREATE TABLE function (
	department_id INT,
	id SERIAL,
	name VARCHAR(100) NOT NULL,
	PRIMARY KEY (department_id, id),
	FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE CASCADE,
	UNIQUE (department_id, name)
);

CREATE TABLE employee (
	personnel_number INT,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	middle_name VARCHAR(100),
	function_id INT NOT NULL,
	department_id INT NOT NULL,
	email VARCHAR(255),
	phone VARCHAR(15),
	salary NUMERIC(15, 2) NOT NULL DEFAULT 0,
	PRIMARY KEY (personnel_number),
	FOREIGN KEY (function_id, department_id) REFERENCES function(id, department_id) ON DELETE RESTRICT,
	UNIQUE (email),
	UNIQUE (phone)
);

CREATE TABLE timesheet (
	personnel_number INT,
	year INT,
	month SMALLINT,
	day SMALLINT,
	present BOOL,
	PRIMARY KEY (personnel_number, year, month, day),
	FOREIGN KEY (personnel_number) REFERENCES employee(personnel_number) ON DELETE RESTRICT
);

CREATE TABLE document_type (
	code CHAR(4),
	name VARCHAR(50) NOT NULL,
	PRIMARY KEY (code)
);

CREATE TABLE document (
	id SERIAL,
	type_id CHAR(4) NOT NULL,
	posting_date DATE NOT NULL,
	note VARCHAR(250),
	reverse_document INT,
	PRIMARY KEY (id),
	FOREIGN KEY (type_id) REFERENCES document_type(code) ON DELETE RESTRICT,
	FOREIGN KEY (reverse_document) REFERENCES document(id) ON DELETE SET NULL
);

CREATE TABLE account (
	code CHAR(10),
	parent_id CHAR(10),
	name VARCHAR(100) NOT NULL,
	PRIMARY KEY (code),
	FOREIGN KEY (parent_id) REFERENCES account(code) ON DELETE RESTRICT
);

CREATE TABLE deduction (
	code CHAR(4),
	account_id CHAR(10) NOT NULL,
	rate INT DEFAULT 0,
	PRIMARY KEY (code),
	FOREIGN KEY (account_id) REFERENCES account(code) ON DELETE RESTRICT
);

CREATE TABLE employee_deduction (
	personnel_number INT,
	deduction_id CHAR(4),
	rate INT DEFAULT 0,
	PRIMARY KEY (personnel_number, deduction_id),
	FOREIGN KEY (personnel_number) REFERENCES employee(personnel_number) ON DELETE RESTRICT,
	FOREIGN KEY (deduction_id) REFERENCES deduction(code) ON DELETE RESTRICT
);

CREATE TABLE document_position (
	document_id INT,
	pos_num INT,
	pos_type CHAR(1) NOT NULL,
	account_id CHAR(10) NOT NULL,
	amount NUMERIC(15, 2) NOT NULL,
	personnel_number INT,
	note VARCHAR(250),
	PRIMARY KEY (document_id, pos_num),
	FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE,
	FOREIGN KEY (account_id) REFERENCES account(code) ON DELETE RESTRICT,
	FOREIGN KEY (personnel_number) REFERENCES employee(personnel_number) ON DELETE RESTRICT
);

CREATE UNIQUE INDEX ux_function_dep_name ON function(department_id, UPPER(name));
CREATE UNIQUE INDEX ux_department_name ON department(UPPER(name));

INSERT INTO account VALUES ('0000000000', null, 'Технический счет'),
						   ('2000000000', null, 'Основное производство'),
						   ('2600000000', null, 'Общехозяйственные расходы'),
						   ('5100000000', null, 'Расчетные счета'),
						   ('6800000000', null, 'Расчеты по налогам и сборам'),
						   ('6900000000', null, 'Расчеты по социальному страхованию и обеспечению'),
						   ('6910000000', '6900000000', 'Расчеты по социальному страхованию'),
						   ('6920000000', '6900000000', 'Расчеты по пенсионному обеспечению'),
						   ('6930000000', '6900000000', 'Расчеты по обязательному медицинскому страхованию'),
						   ('7000000000', null, 'Расчеты с персоналом по оплате труда'),
						   ('7600000000', null, 'Расчеты с разными дебиторами и кредиторами');
						   
INSERT INTO document_type VALUES ('REVE', 'Документ сторно');