package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem("1", "Masovni protesti protiv Trumpa i Muska širom SAD-a", "Milioni Amerikanaca učestvovali su u protestima 'Hands Off!' protiv predsjednika Donalda Trumpa i njegovog savjetnika, Elona Muska, izražavajući nezadovoljstvo politikama administracije.", null, "Politika", true, "Vanity Fair", "2025-04-05"),
            NewsItem("2", "Elon Musk zagovara ukidanje carina između SAD-a i EU", "Elon Musk se distancirao od politike predsjednika Trumpa, pozivajući na uspostavljanje zone slobodne trgovine između Sjedinjenih Država i Evropske unije.", null, "Politika", false, "AS USA", "2025-04-06"),
            NewsItem("3", "Protesti protiv Trumpa privukli ogromne mase širom SAD-a", "Više od 1.000 demonstracija 'Hands Off!' održano je u svih 50 država, uz učešće preko 150 grupa koje se protive politikama administracije.", null, "Nauka/tehnologija", true, "The Guardian", "2025-04-06"),
            NewsItem("4", "Musk se zalaže za transatlantsku zonu slobodne trgovine", "Elon Musk je javno izrazio neslaganje s protekcionističkim mjerama predsjednika Trumpa, pozivajući na intenzivniju ekonomsku saradnju između SAD-a i Evrope.", null, "Politika", false, "Welt", "2025-04-06"),
            NewsItem("5", "Musk brani DOGE uprkos kritikama", "Elon Musk je izrazio iznenađenje intenzivnim kritikama na račun Department of Government Efficiency (DOGE), tvrdeći da je njegova transparentnost neupitna.", null, "Nauka/tehnologija", true, "Business Insider", "2025-04-06"),
            NewsItem("6", "Protesti od Manhattana do Aljaske protiv Trumpa i Muska", "Demonstranti su se okupili širom Sjedinjenih Država, izražavajući nezadovoljstvo politikama predsjednika Trumpa i uticajem Elona Muska.", null, "Politika", false, "AP News", "2025-04-05"),
            NewsItem("7", "Bosna i Hercegovina dobila kandidatski status za EU", "Evropsko vijeće je u decembru 2022. godine dodijelilo Bosni i Hercegovini status kandidata za članstvo u Evropskoj uniji.", null, "Politika", true, "Evropska komisija", "2022-12-15"),
            NewsItem("8", "Amra Džeko o nastavku igranja Edina Džeke za reprezentaciju BiH", "Amra Džeko je izjavila da će njen suprug, Edin Džeko, nastaviti igrati za nogometnu reprezentaciju Bosne i Hercegovine dok god bude mogao.", null, "Sport", false, "Reprezentacija.ba", "2024-05-22"),
            NewsItem("9", "Vlada FBiH održala hitnu sjednicu o sigurnosnoj situaciji", "Vlada Federacije BiH održala je hitnu sjednicu 31. januara 2024. godine, raspravljajući o trenutnoj sigurnosnoj situaciji u zemlji.", null, "Politika", true, "Vlada FBiH", "2024-01-31"),
            NewsItem("10", "NASA-in teleskop SPHEREx lansiran u martu 2025.", "NASA je 11. marta 2025. godine lansirala teleskop SPHEREx s ciljem mapiranja miliona galaksija i proučavanja porijekla svemira.", null, "Nauka/tehnologija", false, "NASA", "2025-03-11"),
            NewsItem("11", "Problemi s Tesla Cybertruck vozilima", "Tesla Cybertruck suočava se s deset glavnih problema koje potencijalni kupci trebaju znati prije kupovine.", null, "Nauka/tehnologija", true, "YouTube", "2025-04-03"),
            NewsItem("12", "EUFOR povećava prisustvo u Bosni i Hercegovini", "EUFOR će privremeno povećati broj svojih snaga u BiH kao proaktivnu mjeru podrške sigurnosti i stabilnosti u zemlji.", null, "Politika", false, "EUFOR", "2025-03-07"),
            NewsItem("13", "Najava posjete generalnog sekretara NATO-a Bosni i Hercegovini", "Generalni sekretar NATO-a Mark Rutte boravit će u radnoj posjeti Bosni i Hercegovini 10. marta 2025. godine.", null, "Politika", true, "Predsjedništvo BiH", "2025-03-07"),
            NewsItem("14", "NASA-ina misija PUNCH lansirana u martu 2025.", "NASA je 11. marta 2025. godine lansirala misiju PUNCH koja će pružiti neviđene podatke o solarnom vjetru i svemirskim vremenskim događajima.", null, "Nauka/tehnologija", false, "NASA", "2025-03-11"),
            NewsItem("15", "Nema dokaza da je Musk prijetio suspenzijom X naloga kritičnih prema Trumpu", "Reuters je utvrdio da nema dokaza da je Elon Musk izjavio da će suspendovati X naloge koji su kritični prema administraciji predsjednika Trumpa.", null, "Nauka/tehnologija", true, "Reuters", "2025-02-12"),
            NewsItem("16", "Pad prodaje Teslinih vozila u Njemačkoj", "Prodaja Teslinih vozila u Njemačkoj pala je za 62,2% u prvom kvartalu 2025. godine u odnosu na isti period prošle godine.", null, "Nauka/tehnologija", false, "Reuters", "2025-04-03"),
            NewsItem("17", "Evropska komisija usvojila paket proširenja za 2024. godinu", "Evropska komisija je u oktobru 2024. godine usvojila paket proširenja, uključujući preporuke za Bosnu i Hercegovinu.", null, "Politika", true, "Evropska komisija", "2024-10-30"),
            NewsItem("18", "Apple najavio novi MacBook Pro", "Novi model dolazi s poboljšanim performansama i dužim trajanjem baterije.", null, "Nauka/tehnologija", true, "Apple Newsroom", "2025-03-13"),
            NewsItem("19", "Uvođenje digitalne lične karte u BiH", "Građani će moći koristiti novu identifikaciju putem mobilne aplikacije.", null, "Nauka/tehnologija", false, "Ministarstvo unutrašnjih poslova", "2025-03-12"),
            NewsItem("20", "Ženska nogometna reprezentacija pobijedila Hrvatsku", "Sjajan nastup i važna pobjeda u kvalifikacijama za Euro 2026.", null, "Sport", true, "Nezavisne novine", "2025-03-11")
        )
    }
}