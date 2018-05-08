CREATE TABLE engine(engine_id serial, engine_data json);
CREATE INDEX ON engine((engine_data->>'fuelTypeName'));

-- entities
-- entity_types
-- attributes
-- attribute_sets