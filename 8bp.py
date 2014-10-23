#!/usr/bin/python

from bs4 import BeautifulSoup
from ParserCommon import docurl

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
    bio.append(item.text)
  for item in rootsoup.find_all('div','noBio'):
    nobio.append(item.text)
  desc = rootsoup.find('div','texts justify').text

scrape_artists()
