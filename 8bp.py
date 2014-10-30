#!/usr/bin/python

from bs4 import BeautifulSoup
from ParserCommon import docurl
import MySQLdb
import sqlite3

# returns an array of record dictionaries
def scrape_disco():
  disco_url = "http://www.8bitpeoples.com/discography?show=all"
  r = docurl(disco_url)
  rootsoup = BeautifulSoup(r.text)
  results = []
  for index,item in enumerate(rootsoup.find_all('div','discographyBlock')):
    if (item.find('div','albumName')) is not None:
      disco_record = {}
      disco_record['img'] = item.find('div','albumImage').find('img').get('src')
      disco_record['albumname'] = item.find('div','albumName').text
      disco_record['artist'] = item.find('div','albumArtist').text
      disco_record['artistlink'] = item.find('div','albumArtist').find('a').get('href')
      disco_record['tracks'] = []
      for trackentry in item.find_all('a','albumTrack'):
        track_record = {}
        track_record['number'] = int(trackentry.text[0:2])
        track_record['title'] = trackentry.text[3:]
        track_record['url'] = trackentry.get('href')
        disco_record['tracks'].append(track_record)
      if (item.find('a','goLink') is not None):
        disco_record['dl'] = item.find('a','goLink').get('href')
      else:
        disco_record['dl'] = None
      disco_record['comment'] = item.find('div','albumComment').text
      disco_record['release_id'] = item.find('div','albumId').text
      results.append(disco_record)
  return results

def scrape_nobio():
  nobio = []
  artists_url = "http://www.8bitpeoples.com/artist/bit_shifter"
  r = docurl(artists_url)
  rootsoup = BeautifulSoup(r.text)
  for item in rootsoup.find_all('div','noBio'):
    nobio.append(item.text)

# returns an array of artist information dictionaries
def scrape_bio():
  artists_url = "http://www.8bitpeoples.com/artist/bit_shifter"
  r = docurl(artists_url)
  rootsoup = BeautifulSoup(r.text)
  artist_links = []
  artists = []
  for item in rootsoup.find('div',id='artistsLeftColumn').find_all('a','links'):
    artist_links.append([item.text,item.get('href')])
  for item in artist_links:
    results = {}
    resp = docurl(item[1])
    soup = BeautifulSoup(resp.text)
    results['name'] = item[0]
    results['url'] = item[1]
    results['desc'] = soup.find('div','texts justify').text
    results['imglink'] = soup.find('img',alt='Artist Photo').get('src')
    info = soup.find('div',id='artistsInfo').text.split('\n')
    for line in range(len(info)):
      item = info[line].strip()
      if (item == "REAL NAME"):
        results['realname'] = info[line+1].strip()
        continue
      elif (item == "BIRTHDATE"):
        results['birthday'] = info[line+1].strip()
        continue
      elif (item == "WEB SITE"):
        results['site'] = info[line+1].strip()
        continue
      elif (item == "LOCATION"):
        results['location'] = info[line+1].strip()
        continue
    artists.append(results)
  return artists

def setup_db(cursor):
    cursor.execute("create table if not exists Artist("
                    "artist_name		varchar(70) not null"
                    "real_name		varchar(40), "
                    "location		varchar(30),"
                    "website			varchar(30),"
                    "bio_text		varchar(500),"
                    "photo_link		varchar(30),"
                    "primary key (artist_name))")
    cursor.execute("create table if not exists Album("
                    "release_no		varchar(10,0) not null"
                    "album_name 		varchar(70) not null"
                    "download_link		varchar(30),"
                    "no_tracks		numeric(3,0),"
                    "artist_name 		varchar(70), not null"
                    "description		varchar(500),"
                    "art_link		varchar(30),"
                    "primary key (release_no),"
                    "foreign key (artist_name) references Artist on delete cascade")
    cursor.execute("create table if not exists Track("
                    "album_name 		varchar(70), not null"
                    "track_no			numeric(3,0),"
                    "title			varchar(70),"
                    "stream_link		varchar(30),"
                    "primary key (album_name, track_no),"
                    "foreign key (album_name) references Album"
                    "on delete cascade)")

def main():
  #db = MySQLdb.connect(host="localhost",
  #                  user="xxx",
  #                  passwd="xxx",
  #                  db="xxx")
  db = sqlite3.connect('tmp.sqlite3')
  c = db.cursor()
  setup_db(c)
  for item in scrape_disco():
    c.execute("INSERT INTO Album values('"+item['release_id']+"','"+item['albumname']+"','"+item['dl']+"','"+len(item['tracks'])+"','"+item['artist']+"','"+item['comment']+"','"+item['img']+"')")
    for track in item['tracks']:
      c.execute("INSERT INTO Track values('"+item['albumname']+"','"+track['number']+"','"+track['title']+"','"+track['url']+"')")
  for item in scrape_bio():
    c.execute("INSERT INTO Artist values('"+item['name']+"','"+item['realname']+"','"+item['location']+"','"+item['site']+"','"+item['desc']+"','"+item['imglink']+"')")
