SRC_DIR = app
OUT_DIR = out
MAIN    = app.Program

SOURCES = $(shell find $(SRC_DIR) -name "*.java")

all: compile

compile:
	javac -d $(OUT_DIR) $(SOURCES)

run:
	java -cp $(OUT_DIR) $(MAIN) $(ARGS)

clean:
	rm -rf $(OUT_DIR)
	rm -f result.txt