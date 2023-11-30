CREATE TABLE ticket
(
    id                BIGINT(20) AUTO_INCREMENT NOT NULL,
    price             DECIMAL(10, 2)            NOT NULL,
    date_of_departure DATETIME                  NOT NULL,
    one_way_route_id  BIGINT                    NOT NULL,
    return_route_id   BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (one_way_route_id) REFERENCES route(id),
    FOREIGN KEY (return_route_id) REFERENCES route(id)
);