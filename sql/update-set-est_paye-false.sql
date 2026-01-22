-- Script: update-set-est_paye-false.sql
-- But: met la colonne `est_paye` à FALSE pour toutes les tables qui possèdent cette colonne
-- Exécution: psql -d taxi_brousse -f sql/update-set-est_paye-false.sql

BEGIN;

DO $$
DECLARE
    tbl record;
BEGIN
    FOR tbl IN
        SELECT table_schema, table_name
        FROM information_schema.columns
        WHERE column_name = 'est_paye'
    LOOP
        -- Exécute un UPDATE sur chaque table trouvée
        EXECUTE format('UPDATE %I.%I SET est_paye = false WHERE est_paye IS DISTINCT FROM false;', tbl.table_schema, tbl.table_name);
    END LOOP;
END$$;

COMMIT;

-- Optionnel: afficher les résultats par table
-- SELECT table_schema, table_name FROM information_schema.columns WHERE column_name = 'est_paye';
