# Makefile
# delucks

all:
	#g++ -Wall -o scrape -I/usa/jluck/8bp-database/tidy/include/tidy -L/usa/jluck/8bp-database/tidy/lib -ltidy -Wl,-rpath,/usa/jluck/8bp-database/tidy/lib -lcurl `xml2-config --cflags --libs` scrape.cc
	g++ -Wall -o scrape -lcurl `xml2-config --cflags --libs` scrape.cc 

clean:
	-@rm scrape
