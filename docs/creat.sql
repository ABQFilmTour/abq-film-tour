create table film_location (
  film_location_id CHAR(16) FOR BIT DATA not null,
  address varchar(255),
  created timestamp not null,
  imdb_id varchar(255),
  lat_coordinate double not null,
  long_coordinate double not null,
  original_details varchar(255),
  shoot_date bigint not null,
  site_name varchar(255) not null,
  production_production_id CHAR(16)
  FOR BIT DATA, primary key (film_location_id)
);

create table google_user (
  user_id CHAR(16) FOR BIT DATA not null,
  gmail_address varchar(255), google_id varchar(255),
  google_name varchar(255),
  primary key (user_id)
);

create table image (
  image_id CHAR(16) FOR BIT DATA not null,
  created timestamp not null,
  description varchar(255),
  film_location_id CHAR(16) FOR BIT DATA not null,
  user_id CHAR(16) FOR BIT DATA,
  primary key (image_id)
);

create table production (
  production_id CHAR(16) FOR BIT DATA not null,
  imdb_id varchar(255),
  plot varchar(255),
  release_year integer not null,
  title varchar(255),
  type varchar(255),
  primary key (production_id)
);

create table user_comment (
  user_comment_id CHAR(16) FOR BIT DATA not null,
  created timestamp not null,
  text varchar(4096) not null,
  film_location_id CHAR(16) FOR BIT DATA not null,
  user_id CHAR(16) FOR BIT DATA,
  primary key (user_comment_id)
);

alter table film_location add constraint FK7ocuvg1l5nfnekkbin41c0ymb
foreign key (production_production_id) references production

alter table image add constraint FKfhmk6kt9rbtkxqx6ywq691jt6
foreign key (film_location_id) references film_location on delete cascade

alter table image add constraint FKfxahgwhy5mclvt0t4wqqdyhpk
foreign key (user_id) references google_user

alter table user_comment add constraint FKrikvpvub4i1bobqe9krwdm7ci
foreign key (film_location_id) references film_location

alter table user_comment add constraint FKpae63qrk5i1fiv1uthc1psev8
foreign key (user_id) references google_user

create table film_location (
  film_location_id CHAR(16) FOR BIT DATA not null,
  address varchar(255),
  created timestamp not null,
  imdb_id varchar(255),
  lat_coordinate double not null,
  long_coordinate double not null,
  original_details varchar(255),
  shoot_date bigint not null,
  site_name varchar(255) not null,
  production_production_id CHAR(16) FOR BIT DATA,
  primary key (film_location_id)
);

create table google_user (
  user_id CHAR(16) FOR BIT DATA not null,
  gmail_address varchar(255),
  google_id varchar(255),
  google_name varchar(255),
  primary key (user_id)
);

create table image (image_id CHAR(16) FOR BIT DATA not null,
  created timestamp not null,
  description varchar(255),
  film_location_id CHAR(16) FOR BIT DATA not null,
  user_id CHAR(16) FOR BIT DATA,
  primary key (image_id)
);

create table production (
  production_id CHAR(16) FOR BIT DATA not null,
  imdb_id varchar(255),
  plot varchar(255),
  release_year integer not null,
  title varchar(255),
  type varchar(255),
  primary key (production_id)
);

create table user_comment (
  user_comment_id CHAR(16) FOR BIT DATA not null,
  created timestamp not null,
  text varchar(4096) not null,
  film_location_id CHAR(16) FOR BIT DATA not null,
  user_id CHAR(16) FOR BIT DATA, primary key (user_comment_id)
);

alter table film_location add constraint FK7ocuvg1l5nfnekkbin41c0ymb
foreign key (production_production_id) references production

alter table image add constraint FKfhmk6kt9rbtkxqx6ywq691jt6
foreign key (film_location_id) references film_location on delete cascade

alter table image add constraint FKfxahgwhy5mclvt0t4wqqdyhpk
foreign key (user_id) references google_user

alter table user_comment add constraint FKrikvpvub4i1bobqe9krwdm7ci
foreign key (film_location_id) references film_location

alter table user_comment add constraint FKpae63qrk5i1fiv1uthc1psev8
foreign key (user_id) references google_user
