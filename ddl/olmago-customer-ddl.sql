alter database olmago_customer default character set utf8;

drop table if exists cust CASCADE;
drop table if exists cust_mbl_phone_rel_hst CASCADE;
drop table if exists mbl_phone CASCADE;
drop table if exists msg_envelope CASCADE;

create table cust (
   id bigint not null auto_increment,
    birthday date not null,
    ci varchar(256) not null,
    name varchar(40) not null,
    version integer not null,
    primary key (id)
) engine=InnoDB;

create table cust_mbl_phone_rel_hst (
   id bigint not null auto_increment,
    eff_end_dtm datetime(6),
    eff_sta_dtm datetime(6),
    version integer not null,
    cust_id bigint,
    mbl_phone_id bigint,
    primary key (id)
) engine=InnoDB;

create table mbl_phone (
   id bigint not null auto_increment,
    dc_uzoo_pass_prod_cd varchar(255),
    mbl_phone_prcpln varchar(255) not null,
    phone_num varchar(20) not null,
    prod_nm varchar(80) not null,
    svc_mgmt_num bigint not null,
    version integer not null,
    primary key (id)
) engine=InnoDB;

create table msg_envelope (
   id bigint not null auto_increment,
   uuid varchar(255) not null,
   msg_typ varchar(10) not null,
    agg_typ varchar(255),
    agg_id varchar(255),
    bind_nm varchar(255) not null,
    created_at datetime(6) not null,
    published_at datetime(6),
    published boolean not null,
    msg_name varchar(255) not null,
    payload varchar(2048) not null,
    primary key (id)
) engine=InnoDB;


alter table cust
   add constraint cust_n1 unique (ci);

alter table cust_mbl_phone_rel_hst
   add constraint cust_mbl_phone_rel_hst_n1 unique (cust_id, eff_end_dtm desc, mbl_phone_id);

alter table mbl_phone
   add constraint mbl_phone_n1 unique (svc_mgmt_num);

alter table cust_mbl_phone_rel_hst
   add constraint cust_mbl_phone_rel_hst_fk_cust
   foreign key (cust_id)
   references cust (id)
;

alter table cust_mbl_phone_rel_hst
   add constraint cust_mbl_phone_rel_hst_fk_mbl_phone
   foreign key (mbl_phone_id)
   references mbl_phone (id);

create index message_envelope_n1 on msg_envelope (published, id);