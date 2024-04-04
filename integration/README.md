## Deployment


### mysql
1. set timezone

```yaml
    environment:
      - SET_CONTAINER_TIMEZONE=true
      - CONTAINER_TIMEZONE=Asia/Shanghai
```


### nacos

1. download init sql: [nacos-mysql-schema.sql](dockers%2Fmysql%2Fresource%2Fnacos-mysql-schema.sql), put into mysql 
   init folder
2. add user,database, privilege into sql file

```mysql-sql
CREATE USER 'nacos'@'%' IDENTIFIED BY 'nacos';
create database nacos_devtest;
grant all privileges on nacos_devtest.* to 'nacos'@'%';

```

#### profile

### [admin-server](admin-server)



## boot

```sh
docker-compose up -f docker-compose.yml -d
```