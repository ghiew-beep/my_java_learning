SRC_DIR = src
OUT_DIR = out
MAIN    = app.Program

SOURCES = $(shell find $(SRC_DIR) -name "*.java")

all: compile run

compile:
	javac -d $(OUT_DIR) $(SOURCES)

run:
	java -cp $(OUT_DIR) $(MAIN)

clean:
	rm -rf $(OUT_DIR)
	rm -f result.txt