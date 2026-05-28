# Introduction
This module focuses on the database-facing components of a chat application—schema design, JDBC CRUD operations, connection pooling, and query optimization.

Modern applications rely on ORMs like Hibernate or JPA, but these are abstractions over the very concepts this module teaches. Understanding what happens beneath the abstraction is what enables debugging of performance issues, slow queries, and production database problems that ORMs cannot hide.

## Topics covered include:

1. Database schema design with one-to-many and many-to-many relationships
2. DAO pattern implementation with JDBC
3. Connection pooling using HikariCP
4. PreparedStatement and SQL injection prevention
5. Manual ResultSet to object mapping
6. Pagination with LIMIT and OFFSET
7. CTE and JSON aggregation for N+1 query prevention

The code written here is not production-ready. The fundamentals learned here are how production issues get debugged.

> *Above text was co-authored with AI assistance.*

## ex00_tables_and_entities

### learning outcome:
1. Design database tables from domain models
2. Implement correct relationship types
3. Use SERIAL for auto-generated numeric IDs
4. Create junction tables for many-to-many 
5. Write INSERT statements with test data 
6. Create Java model classes that mirror tables 
7. Implement equals(), hashCode(), toString() correctly

ex00 is about setting up the FOUNDATION for the mock application - no Java database code yet!

### note:
1. CASCADE - propagate to all child's entity 
2. junction table - bridge for m2m 
3. normalization (1NF, 2NF, 3NF)
   - reduce data redundancy 
   - increase query performance
   - maintain data integrity 
4. BIGSERIAL vs BIGINT, auto-increment behavior 
5. override equals + hashCode()
6. object.hash() vs field.hashCode()
   - for performance, choose the latter approach 
7. multiple field hashing: primitive vs wrapper/custom type
    ```
    //field2 being primitive
    - result = 31 * result + field2;
    //field2 being wrapper custom
    - result = 31 * result + (field2 != null ? field2.hashCode() : 0);
    ```
8. toString() override
   - Avoid infinite recursion from circular references.

9. inject initial dummy data via data.sql
    ```
    psql -d your_database_name -U your_username -f src/main/resources/data.sql
    ```

## ex01_read_find

### learning outcome:
1. Configure connection pool using HikariCP
2. Implement DAO (Repository) pattern
3. Write JOIN queries across multiple tables
4. Use PreparedStatement to prevent SQL injection
5. Map ResultSet to Java objects manually
6. Use Optional to handle null safety
7. Handle subentity references without recursion
8. Implement constructor injection for dependencies
9. Work with java.sql.Timestamp and LocalDateTime
10. Handle SQLException appropriately

ex01 is about READING data from the database using JDBC for the first time.

### note:
1. DAO - data access object
   - to decouple business logic and persistence logic

2. Why use connection pooling utility such as HikariCP?
   - to avoid the expensive overhead of establishing new connection to DB per query
   - Establishing a connection to db involves TCP handshake, TLS/SSL negotiation(if enabled), Postgres authentication, and connection cleanup.

3. Side note: JDBC & TLS/SSL
   - JDBC driver by default will go for SSL, and check if the postgres server has enabled SSL
     - ref: https://jdbc.postgresql.org/documentation/use/ under "sslmode (String) Default prefer"
   - login postgres server and do: SHOW ssl;
   - Note that this is a server-level setting

    Example if Postgres server enabled SSL:
    ```commandline
    postgres=# SHOW ssl;
     ssl
    -----
     on
    (1 row)
    
    postgres=#
    ```
4. Command to check postgres default port:
    ```commandline
    cat /etc/postgresql/*/main/postgresql.conf | grep "^port"
    ```
5. High level flow dealing with HikariCP, JDBC and postgres in getting data from database:
    - create HikariCP config
    - inject said config to create Hikari datasource
    - inject said datasource to create DAO
    - feed query parameter(id) to said DAO's designated method
    - DAO map sql query result to java obj and wrapped it inside an optional
    - handle exception and return optional object
