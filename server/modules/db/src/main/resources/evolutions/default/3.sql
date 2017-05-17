# --- !Ups

CREATE TABLE "announcement" (
  "id" BIGSERIAL PRIMARY KEY,
  "sender_id" VARCHAR NOT NULL,
  "message" VARCHAR NOT NULL,
  "record_status" INT DEFAULT 0 NOT NULL,
  "created_at" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL,
  "version" INTEGER NOT NULL DEFAULT 0
);


# --- !Downs

DROP TABLE IF EXISTS "announcement" CASCADE;