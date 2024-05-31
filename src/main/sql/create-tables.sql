create table if not exists users (
    username text not null unique ,
    password text not null ,
    registration_time timestamp default now()
);

create table if not exists organizations (
    id integer not null unique ,
    name text not null ,
    x integer not null ,
    y integer ,
    creation_date timestamp not null ,
    annual_turnover float not null ,
    type text ,
    address_zip_code text not null,
    owner text not null
);