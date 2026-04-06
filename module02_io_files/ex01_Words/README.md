# ex01_Words — Text Similarity Analyzer

Measures the similarity between two text files using cosine similarity on word frequency vectors.

## How it works

Each file is tokenized into words. A frequency map is built tracking how often each word appears in each file. Cosine similarity is then computed between the two frequency vectors:

```
similarity = (A · B) / (||A|| * ||B||)
```

A result of `1.0` means identical word distribution, `0.0` means no common words.

## Project structure

```
ex01_Words/
├── Makefile
├── README.md
└── app/
    ├── app/
    │   └── Program.java
    ├── model/
    │   └── WordFrequency.java
    └── service/
        └── TextSimilarityAnalyzer.java
```

## Requirements

- Java 17+

## How to run

```bash
# compile
make

# run
make run ARGS="file1.txt file2.txt"

# clean
make clean
```

## Usage

```bash
make run ARGS="input1.txt input2.txt"
Similarity = 0.91
```

## Input rules

- Exactly 2 arguments required — both must be `.txt` files
- Files must exist and be non-empty
- Words are split by spaces
- Case insensitive — `Hello` and `hello` are the same word
- Words must contain at least one alphabetic character
- Words containing numbers are valid — e.g. `hello123`
- Leading and trailing punctuation is stripped — e.g. `"hello"` becomes `hello`
- Empty lines are ignored

## Resources & references

- [Cosine similarity — Wikipedia](https://en.wikipedia.org/wiki/Cosine_similarity)

## Use of AI

- Development assisted by [Claude](https://claude.ai) (Anthropic) — used for concept explanations, code review, and debugging guidance.