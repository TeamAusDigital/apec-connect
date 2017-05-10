# --- !Ups

CREATE TABLE "participant" (
  "id" BIGSERIAL PRIMARY KEY,
  "identifier" VARCHAR NOT NULL,
  "business_name" VARCHAR NOT NULL,
  "email" VARCHAR,
  "phone" VARCHAR,
  "auth_token" VARCHAR NOT NULL,
  "verification_status" INT NOT NULL,
  "account_status" INT NOT NULL,
  "record_status" INT DEFAULT 0 NOT NULL,
  "created_at" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL,
  "version" INTEGER NOT NULL DEFAULT 0
);


# --- !Downs

DROP TABLE IF EXISTS "participant";
