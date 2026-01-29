insert into dish (id, name, dish_type)
values (1, 'Salaide fraîche', 'STARTER'),
       (2, 'Poulet grillé', 'MAIN'),
       (3, 'Riz aux légumes', 'MAIN'),
       (4, 'Gâteau au chocolat ', 'DESSERT'),
       (5, 'Salade de fruits', 'DESSERT');


insert into ingredient (id, name, category, price, id_dish)
values (1, 'Laitue', 'VEGETABLE', 800.0, 1),
       (2, 'Tomate', 'VEGETABLE', 600.0, 1),
       (3, 'Poulet', 'ANIMAL', 4500.0, 2),
       (4, 'Chocolat ', 'OTHER', 3000.0, 4),
       (5, 'Beurre', 'DAIRY', 2500.0, 4);



update dish
set price = 2000.0
where id = 1;

update dish
set price = 6000.0
where id = 2;

CREATE TABLE "order" (
                         id SERIAL PRIMARY KEY,
                         reference VARCHAR(10) UNIQUE NOT NULL, -- Exemple: ORD00001
                         creation_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dish_order (
                            id SERIAL PRIMARY KEY,
                            id_order INTEGER REFERENCES "order"(id),
                            id_dish INTEGER REFERENCES dish(id),
                            quantity INTEGER NOT NULL
);


CREATE TYPE order_type AS ENUM ('EAT_IN', 'TAKE_AWAY');

CREATE TYPE order_status AS ENUM ('CREATED', 'READY', 'DELIVERED');



