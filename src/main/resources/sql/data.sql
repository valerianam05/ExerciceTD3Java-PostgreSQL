SELECT unit,
       SUM(CASE WHEN type = 'OUT' THEN -quantity ELSE quantity END) as actual_quantity
FROM stock_movement
WHERE id_ingredient = 3 AND creation_datetime <= NOW()
GROUP BY unit;

