# ex00 — File Signature Analyzer

Identifies file types by reading their magic bytes (file signature) rather than relying on file extensions.

## How it works

On startup, the program reads `signatures.txt` which contains a list of known file formats and their corresponding hex signatures. When given a file path, it reads the first N bytes of that file and compares them against the known signatures to determine the file type.

## Project structure

```
ex00/
├── src/
│   └── com/example/filesignatureanalyzer/
│       ├── app/
│       │   └── Program.java
│       ├── model/
│       │   └── FileSignature.java
│       └── utility/
│           ├── SignatureLoader.java
│           ├── SignatureAnalyzer.java
│           └── ResultWriter.java
└── signatures.txt
```

## Requirements

- Java 17+
- `signatures.txt` must be present in the working directory

## How to run manually

### option 1
```bash
# compile @/java_basics/module02_io_files/ex00_FileSignature
javac -d out src/**/*.java

# run from ex00 directory (where signatures.txt lives)
java -cp out app.Program
```
### option 2 (Makefile)
```bash
# to compile and run
make
# to run again
make run
# to cleanup
make clean
```
## Usage

The program prompts for file paths one at a time. Enter a full path to a file and press Enter:

```
-> /home/user/images/photo.png
PROCESSED
-> /home/user/documents/archive.zip
PROCESSED
-> 42
```
The program capabilities is only as good as the signatures it knows. When given a valid file with alien format:
```
-> /home/user/doc/file.zpp
UNPROCESSED
```
Enter `42` to exit the program.

## Input rules

- Input must be a full absolute path to a file
- On WSL, use Linux paths: `/home/user/file.png`
- To access Windows files from WSL: `/mnt/c/Users/Admin/file.png`
- File must exist and be readable
- Enter `42` to stop the program

## Output

Results are written to `result.txt` in the working directory. Each line contains the detected file type:

```
PNG
ZIP
```

If a file type cannot be determined, it is logged as `UNDEFINED` to the console but not written to `result.txt`.

## signatures.txt format

Each line follows this exact format:

```
FILE_TYPE, XX XX XX XX
```

Example:
```
PNG, 89 50 4E 47 0D 0A 1A 0A
JPG, FF D8 FF
ZIP, 50 4B 03 04
```

- File type and hex bytes are separated by `, ` (comma + space)
- Hex bytes are separated by single spaces
- Empty lines are ignored
- Lines with invalid format will cause the program to exit with an error

## Resources & references

- [List of file signatures — Wikipedia](https://en.wikipedia.org/wiki/List_of_file_signatures)

## Use of AI

- Development assisted by [Claude](https://claude.ai) (Anthropic) — used for concept explanations, code review, and debugging guidance.