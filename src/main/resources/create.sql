CREATE TABLE IF NOT EXISTS `{TABLE_NAME}` (
  `Username`  VARCHAR(16) PRIMARY KEY,
  `Cash`      DOUBLE      NOT NULL         DEFAULT 0,
  UNIQUE (`Username`)
);
