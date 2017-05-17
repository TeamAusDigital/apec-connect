# --- !Ups

ALTER TABLE "participant" ADD COLUMN "economy" VARCHAR NOT NULL DEFAULT 'AU';

# --- !Downs

ALTER TABLE "participant" DROP COLUMN "economy";