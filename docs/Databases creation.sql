-- UserService:
create table sexes
(
    id serial not null
        constraint sexes_pk
            primary key,
    name text
);

create table users
(
    birth_date date,
    firstname text,
    id serial not null
        constraint users_pkey
            primary key,
    passport_number text,
    sex_id integer default 1 not null
        constraint users_sexes_id_fk
            references sexes
            on update set default on delete set default,
    surname text,
    tax_payer_id text,
    driver_licence_id text,
    "creditServiceId" bigint
);

create unique index users_creditserviceid_uindex
    on users ("creditServiceId");

create table parents
(
    "parentId" integer not null
        constraint parents_users_id_fk
            references users
            on update cascade on delete cascade,
    "childId" integer not null
        constraint parents_users_id_fk_2
            references users
            on update cascade on delete cascade,
    constraint parents_pk
        unique ("parentId", "childId")
);

-- CreditService:
create table branches
(
    id bigserial not null
        constraint branches_pk
            primary key,
    name text default 'Новый филиал банка'::text not null
);

comment on table branches is 'Филиалы банков, участвующих в системе кредитной истории';

create table sexes
(
    id serial not null
        constraint sexes_pk
            primary key,
    name text not null
);

comment on table sexes is 'Варианты пола пользователей';

create unique index sexes_name_uindex
    on sexes (name);

create table users
(
    id bigserial not null
        constraint users_pk
            primary key,
    firstname text,
    surname text,
    birth_date date,
    passport_number text,
    tax_payer_id text,
    driver_licence_id text,
    sex_id integer default 1 not null
        constraint users_sexes_id_fk
            references sexes
            on update set default on delete set default,
    patronimic text
);

comment on table users is 'Пользователи системы кредитной истории';

create table credits
(
    id bigserial not null
        constraint credits_pk
            primary key,
    "userId" bigint not null
        constraint credits_users_id_fk
            references users
            on update cascade on delete cascade,
    "totalSum" numeric(19,2) default 0 not null,
    "startPaying" date not null,
    "endPaying" date not null,
    "branchId" bigint default 1 not null
        constraint credits_branches_id_fk
            references branches
            on update set default on delete set default
);

comment on table credits is 'Займы пользователей';

create table payments
(
    id bigserial not null
        constraint table_name_pk
            primary key,
    "creditId" bigint not null
        constraint payments_credits_id_fk
            references credits
            on update cascade on delete cascade,
    sum numeric(19,2) default 0 not null,
    date date not null
);

comment on table payments is 'Платежи по займам';
