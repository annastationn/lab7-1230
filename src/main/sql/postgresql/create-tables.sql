CREATE TABLE IF NOT EXISTS "Organizations" (
                                               "id" bigint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
                                               "name" text NOT NULL,
                                               "coordinates_id" bigint NOT NULL,
                                               "creation_date" text NOT NULL,
                                               "annual_turnover" double precision NOT NULL,
                                               "type_id" bigint NOT NULL,
                                               "address_id" bigint NOT NULL,
                                               "owner_id" bigint NOT NULL,
                                               PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "OrganizationTypes" (
                                                   "id" bigint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
                                                   "name" text NOT NULL,
                                                   PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Addresses" (
                                           "id" bigint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
                                           "zip_code" bigint NOT NULL,
                                           PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Coordinates" (
                                             "id" bigint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
                                             "x" bigint NOT NULL,
                                             "y" bigint NOT NULL,
                                             PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Users" (
                                       "id" bigint GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
                                       "username" text NOT NULL,
                                       "password" text NOT NULL,
                                       PRIMARY KEY ("id")
);

ALTER TABLE "Organizations" ADD CONSTRAINT "Organizations_fk2" FOREIGN KEY ("coordinates_id") REFERENCES "Coordinates"("id");

ALTER TABLE "Organizations" ADD CONSTRAINT "Organizations_fk5" FOREIGN KEY ("type_id") REFERENCES "OrganizationTypes"("id");

ALTER TABLE "Organizations" ADD CONSTRAINT "Organizations_fk6" FOREIGN KEY ("address_id") REFERENCES "Addresses"("id");

ALTER TABLE "Organizations" ADD CONSTRAINT "Organizations_fk7" FOREIGN KEY ("owner_id") REFERENCES "Users"("id");



