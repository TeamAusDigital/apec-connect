# --- !Ups

CREATE TABLE "user" (
  "id" BIGSERIAL PRIMARY KEY,
  "first_name" VARCHAR,
  "last_name" VARCHAR,
  "full_name" VARCHAR,
  "email" VARCHAR,
  "phone" VARCHAR,
  "avatar_url" VARCHAR,
  "verification_status" INT NOT NULL,
  "account_status" INT NOT NULL,
  "record_status" INT DEFAULT 0 NOT NULL,
  "date_created" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE "user_token" (
  "id" BIGSERIAL PRIMARY KEY,
  "user_id" BIGINT NOT NULL,
  "token" VARCHAR NOT NULL,
  "token_type" VARCHAR NOT NULL,
  "record_status" INT DEFAULT 0 NOT NULL,
  "date_created" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE "login_info" (
  "id" BIGSERIAL NOT NULL PRIMARY KEY,
  "provider_id" VARCHAR NOT NULL,
  "provider_key" VARCHAR NOT NULL,
  "record_status" INT DEFAULT 0 NOT NULL,
  "date_created" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE "user_login_info" (
  "user_id" BIGINT NOT NULL,
  "login_info_id" BIGINT NOT NULL,
  "record_status" INT DEFAULT 0 NOT NULL,
  "date_created" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE "auth_info" (
  "id" BIGSERIAL PRIMARY KEY,
  "data" VARCHAR NOT NULL,
  "auth_type" VARCHAR NOT NULL,
  "login_info_id" BIGINT NOT NULL,
  "record_status" INT DEFAULT 0 NOT NULL,
  "date_created" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL
);

INSERT INTO "user" ("full_name", "verification_status", "account_status") VALUES ('Test User', 1, 0);
insert into "login_info" ("provider_id", "provider_key") values ('credentials', 'test@agiledigital.com.au');
insert into "user_login_info" ("user_id", "login_info_id") values ((select max("id") from "user"), (select max("id") from "login_info"));
-- This is `1234` hashed.
insert into "auth_info" ("data", "auth_type", "login_info_id") values (
  '{"hasher":"bcrypt","password":"$2a$10$kEVVqPhI8D3BRWnUg4tBdueBKo4FTOGeaxivOqJrxz.4OnOYMjpeW"}',
  'com.mohiva.play.silhouette.api.util.PasswordInfo',
  (select max("id") from "login_info")
);

# --- !Downs

DROP TABLE IF EXISTS "auth_info";
DROP TABLE IF EXISTS "user_login_info";
DROP TABLE IF EXISTS "login_info";
DROP TABLE IF EXISTS "user_token";
DROP TABLE IF EXISTS "user";
