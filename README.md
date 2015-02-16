# Sideswipe-vertical-viewpager #

<i>Author:</i> Victor Choy <[vic.choy@gmail.com](mailto: vic.choy@gmail.com), [victor.cui@vibin.it](mailto: victor.cui@vibin.it)>

<i>Copyright</i> © 2015 www.vibin.it

## About ##

First off, this app uses: [https://github.com/LambergaR/VerticalViewPager](https://github.com/LambergaR/VerticalViewPager), which is a vertical ViewPager, not horizontal (thanks for the hard work, props! [Martin Sonc](http://martinsonc.blogspot.com/)).

This is an sample app which allows View items inside of a vertical ViewPager to be swiped left and right. If you inspect the code, you can see that 
I implemented drop zones in my main layout. When the View items are dragged over these drop zones, the subscribing Activity (or Fragment) will be 
notified on callbacks.

Feel free to pick and choose whatever you want to use, but you will need just about everything withthe exception of "MainActivity".
If you need help with your implementation, you can contact me via email (preferably Gmail).

## TODOs ##

- Fix the bounds of the ViewPager layout, so the views go completely off screen and looks cooler
- Add support for reversed stack view paging... currently, the views feel paged in the wrong way
- Make this a project library and maybe even a jar
- Remove the possible copyrighted images :o


## About Vibin ##

We at Vibin make cool stuff... check out our Photo Notes app and give us feedback on what you think! It's a great way to organize your photos and tag them at the same time!

[App Store](https://itunes.apple.com/us/app/vibin-photo-notes/id749920897?mt=8), 
[Google Play](https://play.google.com/store/apps/details?id=it.vibin.app)

## Keywords ##

Keywords: android, library, vertical view pager, horizontal swipe, drag and drop, cards ui

### Open Source Licenses ###

[LambergaR/VerticalViewPager](https://github.com/LambergaR/VerticalViewPager)
Apache License Version 2.0, January 2004
(Thanks Martin Sonc!)