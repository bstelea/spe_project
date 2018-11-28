create table beerStocked
(
   id integer not null,
   name varchar(255) not null,
   origin varchar(255) not null,
   brewer varchar(255) not null,
   type varchar(255) not null,
   abv decimal(3,1) not null,
   primary key(id)
);