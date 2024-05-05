create table if not exists `configs` (
    `app` varchar(64) not null,
    `env` varchar(64) not null,
    `namespace` varchar(64) not null,
    `pkey` varchar(64) not null,
    `pval` varchar(128) null
);



insert into configs (`app`,`env`,`namespace`,`pkey`, `pval`) values ('app1','dev','public','lht.a', 'dev100');
insert into configs (`app`,`env`,`namespace`,`pkey`, `pval`) values ('app1','dev','public','lht.b', 'http://localhost:9129');
insert into configs (`app`,`env`,`namespace`,`pkey`, `pval`) values ('app1','dev','public','lht.c', 'yd100');