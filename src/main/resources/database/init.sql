CREATE TABLE IF NOT EXISTS Currencies
(
    ID       INTEGER PRIMARY KEY AUTOINCREMENT,
    Code     VARCHAR UNIQUE NOT NULL,
    FullName VARCHAR        NOT NULL,
    Sign     VARCHAR
);

CREATE TABLE IF NOT EXISTS ExchangeRates
(
    ID               INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId   INTEGER NOT NULL,
    TargetCurrencyId INTEGER NOT NULL,
    Rate             DECIMAL(6),
    CONSTRAINT fk_base_curr FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies (ID) ON DELETE CASCADE,
    CONSTRAINT fk_target_curr FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies (ID) ON DELETE CASCADE
);

INSERT INTO Currencies (Code, FullName, Sign)
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

INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
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
       (6, 7, 1.07);