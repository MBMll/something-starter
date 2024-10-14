CREATE TABLE h2_material (
  id BIGINT NOT NULL,
   name VARCHAR(255),
   update_time TIMESTAMP,
   delete_flag BOOLEAN,
   CONSTRAINT pk_h2_material PRIMARY KEY (id)
);