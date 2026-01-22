-- Script: update-publicite-set-est_paye-false-per-row.sql
-- Met à jour chaque enregistrement de la table `publicite` un à un
-- et force `est_paye = false` pour chacun.
-- Exécution: psql -d taxi_brousse -f sql/update-publicite-set-est_paye-false-per-row.sql

BEGIN;

DO $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN SELECT id_publicite FROM publicite ORDER BY id_publicite LOOP
        EXECUTE format('UPDATE publicite SET est_paye = false WHERE id_publicite = %L;', rec.id_publicite);
        RAISE NOTICE 'Updated publicite id %', rec.id_publicite;
    END LOOP;
END$$;

COMMIT;

-- Option: vérifier
-- SELECT id_publicite, est_paye FROM publicite ORDER BY id_publicite;
