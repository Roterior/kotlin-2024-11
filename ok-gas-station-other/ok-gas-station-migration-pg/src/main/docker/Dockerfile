FROM liquibase/liquibase:4.31.1

COPY ../* .

CMD ["liquibase", "--changelog-file=changelog-v0.0.1.sql", "--url=jdbc:postgresql://postgres:5432/gasstation_orders", "update"]