# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table device (
  id                        bigint not null,
  name                      varchar(255),
  created_at                timestamp,
  iridium_imei              varchar(255),
  position_id               bigint,
  constraint pk_device primary key (id))
;

create table device_position (
  id                        bigint not null,
  lat                       double,
  lon                       double,
  timestamp                 timestamp,
  device                    bigint,
  position_class            varchar(255),
  constraint pk_device_position primary key (id))
;

create table iridium_message (
  id                        bigint not null,
  type                      integer,
  msg                       clob,
  created_at                timestamp,
  updated_at                timestamp,
  constraint pk_iridium_message primary key (id))
;

create table message (
  id                        bigint not null,
  type                      integer,
  dst                       bigint,
  data                      varbinary(255),
  timestamp                 timestamp,
  constraint pk_message primary key (id))
;

create sequence device_seq;

create sequence device_position_seq;

create sequence iridium_message_seq;

create sequence message_seq;

alter table device add constraint fk_device_position_1 foreign key (position_id) references device_position (id) on delete restrict on update restrict;
create index ix_device_position_1 on device (position_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists device;

drop table if exists device_position;

drop table if exists iridium_message;

drop table if exists message;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists device_seq;

drop sequence if exists device_position_seq;

drop sequence if exists iridium_message_seq;

drop sequence if exists message_seq;

