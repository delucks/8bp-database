#!/usr/bin/python

from bs4 import BeautifulSoup
from ParserCommon import docurl
import MySQLdb

# returns an array of record dictionaries
def scrape_disco():
  disco_url = "http://www.8bitpeoples.com/discography?show=all"
  r = docurl(disco_url)
  rootsoup = BeautifulSoup(r.text)
  results = []
  for index,item in enumerate(rootsoup.find_all('div','discographyBlock')):
    if (item.find('div','albumName')) is not None:
      disco_record = {}
      disco_record['albumname'] = item.find('div','albumName').text
      disco_record['artist'] = item.find('div','albumArtist').text
      disco_record['artistlink'] = item.find('div','albumArtist').find('a').get('href')
      disco_record['tracks'] = []
      for trackentry in item.find_all('a','albumTrack'):
        disco_record['tracks'].append(trackentry.text)
      if (item.find('a','goLink') is not None):
        disco_record['dl'] = item.find('a','goLink').get('href')
      else:
        disco_record['dl'] = None
      disco_record['comment'] = item.find('div','albumComment').text
      disco_record['release_id'] = item.find('div','albumId').text
      results.append(disco_record)
  return results

def scrape_artists():
  artists_url = "http://www.8bitpeoples.com/artist/bit_shifter"
  r = docurl(artists_url)
  rootsoup = BeautifulSoup(r.text)
  bio = []
  nobio = []
  for item in rootsoup.find('div',id='artistsLeftColumn').find_all('a','links'):
    results = {}
    bio.append(item.text)
  for item in rootsoup.find_all('div','noBio'):
    nobio.append(item.text)
  desc = rootsoup.find('div','texts justify').text

def setup_db(cursor):
    cursor.execute("create table if not exists Artist(
                    artist_name		varchar(70), not null
                    real_name		varchar(40), 
                    location		varchar(30),
                    website			varchar(30),
                    bio_text		varchar(500),
                    photo_link		varchar(30),
                    primary key (artist_name))")
    cursor.execute("create table if not exists Album(
                    release_#		numeric(10,0), not null
                    album_name 		varchar(70), not null
                    download_link		varchar(30),
                    #_tracks		numeric(3,0),
                    artist_name 		varchar(70), not null
                    description		varchar(500),
                    art_link		varchar(30),
                    primary key (release_#),
                    foreign key (artist_name) references Artist
                    on delete cascade
                    on update cascade)")
    cursor.execute("create table if not exists Track(
                    album_name 		varchar(70), not null
                    track_#			numeric(3,0),
                    title			varchar(70),
                    stream_link		varchar(30),
                    primary key (album_name, track_#)
                    foreign key (album_name) references Album
                    on delete cascade
                    on delete cascade)")

def main():
  db = MySQLdb.connect(host="localhost",
                    user="xxx",
                    passwd="xxx",
                    db="xxx")
  c = db.cursor()
  for item in scrape_disco():
    # TODO
    c.execute("INSERT INTO Album values('"+item+"')")

# for selects:
#c.execute("SELECT * FROM BLAH")
#for row in c.fetchall():
#  foo
scrape_artists()

