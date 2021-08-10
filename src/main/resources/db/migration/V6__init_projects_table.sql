create table projects
(
    id    int primary key auto_increment,
    description varchar (100) NOT NULL
);
alter table TASK_GROUPS add column project_id int null ;
alter table TASK_GROUPS add foreign key (project_id) references projects(id);