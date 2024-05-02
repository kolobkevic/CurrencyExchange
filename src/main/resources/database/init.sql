CREATE TABLE IF NOT EXISTS currency
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    code      VARCHAR UNIQUE NOT NULL,
    full_name VARCHAR        NOT NULL,
    sign      VARCHAR
);

CREATE TABLE IF NOT EXISTS exchange_rate
(
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate               DECIMAL(6),
    CONSTRAINT fk_base_currency_id FOREIGN KEY (base_currency_id) REFERENCES currency (id) ON DELETE CASCADE,
    CONSTRAINT fk_target_currency_id FOREIGN KEY (target_currency_id) REFERENCES currency (id) ON DELETE CASCADE,
    CONSTRAINT fk_base_target_id UNIQUE (base_currency_id, target_currency_id)
);

INSERT INTO currency (code, full_name, sign)
VALUES ('AZN', 'Азербайджанский манат', '₼'),
       ('BYN', 'Белорусский рубль', 'Р'),
       ('RUB', 'Российский рубль', 'Р'),
       ('GEL', 'Грузинский лари', '₾'),
       ('AED', 'Дирхам ОАЭ', '₼'),
       ('USD', 'Доллар США', '$'),
       ('EUR', 'Евро', '€'),
       ('CNY', 'Китайский юань', '¥'),
       ('TRY', 'Турецкая лира', '₺'),
       ('GBP', 'Фунт стерлингов Соединенного королевства', '£');

INSERT INTO exchange_rate (base_currency_id, target_currency_id, rate)
VALUES (1, 3, 54.8775),
       (2, 3, 28.6672),
       (4, 3, 34.9028),
       (5, 3, 25.4028),
       (6, 3, 93.2918),
       (7, 3, 99.5609),
       (8, 3, 12.8396),
       (9, 3, 2.8679),
       (10, 3, 115.0381),
       (7, 6, 0.93442),
       (6, 7, 1.07),
       (6, 10, 0.8);