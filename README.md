# passi
Työkykypassi

Tämä on työkykypassin mobiiliosa Android-puhelimille. IOS-versiota ei toistaiseksi ole.  
  
Ohjelmisto koostuu kolmesta osasesta, jotka on jaettu kolmeen github repositoryyn: Passi-mobiili, passi ja passi-rest. Tämä on mobiilin repository. passi sisältää www-käyttöliittymän ja passi-rest on mobiilin backend.  
  
Ohjelmisto on suunnattu 2. asteen oppilaitoksiin. Ohjelmisto mahdollistaa opiskelijoille mobiiliohjelmistolla kuvien ottamisen, tekstimuotoisten vastausten palauttamisen yms. mobiilisovelluksen perustoiminnot. www-käyttöliittymä (passi) on tarkoitettu opettajille, jossa he voivat tarkastella ja arvioida opiskelijoiden suorituksia, jotka kuuluvat tiettyyn ryhmään.  
  
Passi saa tietonsa rest-backendin kautta, jonka kanssa mobiilisovellus asioi.
  

Uuden projektin haku palvelimelta (git clone):
1.  tee kansio nimeltä git (jos ei jo ole)
2.  cd git
3.  git clone https://github.com/jusju/passi.git (luo passi kansion)


Koodin uusimpien muutosten haku palvelimelta (git pull):
1.  cd passi/
2.  git pull (hakee muutokset palvelimelta)


Omien muutosten hylkäys:
1.  muokkaa haluamiasi tiedostoja
2.  git checkout -- . (kaikki muutokset pois stagelta)
3.  git clean -f -d
4.  git pull (hakee palvelimella olevan version projektista)


Muokkaus ja tallennus palvelimelle (commit ja push):
1.  muokkaa haluamiasi tiedostoja
3.  git status (näyttää muokatut tiedostot punaisella)
4.  git add . (lisää kaikki tiedostot stagelle) tai git add tiedoston_nimi (lisää vain kyseisen tiedoston stagelle)
5.  git status (näyttää lisätyt tiedostot vihreällä)
6.  git commit -m “kommentti” (commitoi muutokset)
7.  git push origin master (vie muutokset palvelimelle)


