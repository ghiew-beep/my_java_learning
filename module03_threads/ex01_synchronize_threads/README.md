# Java Thread Synchronization Example

## Overview
This Java program demonstrates **thread synchronization** using `synchronized`, `wait()`, and `notifyAll()`. Two threads — `"Egg"` and `"Hen"` — alternate printing their respective tags to the console a specified number of times. The program enforces strict turn-taking to avoid race conditions.

---

## Features
- Uses a shared `SharedState` object to coordinate turns between threads.
- Accepts a command-line argument to specify how many times each thread should print its tag.
- Validates input to ensure it is a positive integer.
- Demonstrates proper use of `synchronized`, `wait()`, and `notifyAll()` for thread coordination.

---

## Code Structure
### `SharedState`
```java
class SharedState {
    boolean eggTurn = true;
}
```

Holds a boolean indicating whose turn it is (true = Egg, false = Hen).

### `MyThread`
Custom thread class that prints "Egg" or "Hen" in alternation.
Constructor parameters:
```
tag — The text to print
count — Number of times to print
lock — Object used for synchronization
state — Shared state object
mySignal — Boolean indicating which thread’s turn corresponds to this thread
```
### `Core logic in run():`
```
Acquire the lock.
Wait while it is not the thread’s turn.
Print the tag.
Toggle the shared turn.
Notify the other thread.
```
### `Program`

Validates command-line argument in the format:
```
--count=POSITIVE_INTEGER
```
Creates a shared lock object and shared state.
Starts "Egg" and "Hen" threads and waits for them to finish using join().
Usage

Compile the program in ex01/:
```bash
javac src/app/Program.java
```

Run the program with a count of 10:
```bash
java -cp src app.Program --count=10
```

Expected output:
```bash
Thread-0: Egg
Thread-1: Hen
Thread-0: Egg
Thread-1: Hen
...
```

Threads strictly alternate printing "Egg" and "Hen".
Notes
Input validation ensures the program only accepts positive integers.
synchronized ensures only one thread prints at a time.
wait() suspends a thread until it is notified.
notifyAll() wakes all threads waiting on the lock to check if it is their turn.