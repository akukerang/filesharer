# File Sharer
File Sharer is a cryptography project that utilizes AES, RSA, and Hash functions to provide a "secure" file sharing service.
This project aims to provide a secure platform for sharing files between users by encrypting and storing the file's data using AES, 
encrypting the symmetric key using RSA, and uses Hash for password and user authentication.

## Security
The AES algorithm uses a 128-bit key, RSA uses a 2048-bit key.

## Limitations and Issues
- Keep file size under 80 MB
- Generating primes and keys for RSA takes a while
- Need to find a better way to sensitive information

## Installation
1. Clone the repository:
`git clone https://github.com/akukerang/filesharer.git`
2. Create an MySQL server, and run the following queries
```
USE FILES;

CREATE TABLE USERS (
	ID INT PRIMARY KEY AUTO_INCREMENT,
	USERNAME VARCHAR(256),
    	PASSWORDHASH VARCHAR(256),
    	PUBLICKEY VARCHAR(1300),
    	MASTERKEY VARCHAR(650)
);

CREATE TABLE FILES (
	ID INT PRIMARY KEY AUTO_INCREMENT,
  	FILENAME VARCHAR(256),
  	FILEDATA MEDIUMBLOB,
	USERNAME VARCHAR(256),
  	DATECREATED DATETIME
);

CREATE TABLE SHARED (
	ID INT PRIMARY KEY AUTO_INCREMENT,
  	FILENAME VARCHAR(256),
  	FILEDATA LONGBLOB,
	SENDER VARCHAR(256),
	RECIEVER VARCHAR(256),
	MASTERKEY VARCHAR(1000),
  	DATECREATED DATETIME
);

```
3. Edit the code as needed to link to the SQL server
4. Run `run.java`

## Usage
1. Start the app by running `run.java`
2. Create an account, and an account to share to.
3. To share a file:
    - Login to the sender account
    - Press `View Files`
    - Press `Upload`, and select a file to upload
    - Ensure file is uploaded by seeing it in the table
    - Press `Share`, and enter the recipient's username
4. To see shared files:
    - Login to the recipient account
    - Press `View Shared`
    - Should see file listed, along with sender's username
    - To save the file: Press `Download`, and select directory
5. To edit password and keys
    - Press `Edit Profile`
    - Current password must be provided to edit.
  
