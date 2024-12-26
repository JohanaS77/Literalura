ALTER TABLE books
    ALTER COLUMN birth_year TYPE varchar(255),
    ALTER COLUMN death_year TYPE varchar(255),
    ALTER COLUMN download_count SET DEFAULT 0;

--Actualizar registros existentes
UPDATE books
SET download_count = 0
WHERE download_count IS NULL;