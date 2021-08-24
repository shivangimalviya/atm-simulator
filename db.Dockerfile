FROM mysql:8

ENV MYSQL_DATABASE=atm-machine-simulator-db \
    MYSQL_ROOT_PASSWORD=rootpassword

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306