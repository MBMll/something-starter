#### 基于 `truevoly/oracle-12c` 编写 [Dockerfile](Dockerfile-initdb) 和 [docker-compose](docker-compose.yml) 文件

#### 等待输出完成 log

#### 进入sql编辑器

```shell
sqlplus system/oracle@//localhost:1521/xe
```

```roomsql
create temporary tablespace TEST_DBF_TEMP tempfile '/home/oracle/data/TEST_DBF_TEMP.dbf' size 50m autoextend on next 50m maxsize 2048m;

create  tablespace TEST_DBF datafile '/home/oracle/data/TEST_DBF.dbf' size 50M autoextend on next 50m maxsize 2048m;

create user TEST identified by "123" default tablespace TEST_DBF temporary tablespace TEST_DBF_TEMP;

grant create session,connect,resource,dba to TEST;

imp TEST/123 file = /data_20181210.dmp full=y;

```

### issue
#### 当存在 oracledata, 报错 ORA-12547: TNS:lost contact
#### 初始化 sql: ORA-01659: unable to allocate MINEXTENTS beyond 4 in tablespace PGKF
#### 采用 Dockerfile build 太慢,太大
#### tomcat 访问