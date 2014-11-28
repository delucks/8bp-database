# Makefile
# delucks

all:
	g++ -Wall -o scrape -lcurl `xml2-config --cflags --libs` scrape.cc
	#g++ -Wall -o scrape -lcurl scrape.cc 

clean:
	-@rm scrape
