# --- !Ups

CREATE TABLE "invoice" (
  "id" BIGSERIAL PRIMARY KEY,
  "date_issued" TIMESTAMP NOT NULL,
  "date_due" TIMESTAMP NOT NULL,
  "is_paid" BOOLEAN NOT NULL DEFAULT FALSE,
  "is_accepted" BOOLEAN NOT NULL DEFAULT FALSE,
  "amount" NUMERIC NOT NULL,
  "payment_reference" VARCHAR,
  "payment_options" VARCHAR NOT NULL,
  "payment_method" VARCHAR,
  "record_status" INT DEFAULT 0 NOT NULL,
  "created_at" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL,
  "version" INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE "participant_message" (
  "id" BIGSERIAL PRIMARY KEY,
  "sender_id" VARCHAR NOT NULL,
  "receiver_id" VARCHAR NOT NULL,
  "message" VARCHAR,
  "rating" INT,
  "invoice_id" INTEGER,
  "record_status" INT DEFAULT 0 NOT NULL,
  "created_at" TIMESTAMP DEFAULT now() NOT NULL,
  "last_updated" TIMESTAMP DEFAULT now() NOT NULL,
  "version" INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE ONLY participant_message ADD CONSTRAINT participant_message_invoice_id_fkey FOREIGN KEY (invoice_id) REFERENCES invoice(id);

# --- !Downs

DROP TABLE IF EXISTS "participant_message" CASCADE;
DROP TABLE IF EXISTS "invoice" CASCADE;
