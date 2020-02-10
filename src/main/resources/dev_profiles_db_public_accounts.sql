create table accounts
(
    id         integer not null
        constraint accounts_pkey
            primary key,
    first_name varchar(255),
    last_name  varchar(255),
    city       varchar(255),
    gender     varchar(255),
    username   varchar(255)
);

alter table accounts
    owner to postgres;

create table profiles_table
(
    id         integer      not null
        constraint profiles_pk
            primary key,
    username   varchar(255) not null,
    job_title  varchar(255) not null,
    department varchar(255) not null,
    company    varchar(255) not null,
    skill      varchar(255) not null
);

alter table profiles
    owner to postgres;

ALTER Table profiles_table
    RENAME TO profiles;

--task_1
SELECT *
FROM dev_profiles_db.public.profiles
WHERE department = 'Support'
ORDER BY skill;

--task_2
CREATE TABLE temporary AS
SELECT job_title, count(*) AS number
FROM dev_profiles_db.public.profiles
GROUP BY job_title;

CREATE TABLE job_title_more_than_3 AS
SELECT job_title, number
FROM temporary
WHERE number > 3;

DROP TABLE temporary;

--task_3
SELECT accounts.first_name, accounts.last_name, profiles.job_title, profiles.company
FROM dev_profiles_db.public.accounts
         Inner JOIN profiles
                    ON accounts.id = profiles.id
WHERE city = 'Isawa';

--task_4
SELECT department, COUNT(department) as users
FROM dev_profiles_db.public.profiles
GROUP BY department
HAVING COUNT(department) = (
    SELECT MAX(users)
    FROM (
             SELECT department, COUNT(department) users
             FROM dev_profiles_db.public.profiles
             GROUP BY department
         ) as users
);

--task_5
SELECT job_title, job_count, city, city_count
FROM (
         SELECT job_title, count(*) as job_count
         FROM dev_profiles_db.public.profiles
         WHERE job_title = 'Executive Secretary'
         GROUP BY job_title) as first
         INNER JOIN (
    SELECT city, count(*) as city_count
    FROM dev_profiles_db.public.accounts
    GROUP BY city) as second
                    ON first = second;


--task_6
SELECT *
FROM dev_profiles_db.public.accounts
ORDER BY city;

--task_7
UPDATE dev_profiles_db.public.profiles
SET job_title = REPLACE(job_title, 'Engineer', 'Developer')
WHERE job_title LIKE '%Engineer%';