CREATE TABLE h2_material (
  id BIGINT NOT NULL,
   name VARCHAR(255),
   update_time TIMESTAMP,
   deleted BOOLEAN,
   CONSTRAINT pk_h2_material PRIMARY KEY (id)
);

CREATE TABLE h2_equipment (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    create_time TIMESTAMP,
    update_time TIMESTAMP,
    deleted BOOLEAN,
    status VARCHAR(255),
    description VARCHAR(255)
);
