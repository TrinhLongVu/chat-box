create database ChatBoxJAVA
use ChatBoxJAVA

CREATE TABLE Client (
    idClient INT IDENTITY(1,1) NOT NULL,
    username VARCHAR(50),
    password VARCHAR(50),
    PRIMARY KEY (idClient)
);

create table Content(
	idContent varchar(50),
	content varchar(255),
	primary key (idContent)
)

create table ChatBox (
	idClientSend int,
	idClientRecieve int,
	idContent varchar(50),
	foreign key (idClientSend) references Client(idClient),
	foreign key (idClientRecieve) references Client(idClient),
	foreign key (idContent) references Content(idContent)
)

insert Client(username, password)
values('user1', '123456'),
	('user2', '1234')

insert content(idContent, content)
values('1,2', '')
select * from Client

SELECT *
FROM Content
WHERE idContent IN ('1,2', '2,1');