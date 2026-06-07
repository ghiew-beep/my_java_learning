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

### Learning Outcome:
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

### Learning Outcome:
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


6. 4 Key JDBC interfaces and their hierarchy:

    ```text
    [ DataSource ] (The Manager)
     ↓ gives
    
    [ Connection ] (The Pipe/Session)
     ↓ creates
    
    [ PreparedStatement ] (The Blueprint)
     ↓ returns
    
    [ ResultSet ] (The Output Table)
    ```
7. Common consideration/scenarios when using these interfaces
    ### [Key 1] DataSource
    Factory for database connections. Hides connection details (URL, username, password) and typically provides connection pooling.
    ```java
    //Create a new database connection
    Connection conn = dataSource.getConnection();
   
    //Create connection with specific credentials
    Connection conn = dataSource.getConnection("admin", "secret");
   
    //Return maximum time (seconds) to wait for connection
    int timeout = dataSource.getLoginTimeout();
    
    //Set maximum time to wait for connection
    dataSource.setLoginTimeout(30);
    
    //Returns log writer for connection attempts
    PrintWriter writer = dataSource.getLogWriter();
    
    /*
    Real Object:           Wrapped Object (what you have):
    ┌─────────────────┐   ┌─────────────────────────┐
    │ HikariDataSource│   │ Some Proxy/Wrapper      │
    │ - poolSize: 10  │   │ ┌─────────────────────┐ │
    │ - timeout: 30s  │◄──│ │ HikariDataSource    │ │
    │ - ...           │   │ └─────────────────────┘ │
    └─────────────────┘   └─────────────────────────┘
    ↑
    You only see this wrapper
    */
    
    //Involved in best practice for any code that needs to interact with a proxied JDBC object safely:
    
        //Check if this DataSource wraps another class (for HikariCP specific features)
    boolean isHikari = dataSource.isWrapperFor(HikariDataSource.class);
    
        //Return the underlying implementation object
    HikariDataSource hikari = dataSource.unwrap(HikariDataSource.class);
    ```
   ### [Key 2] Connection
   Represents a session with the database. Handles transactions, creates statements, and manages database communication.
   
    - Transaction Management Methods
    ```java
    //Enable/disable auto-commit mode. false starts a transaction
    conn.setAutoCommit(false); // BEGIN
    
    //Return current auto-commit status
    boolean isAuto = conn.getAutoCommit();
    
    //Make all changes permanent since last commit/rollback
    conn.commit(); // COMMIT
    
    //Undo all changes since last commit/rollback
    conn.rollback(); // ROLLBACK
    
    //Create a named savepoint to roll back to partially
    Savepoint sp = conn.setSavepoint("beforeUpdate");
    
    //Roll back to a specific savepoint
    conn.rollback(sp);
    
    //Remove a savepoint
    conn.releaseSavepoint(sp);
    
    //Set transaction isolation level
    conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    ```
    - Statement Creation Methods
    ```java
    //Creates a simple Statement for static SQL
    Statement stmt = conn.createStatement();
    
    //Creates PreparedStatement for parameterized SQL
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
    
    //PreparedStatement that returns generated keys
    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
    //Creates CallableStatement for stored procedures
    CallableStatement cs = conn.prepareCall("{call get_user(?)}");
    ```
    - Metadata & Utility Methods
    ```java
    //Returns DatabaseMetaData about DB structure
    DatabaseMetaData meta = conn.getMetaData();
    
    //Checks if connection is closed
    if (!conn.isClosed()) { conn.close(); }
    
    //Checks if connection is still valid
    boolean valid = conn.isValid(5);
    
    //Closes the connection and releases resources
    conn.close();
    
    //Returns first warning reported on this connection
    SQLWarning warning = conn.getWarnings();
    
    //Clears all warnings for this connection
    conn.clearWarnings();
    ```
   ### [Key 3] PreparedStatement
   Precompiled SQL statement with parameter placeholders (?). Prevents SQL injection and improves performance for repeated execution.

   - Parameter Setting Methods
    ```java
    //general pattern for all: setType(index, value)

    ps.setNull(1, Types.BIGINT);
    
    ps.setInt(1, 25);
    ps.setLong(1, 100L);
    ps.setString(1, "alice");
    ps.setBoolean(1, true);
    ps.setDouble(1, 19.99);
    
    ps.setDate(1, Date.valueOf("2024-01-01"));
    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
    
    //generic method that can handle any type, less readable
    ps.setObject(1, LocalDate.now());
    ```
    - Execution Methods
    ```java
    //Executes SELECT query, returns ResultSet
    ResultSet rs = ps.executeQuery();
    
    //Executes INSERT/UPDATE/DELETE, returns row count
    int rows = ps.executeUpdate();
    
    //Executes any SQL, returns boolean (has ResultSet?)
    boolean isQuery = ps.execute();
    
    /*Executes batch of commands
    key methods:
    ps.addBatch();
    int[] counts = ps.executeBatch();
    */
    public void saveAll(List<Message> messages) {
    String sql = "INSERT INTO messages (text, author_id, room_id, message_date) VALUES (?, ?, ?, ?)";
    
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            
            for (Message msg : messages) {
                ps.setString(1, msg.getText());
                ps.setLong(2, msg.getAuthor().getId());
                ps.setLong(3, msg.getRoom().getId());
                ps.setTimestamp(4, Timestamp.valueOf(msg.getDateTime()));
                ps.addBatch();  // Add to batch
            }
            
            int[] counts = ps.executeBatch();  // Execute all at once and get the row affected per each command
            System.out.println("Inserted " + counts.length + " messages");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ```
    - Generated Keys Methods
    ```java
    //Returns auto-generated keys from INSERT
    ResultSet keys = ps.getGeneratedKeys();
    ```
   ### [Key 4] ResultSet
   Represents a table of data from a SELECT query. Provides cursor-based navigation and column access.
    ```java
    ```
   
## ex02_create_save

### Learning Outcome:
1. Adding new message(from existing user to a valid chatroom) to database with JDBC INSERT
2. Retrieving auto-generated IDs with getGeneratedKeys()
3. Transaction Management, Manual commit/rollback with setAutoCommit(false)
4. Data Validation, checking required fields before saving 
5. Exception Handling, custom exceptions for business logic errors

### note:

1. The code snippet from module implies strongly that we are creating new message from existing user:
    ```java
    public static void main(String args[]) {
    ...
    User creator = new User(7L, "user", "user", new ArrayList(), new ArrayList());
    User author = creator;
    Room room = new Room(8L, "room", creator, new ArrayList());
    Message message = new Message(null, author, room, "Hello!", LocalDateTime.now());
    MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(...);
    messagesRepository.save(message);
    System.out.println(message.getId()); // ex. id == 11
    }
    ```
   If we are creating new user, id field would have been omitted and let Postgres generate new one


2. Ways to insert ID
   - (not recommended) manual compute and insert, example: 
   ```sql
    INSERT INTO messages (id, text, author_id, room_id, message_date)
    VALUES (
        (SELECT COALESCE(MAX(id), 0) + 1 FROM messages),
        'Hello',
        1,
        1,
        NOW()
    );
   ```
   - (recommended) Or use BIGSERIAL during table creation, omit the field during insertion and Postgres will auto insert ID
   ```sql
   INSERT INTO messages (text, author_id, room_id, message_date)
   VALUES ('Hello', 1, 1, NOW());
   --field 'id' is omitted
   ```
3. Transaction
    ```bash
    try (Connection connection = dataSource.getConnection()) {...}
    
    //SQL vs jdbc code counterpart
    BEGIN
    connection.setAutoCommit(false);
    
    COMMIT
    connection.commit();
    
    ROLLBACK
    connection.rollback();
   ```
4. Use 'SELECT 1' for performance optimization
    ```java
    private boolean userExists(Connection connection, Long userId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    ```
   - SELECT in SQL works like return()
   - if query is valid, integer 1 will be returned instead of copied data from DB, thereby improve performance
   - serve as lightweight existence check

## ex03_update

### Learning Outcome:
1. Modifying existing records in database
2. Handling null values correctly (set column to NULL)
3. (Bonus) Version checking for concurrent updates 
4. Ensuring exactly one row was updated 
5. Maintaining data integrity during updates

### note:

1. ex02_create_save vs ex03_update
   - SQL command: INSERT vs UPDATE
   - create new data vs modify existing data
   - let postgres generate new ID vs caller must provide valid ID

2. To let setNull() works without running into error
   - remove NOT NULL constraints on 'text' and 'message_date' from 'messages' in schema.sql
   - rerun the schema.sql and data.sql before running the java program