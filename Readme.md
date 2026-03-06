### How to rebuild `target/`

Use Maven.

#### Clean old build

```
mvn clean
```

This **deletes the `target` folder**.

---

#### Build project

```
mvn package
```

This will:

1. Compile Java code
2. Run tests
3. Create the jar
4. Recreate `target/`

Result:

```
target/
 ├── classes/
 ├── test-classes/
 └── bank-management-system.jar
```

---

### Running the application

#### Method 1 (for development)

```
mvn spring-boot:run
```

Spring Boot compiles and runs the app.

---

#### Method 2 (run the built jar)

First build:

```
mvn package
```

Then run:

```
java -jar target/bank-management-system-0.0.1-SNAPSHOT.jar
```

---
