/*
select p.part_type_id as "Part type", count(*) as "Count"
from
    part as p
    join bom as b on p.id = b.parent_part_id
    join bom_descendant as bd on b.id = bd.part_bom_id
group by p.part_type_id
order by p.part_type_id asc;


select tcmey.*
from  turbo_car_model_engine_year as tcmey
where part_id in(
    select
        b2.child_part_id
    from
        bom as b
        join bom_descendant as bd on b.id = bd.part_bom_id
        join bom as b2 on bd.descendant_bom_id = b2.id
    where b.parent_part_id=68085
);

select distinct
    b.child_part_id
from
    turbo_car_model_engine_year as tcmey
    join bom as b on tcmey.part_id = b.parent_part_id
    join bom_descendant as bd on b.id = bd.part_bom_id
where b.parent_part_id=68085;

*/


delete from bom_descendant;
call RebuildBomDescendancyForPart(10980, 0);
-------------------------------------------------------------------------------

DELIMITER ;;

DROP PROCEDURE IF EXISTS RebuildBomDescendancyForPart;
CREATE PROCEDURE `RebuildBomDescendancyForPart`(IN part_id BIGINT, IN clean TINYINT)
    SQL SECURITY INVOKER
BEGIN
    -- Disable DATA NOT FOUND handlers from calling functions
    -- DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN END;
    IF clean != 0 THEN
        DELETE FROM bom_descendant
        WHERE part_bom_id IN (SELECT id FROM bom where parent_part_id = part_id);
    END IF;

    INSERT IGNORE INTO bom_descendant(
        part_bom_id, descendant_bom_id, distance, type, qty
    )
    SELECT
        b.id, b.id, 1, 'direct', b.quantity
    FROM bom b
    WHERE parent_part_id = part_id;

    SET @distance = 1;
    REPEAT
        SET @distance = @distance + 1;
        INSERT IGNORE INTO bom_descendant (
            `part_bom_id`,
            `descendant_bom_id`,
            `distance`,
            `type`,
            `qty`
        )
        SELECT
            bd.part_bom_id,
            bc.id,
            @distance,
            IF (bd.`type` = 'Interchange' OR (ii2.part_id <> b.child_part_id), 'Interchange', 'direct'),
            bc.quantity * bd.qty 
        FROM
            bom_descendant bd
            inner join bom as b on bd.descendant_bom_id = b.id
 
            left join interchange_item ii1 on b.child_part_id = ii1.part_id
            left join interchange_item ii2 on ii1.interchange_header_id = ii2.interchange_header_id

            inner join bom as bc on coalesce(ii2.part_id, b.child_part_id) = bc.parent_part_id

        WHERE bd.distance = @distance - 1;
        -- WHERE bd.distance = @distance - 1 AND b.parent_part_id = part_id;

    UNTIL ROW_COUNT() = 0 END REPEAT;

END ;;

DROP PROCEDURE IF EXISTS RebuildBomDescendancyAll;
CREATE PROCEDURE `RebuildBomDescendancyAll`()
    SQL SECURITY INVOKER
BEGIN
    DECLARE done BOOLEAN DEFAULT FALSE;
    DECLARE part_id BIGINT UNSIGNED;
    DECLARE cur CURSOR FOR SELECT id FROM part ORDER BY id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    DELETE FROM bom_descendant;

    OPEN cur;

    partsLoop: LOOP
        FETCH cur INTO part_id;
        IF done THEN
            LEAVE partsLoop;
        END IF;
        CALL RebuildBomDescendancyForPart(part_id, 0);
    END LOOP partsLoop;

    CLOSE cur;

END ;;

DROP PROCEDURE IF EXISTS RebuildBomDescendancy;
CREATE PROCEDURE `RebuildBomDescendancy`()
    SQL SECURITY INVOKER
BEGIN

    DELETE FROM bom_descendant;

    INSERT IGNORE INTO bom_descendant(
        part_bom_id, descendant_bom_id, distance, type, qty
    ) SELECT b.id, b.id, 1, 'direct', b.quantity FROM bom b;

    SET @distance = 1;
    REPEAT
        SET @distance = @distance + 1;
        INSERT IGNORE INTO bom_descendant (
            `part_bom_id`,
            `descendant_bom_id`,
            `distance`,
            `type`,
            `qty`
        )
        SELECT
            bd.part_bom_id,
            bc.id,
            @distance,
            IF (bd.`type` = 'Interchange' OR (ii2.part_id <> b.child_part_id), 'Interchange', 'direct'),
            bc.quantity * bd.qty 
        FROM
            bom_descendant bd
            inner join bom as b on bd.descendant_bom_id = b.id
 
            left join interchange_item ii1 on b.child_part_id = ii1.part_id
            left join interchange_item ii2 on ii1.interchange_header_id = ii2.interchange_header_id

            inner join bom as bc on coalesce(ii2.part_id, b.child_part_id) = bc.parent_part_id

        WHERE bd.distance = @distance - 1;

    UNTIL ROW_COUNT() = 0 END REPEAT;

END ;;


DELIMITER ;

