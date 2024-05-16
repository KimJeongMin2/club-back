create database clubcommunity;
CREATE USER 'user1'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON clubcommunity.* TO 'user1'@'localhost';
FLUSH PRIVILEGES;