# afterwave
后浪论坛

##需求说明

###


##实现
###需要安装lombok.jar
具体做法是装好eclipse后， java -jar lombok.jar就可



init db

CREATE USER 'sqluser'@'localhost' IDENTIFIED BY 'password'; GRANT ALL PRIVILEGES ON . TO 'sqluser'@'localhost'; FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS afterwavedb DEFAULT CHARSET utf8 COLLATE utf8generalci;

fix elastic error

https://discuss.elastic.co/t/elasticsearch-5-4-1-availableprocessors-is-already-set/88036/8

sudo usermod -a -G group username
