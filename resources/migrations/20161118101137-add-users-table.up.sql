CREATE TABLE users
(id VARCHAR(35) PRIMARY KEY,
 email VARCHAR(30) UNIQUE NOT NULL,
 phonenumber VARCHAR(30),
 pass VARCHAR(300),
 full_name VARCHAR(100),
 description TEXT,
 rate INTEGER,
 xcoordinate INTEGER,
 ycoordinate INTEGER,
 active BOOLEAN);
