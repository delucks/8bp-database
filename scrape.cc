#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <curl/curl.h>

/*
 * file:   scrape.cc
 * author: delucks
 * date:   11/28/2014
 * description:
 * grab information from 8bitpeoples' catalog and put it in the database
 * written in two parts: use libcurl to save the file. then, use libxml2 to parse it
 */

const char* useragent = "CISC437 C++ databot (jluck@udel.edu)";
const char* discog_filepath = "discog.html";
const char* artist_filepath = "artist.html";
const char* discog_url = "http://www.8bitpeoples.com/discography?show=all";
const char* artist_url = "http://www.8bitpeoples.com/artist/bit_shifter";

// CURL callback to write file to a buffer
static size_t writer(void *ptr, size_t size, size_t nmemb, void *stream)
{
	int written = fwrite(ptr, size, nmemb, (FILE *)stream);
	return written;
}

// pull down a page using a pre-existing CURL handle, put it in fh
CURLcode download_page(CURL* c,const char* url,FILE* fh)
{
	CURLcode res;
	curl_easy_setopt(c,CURLOPT_URL,url);
	curl_easy_setopt(c,CURLOPT_USERAGENT,useragent);
	curl_easy_setopt(c,CURLOPT_FOLLOWLOCATION,1L);
	curl_easy_setopt(c,CURLOPT_WRITEFUNCTION,writer);
	curl_easy_setopt(c,CURLOPT_WRITEDATA,fh);
	res = curl_easy_perform(c);
	return res;
}

int main()
{
	CURL* c = curl_easy_init();
	FILE* discog_fh;
	discog_fh = fopen(discog_filepath,"wb");
	if (!discog_fh)
	{
		curl_easy_cleanup(c);
		fprintf(stderr,"Could not open discog filepath for writing\n");
		exit(EXIT_FAILURE);
	}
	if (c)
	{
		CURLcode rc = download_page(c,discog_url,discog_fh);
		if (rc != CURLE_OK)
		{
			fprintf(stderr,"Failed to download file.\n");
			exit(EXIT_FAILURE);
		}
		fclose(discog_fh);
		curl_easy_cleanup(c);
	}
	else
	{
		curl_easy_cleanup(c);
		fprintf(stderr,"Could not create CURL object\n");
		exit(EXIT_FAILURE);
	}
}
