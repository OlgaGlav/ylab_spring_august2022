alter table ulab_edu.person
    add email varchar(50);

comment on column ulab_edu.person.email is 'Email пользователя';
