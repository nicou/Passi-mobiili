# passi
Työkykypassi

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


