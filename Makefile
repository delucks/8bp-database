# Makefile
# delucks

all:
	g++ -Wall -o scrape -lcurl scrape.cc 

clean:
	-@rm scrape
