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
    BaseCurrencyId   INTEGER UNIQUE NOT NULL,
    TargetCurrencyId INTEGER UNIQUE NOT NULL,
    Rate             DECIMAL(6),
    CONSTRAINT fk_base_curr FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies (ID) ON DELETE CASCADE,
    CONSTRAINT fk_target_curr FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies (ID) ON DELETE CASCADE
);

INSERT INTO Currencies (Code, FullName, Sign) VALUES ('AZN', 'Азербайджанский манат', '₼'),
                                                     ('BYN', 'Белорусский рубль', 'Р'),
                                                     ('RUB', 'Российский рубль', 'Р'),
                                                     ('GEL', 'Грузинский лари', '₾'),
                                                     ('AED', 'Дирхам ОАЭ', '₼'),
                                                     ('USD', 'Доллар США', '$'),
                                                     ('EUR', 'Евро', '€'),
                                                     ('CNY', 'Китайский юань', '¥'),
                                                     ('TRY', 'Турецкая лира', '₺'),
                                                     ('GBP', 'Фунт стерлингов Соединенного королевства', '£');
