package com.deutschai.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal data class WordToolItem(
    val german: String,
    val czech: String,
    val sentenceGerman: String,
    val sentenceCzech: String
)

internal data class WordToolGroup(
    val id: String,
    val title: String,
    val subtitle: String,
    val items: List<WordToolItem>
)

internal data class WordToolQuestion(
    val id: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val fullGerman: String,
    val fullCzech: String,
    val explanation: String
)

internal data class WordToolMode(
    val id: String,
    val title: String,
    val subtitle: String
)

internal val wordToolModes = listOf(
    WordToolMode("words", "1. Překlad slov", "Vyber český význam německého výrazu."),
    WordToolMode("sentences", "2. Krátké věty", "Doplň správné slovo do jednoduché věty."),
    WordToolMode("challenge", "3. Náročnější použití", "Pochop výraz v delší větě a vyber celý český překlad.")
)

private fun wordToolGroup(id: String, title: String, subtitle: String, vararg rows: String) =
    WordToolGroup(
        id, title, subtitle, rows.map { row ->
            val f = row.split("§", limit = 4)
            require(f.size == 4) { "Invalid word-tool row: $row" }
            WordToolItem(f[0], f[1], f[2], f[3])
        }
    )

internal val wordToolGroups = listOf(
    wordToolGroup("numbers", "Číslovky", "Základní a časté číselné výrazy",
        "null§nula§Der Zähler zeigt null an.§Počítadlo ukazuje nulu.",
        "eins§jedna§Die Zahl eins steht am Anfang.§Číslo jedna stojí na začátku.",
        "zwei§dva / dvě§Wir brauchen zwei Fahrkarten.§Potřebujeme dvě jízdenky.",
        "drei§tři§Drei Gäste warten draußen.§Venku čekají tři hosté.",
        "vier§čtyři§Der Kurs beginnt in vier Minuten.§Kurz začíná za čtyři minuty.",
        "fünf§pět§Ich arbeite fünf Tage pro Woche.§Pracuji pět dní v týdnu.",
        "sechs§šest§Der Zug fährt um sechs Uhr ab.§Vlak odjíždí v šest hodin.",
        "sieben§sedm§Meine Schwester steht um sieben Uhr auf.§Moje sestra vstává v sedm hodin.",
        "acht§osm§Der Supermarkt schließt um acht Uhr.§Supermarket zavírá v osm hodin.",
        "neun§devět§Der Unterricht beginnt um neun Uhr.§Výuka začíná v devět hodin.",
        "zehn§deset§Ich brauche zehn Minuten.§Potřebuji deset minut.",
        "elf§jedenáct§Der Bus kommt in elf Minuten.§Autobus přijede za jedenáct minut.",
        "zwölf§dvanáct§Wir essen um zwölf Uhr zu Mittag.§Obědváme ve dvanáct hodin.",
        "dreizehn§třináct§Das Ticket kostet dreizehn Euro.§Lístek stojí třináct eur.",
        "vierzehn§čtrnáct§Die Reise dauert vierzehn Tage.§Cesta trvá čtrnáct dní.",
        "fünfzehn§patnáct§Der Film beginnt in fünfzehn Minuten.§Film začíná za patnáct minut.",
        "sechzehn§šestnáct§Meine Tochter ist sechzehn Jahre alt.§Moje dcera má šestnáct let.",
        "siebzehn§sedmnáct§Heute kommen siebzehn Personen.§Dnes přijde sedmnáct lidí.",
        "achtzehn§osmnáct§Der Schüler ist achtzehn Jahre alt.§Tomuto studentovi je osmnáct let.",
        "neunzehn§devatenáct§Der Laden hat bis neunzehn Uhr geöffnet.§Obchod má otevřeno do devatenácti hodin.",
        "zwanzig§dvacet§Wir haben zwanzig Minuten Zeit.§Máme dvacet minut času.",
        "dreißig§třicet§Die Fahrt dauert dreißig Minuten.§Cesta trvá třicet minut.",
        "fünfzig§padesát§Das Zimmer kostet fünfzig Euro pro Nacht.§Pokoj stojí padesát eur za noc.",
        "hundert§sto§In diesem Buch sind hundert Seiten.§Tato kniha má sto stran.",
        "tausend§tisíc§Tausend Menschen besuchen das Fest.§Oslavu navštíví tisíc lidí."
    ),
    wordToolGroup("ordinal-numbers", "Řadové číslovky", "První až dvacátý pátý; 25 otázek",
        "ersten§první§Am ersten Tag besuche ich einen neuen Kurs.§První den navštívím nový kurz.",
        "zweiten§druhý§Am zweiten Tag lerne ich neue Wörter.§Druhý den se učím nová slova.",
        "dritten§třetí§Am dritten Tag fahre ich nach Berlin.§Třetí den jedu do Berlína.",
        "vierten§čtvrtý§Am vierten Tag habe ich einen Termin.§Čtvrtý den mám schůzku.",
        "fünften§pátý§Am fünften Tag arbeite ich von zu Hause.§Pátý den pracuji z domova.",
        "sechsten§šestý§Am sechsten Tag besuche ich meine Familie.§Šestý den navštívím rodinu.",
        "siebten§sedmý§Am siebten Tag mache ich eine Pause.§Sedmý den si udělám přestávku.",
        "achten§osmý§Am achten Tag beginnt ein neues Projekt.§Osmý den začíná nový projekt.",
        "neunten§devátý§Am neunten Tag schreibe ich einen Test.§Devátý den píšu test.",
        "zehnten§desátý§Am zehnten Tag endet der Sprachkurs.§Desátý den končí jazykový kurz.",
        "elften§jedenáctý§Am elften Tag kommt ein wichtiger Brief.§Jedenáctý den přijde důležitý dopis.",
        "zwölften§dvanáctý§Am zwölften Tag fahren wir ans Meer.§Dvanáctý den jedeme k moři.",
        "dreizehnten§třináctý§Am dreizehnten Tag besichtigen wir ein Museum.§Třináctý den navštívíme muzeum.",
        "vierzehnten§čtrnáctý§Am vierzehnten Tag kehre ich nach Hause zurück.§Čtrnáctý den se vrátím domů.",
        "fünfzehnten§patnáctý§Am fünfzehnten Tag treffe ich meine Freunde.§Patnáctý den se setkám s přáteli.",
        "sechzehnten§šestnáctý§Am sechzehnten Tag beginnt die nächste Etappe.§Šestnáctý den začíná další etapa.",
        "siebzehnten§sedmnáctý§Am siebzehnten Tag kaufe ich ein neues Buch.§Sedmnáctý den si koupím novou knihu.",
        "achtzehnten§osmnáctý§Am achtzehnten Tag besuchen wir unsere Nachbarn.§Osmnáctý den navštívíme sousedy.",
        "neunzehnten§devatenáctý§Am neunzehnten Tag findet die Besprechung statt.§Devatenáctý den se koná porada.",
        "zwanzigsten§dvacátý§Am zwanzigsten Tag bekomme ich die Ergebnisse.§Dvacátý den dostanu výsledky.",
        "einundzwanzigsten§dvacátý první§Am einundzwanzigsten Tag beginnt mein Urlaub.§Dvacátý první den začíná moje dovolená.",
        "zweiundzwanzigsten§dvacátý druhý§Am zweiundzwanzigsten Tag besuche ich einen Freund.§Dvacátý druhý den navštívím kamaráda.",
        "dreiundzwanzigsten§dvacátý třetí§Am dreiundzwanzigsten Tag fahren wir mit dem Zug.§Dvacátý třetí den jedeme vlakem.",
        "vierundzwanzigsten§dvacátý čtvrtý§Am vierundzwanzigsten Tag feiern wir ein Fest.§Dvacátý čtvrtý den slavíme.",
        "fünfundzwanzigsten§dvacátý pátý§Am fünfundzwanzigsten Tag endet unsere Reise.§Dvacátý pátý den končí naše cesta."
    ),
    wordToolGroup("multiplicative-numbers", "Násobné číslovky", "Jednou, dvakrát, třikrát a další násobky; 25 otázek",
        "einmal§jednou§Ich lese den Text einmal laut vor.§Přečtu text jednou nahlas.",
        "zweimal§dvakrát§Ich höre mir die Aufnahme zweimal an.§Poslechnu si nahrávku dvakrát.",
        "dreimal§třikrát§Sie ruft ihre Mutter dreimal pro Woche an.§Volá mamince třikrát týdně.",
        "viermal§čtyřikrát§Wir treffen uns viermal im Monat.§Setkáváme se čtyřikrát měsíčně.",
        "fünfmal§pětkrát§Er wiederholt das Wort fünfmal.§Opakuje to slovo pětkrát.",
        "sechsmal§šestkrát§Der Bus fährt sechsmal am Tag.§Autobus jede šestkrát denně.",
        "siebenmal§sedmkrát§Ich wiederhole die Übung siebenmal.§Opakuji cvičení sedmkrát.",
        "achtmal§osmkrát§Die Maschine piept achtmal.§Stroj osmkrát zapípá.",
        "neunmal§devětkrát§Sie hat neunmal versucht, ihn zu erreichen.§Devětkrát se ho pokusila zastihnout.",
        "zehnmal§desetkrát§Bitte lies den Satz zehnmal.§Přečti prosím větu desetkrát.",
        "elfmal§jedenáctkrát§Der Spieler hat elfmal getroffen.§Hráč skóroval jedenáctkrát.",
        "zwölfmal§dvanáctkrát§Die Uhr schlägt zwölfmal.§Hodiny odbijí dvanáctkrát.",
        "dreizehnmal§třináctkrát§Ich habe dreizehnmal dieselbe Frage gehört.§Stejnou otázku jsem slyšel třináctkrát.",
        "vierzehnmal§čtrnáctkrát§Sie kontrolliert die Liste vierzehnmal.§Kontroluje seznam čtrnáctkrát.",
        "fünfzehnmal§patnáctkrát§Der Trainer wiederholt die Übung fünfzehnmal.§Trenér opakuje cvičení patnáctkrát.",
        "sechzehnmal§šestnáctkrát§Wir haben sechzehnmal angerufen.§Volali jsme šestnáctkrát.",
        "siebzehnmal§sedmnáctkrát§Das Wort kommt siebzehnmal im Text vor.§Toto slovo se v textu vyskytuje sedmnáctkrát.",
        "achtzehnmal§osmnáctkrát§Er hat achtzehnmal nachgefragt.§Osmnáctkrát se doptával.",
        "neunzehnmal§devatenáctkrát§Die Datei wurde neunzehnmal geöffnet.§Soubor byl otevřen devatenáctkrát.",
        "zwanzigmal§dvacetkrát§Ich habe die Regel zwanzigmal erklärt.§Pravidlo jsem vysvětlil dvacetkrát.",
        "dreißigmal§třicetkrát§Der Name steht dreißigmal auf der Liste.§Jméno je na seznamu třicetkrát.",
        "vierzigmal§čtyřicetkrát§Die Frage wurde vierzigmal gestellt.§Otázka byla položena čtyřicetkrát.",
        "fünfzigmal§padesátkrát§Sie hat das Lied fünfzigmal gehört.§Píseň slyšela padesátkrát.",
        "hundertmal§stokrát§Ich habe dir das hundertmal gesagt.§Řekl jsem ti to stokrát.",
        "tausendmal§tisíckrát§Dieses Bild wurde tausendmal geteilt.§Tento obrázek byl sdílen tisíckrát."
    ),
    wordToolGroup("prepositions", "Předložky", "Místo, směr, čas a vztahy",
        "an§na / u§Ich warte an der Haltestelle.§Čekám na zastávce.",
        "auf§na§Das Buch liegt auf dem Tisch.§Kniha leží na stole.",
        "aus§z / ze§Ich komme aus Tschechien.§Pocházím z Česka.",
        "bei§u / při§Ich wohne bei meinen Eltern.§Bydlím u rodičů.",
        "bis§do / až do§Der Laden ist bis acht Uhr offen.§Obchod má otevřeno do osmi hodin.",
        "durch§skrz§Wir gehen durch den Park.§Jdeme parkem.",
        "für§pro§Das Geschenk ist für dich.§Tento dárek je pro tebe.",
        "gegen§proti§Wir spielen heute gegen die andere Mannschaft.§Dnes hrajeme proti druhému týmu.",
        "hinter§za§Das Fahrrad steht hinter dem Haus.§Kolo stojí za domem.",
        "in§v / do§Ich wohne in Brünn.§Bydlím v Brně.",
        "mit§s / pomocí§Ich fahre mit dem Bus.§Jedu autobusem.",
        "nach§po / do§Nach der Arbeit gehe ich nach Hause.§Po práci jdu domů.",
        "neben§vedle§Die Tasche steht neben dem Stuhl.§Taška stojí vedle židle.",
        "ohne§bez§Ich trinke Kaffee ohne Zucker.§Piji kávu bez cukru.",
        "seit§od (časově)§Ich lerne seit einem Jahr Deutsch.§Učím se německy už rok.",
        "über§nad / o§Wir sprechen über den Film.§Mluvíme o filmu.",
        "um§v (čas) / kolem§Der Unterricht beginnt um neun Uhr.§Výuka začíná v devět hodin.",
        "unter§pod§Die Katze schläft unter dem Tisch.§Kočka spí pod stolem.",
        "vor§před§Ich warte vor dem Kino.§Čekám před kinem.",
        "von§od / z§Der Brief ist von meiner Schwester.§Dopis je od mé sestry.",
        "zu§k / do§Ich gehe zu Fuß zur Arbeit.§Jdu do práce pěšky.",
        "zwischen§mezi§Der Supermarkt liegt zwischen der Bank und der Apotheke.§Supermarket leží mezi bankou a lékárnou.",
        "gegenüber§naproti§Die Bank liegt gegenüber dem Bahnhof.§Banka leží naproti nádraží.",
        "entlang§podél§Wir gehen den Fluss entlang.§Jdeme podél řeky.",
        "trotz§navzdory§Trotz des Regens gehen wir spazieren.§Navzdory dešti jdeme na procházku."
    ),
    wordToolGroup("conjunctions", "Spojky a spojovací výrazy", "Spojuj slova, myšlenky a celé věty",
        "und§a§Ich trinke Tee und lese ein Buch.§Piji čaj a čtu knihu.",
        "oder§nebo§Möchtest du Tee oder Kaffee?§Chceš čaj, nebo kávu?",
        "aber§ale§Ich bin müde, aber ich arbeite weiter.§Jsem unavený, ale pracuji dál.",
        "denn§neboť / protože§Ich bleibe zu Hause, denn ich bin krank.§Zůstávám doma, protože jsem nemocný.",
        "sondern§nýbrž§Ich trinke keinen Kaffee, sondern Tee.§Nepiji kávu, nýbrž čaj.",
        "weil§protože§Ich lerne Deutsch, weil ich in Wien arbeite.§Učím se německy, protože pracuji ve Vídni.",
        "dass§že§Ich weiß, dass du heute kommst.§Vím, že dnes přijdeš.",
        "wenn§když / pokud§Wenn es regnet, bleibe ich zu Hause.§Když prší, zůstávám doma.",
        "obwohl§ačkoli§Obwohl ich müde bin, mache ich einen Spaziergang.§Ačkoli jsem unavený, jdu na procházku.",
        "damit§aby§Ich spreche langsam, damit du mich verstehst.§Mluvím pomalu, abys mi rozuměl.",
        "deshalb§proto§Der Zug hat Verspätung, deshalb warte ich.§Vlak má zpoždění, proto čekám.",
        "trotzdem§přesto§Es regnet, trotzdem gehen wir spazieren.§Prší, přesto jdeme na procházku.",
        "während§zatímco / během§Ich höre Musik, während ich koche.§Poslouchám hudbu, zatímco vařím.",
        "bevor§než§Bevor ich schlafe, lese ich.§Než jdu spát, čtu si.",
        "nachdem§poté co§Nachdem wir gegessen haben, gehen wir raus.§Poté co jsme se najedli, jdeme ven.",
        "sobald§jakmile§Sobald ich Zeit habe, rufe ich dich an.§Jakmile budu mít čas, zavolám ti.",
        "falls§v případě, že§Falls du Hilfe brauchst, ruf mich an.§Pokud budeš potřebovat pomoc, zavolej mi.",
        "ob§zda§Ich weiß nicht, ob der Bus schon kommt.§Nevím, zda už autobus přijíždí.",
        "als§když (v minulosti)§Als ich ein Kind war, spielte ich draußen.§Když jsem byl dítě, hrál jsem si venku.",
        "entweder§buď§Entweder fahren wir mit dem Zug oder mit dem Bus.§Pojedeme buď vlakem, nebo autobusem.",
        "weder§ani§Er trinkt weder Kaffee noch Tee.§Nepije ani kávu, ani čaj.",
        "sowie§a také / jakož i§Wir brauchen Brot sowie Milch.§Potřebujeme chléb a také mléko.",
        "daher§proto / z toho důvodu§Der Termin wurde verschoben, daher komme ich später.§Termín byl posunut, proto přijdu později.",
        "dennoch§přesto / nicméně§Es ist kalt, dennoch gehe ich joggen.§Je zima, přesto jdu běhat.",
        "also§takže / tedy§Ich bin fertig, also können wir gehen.§Jsem hotový, takže můžeme jít."
    ),
    wordToolGroup(
        "possessives",
        "Přivlastňovací zájmena",
        "mein, dein, sein, ihr… Nauč se, jak se koncovka mění podle rodu, čísla a pádu.",
        "mein§můj (mužský rod, 1. pád)§Mein Bruder wohnt in Brünn.§Můj bratr bydlí v Brně.",
        "meine§moje (ženský rod, 1. pád)§Meine Schwester lernt Deutsch.§Moje sestra se učí německy.",
        "mein§moje (střední rod, 1. pád)§Mein Kind spielt im Garten.§Moje dítě si hraje na zahradě.",
        "meine§moji (množné číslo, 1. pád)§Meine Freunde kommen heute.§Moji přátelé dnes přijdou.",
        "meinen§svého (mužský rod, 4. pád)§Ich besuche meinen Bruder.§Navštívím svého bratra.",
        "meinem§svému (mužský rod, 3. pád)§Ich helfe meinem Vater.§Pomáhám svému otci.",
        "meiner§své (ženský rod, 3. pád)§Ich helfe meiner Mutter.§Pomáhám své matce.",
        "meinen§svým (množné číslo, 3. pád)§Ich schreibe meinen Freunden.§Píšu svým přátelům.",
        "dein§tvůj (mužský rod, 1. pád)§Dein Vater wohnt in Brünn.§Tvůj otec bydlí v Brně.",
        "deine§tvoje (ženský rod, 1. pád)§Deine Tasche liegt hier.§Tvoje taška leží tady.",
        "dein§tvoje (střední rod, 1. pád)§Dein Kind liest ein Buch.§Tvoje dítě čte knihu.",
        "deinen§tvého (mužský rod, 4. pád)§Ich kenne deinen Bruder.§Znám tvého bratra.",
        "deinem§tvému (mužský rod, 3. pád)§Ich helfe deinem Vater.§Pomáhám tvému otci.",
        "deiner§tvé (ženský rod, 3. pád)§Ich helfe deiner Schwester.§Pomáhám tvé sestře.",
        "sein§jeho (mužský rod, 1. pád)§Sein Bruder arbeitet heute.§Jeho bratr dnes pracuje.",
        "seine§jeho (ženský rod, 1. pád)§Seine Schwester kommt später.§Jeho sestra přijde později.",
        "ihr§její (mužský rod, 1. pád)§Das ist ihr Bruder.§To je její bratr.",
        "seinem§svému (mužský rod, 3. pád)§Er hilft seinem Vater.§Pomáhá svému otci.",
        "ihren§jejího (mužský rod, 4. pád)§Peter besucht ihren Vater.§Petr navštěvuje jejího otce.",
        "ihre§její (ženský rod, 1. pád)§Ihre Mutter lebt in Wien.§Její matka žije ve Vídni.",
        "ihre§její (množné číslo, 1. pád)§Ihre Freunde kommen morgen.§Její přátelé přijedou zítra.",
        "unser§náš (mužský rod, 1. pád)§Unser Lehrer spricht langsam.§Náš učitel mluví pomalu.",
        "unsere§naše (ženský rod, 1. pád)§Unsere Schule ist groß.§Naše škola je velká.",
        "euer§váš (mužský rod, 1. pád)§Euer Hund ist freundlich.§Váš pes je přátelský.",
        "Ihre§Vaše (zdvořilé oslovení, 4. pád)§Ich habe Ihre Tasche gefunden.§Našel/Našla jsem Vaši tašku."
    ),

    wordToolGroup("adverbs", "Příslovce", "Čas, četnost, místo a způsob",
        "heute§dnes§Heute arbeite ich von zu Hause.§Dnes pracuji z domova.",
        "gestern§včera§Gestern habe ich lange gearbeitet.§Včera jsem dlouho pracoval.",
        "morgen§zítra§Morgen fahre ich nach Berlin.§Zítra jedu do Berlína.",
        "jetzt§teď§Jetzt habe ich leider keine Zeit.§Teď bohužel nemám čas.",
        "später§později§Ich rufe dich später zurück.§Zavolám ti později zpět.",
        "früh§brzy / časně§Am Samstag stehe ich früh auf.§V sobotu vstávám brzy.",
        "bald§brzy§Der Zug kommt bald an.§Vlak brzy přijede.",
        "immer§vždy§Ich trinke immer Wasser zum Essen.§K jídlu vždy piji vodu.",
        "oft§často§Wir gehen oft zusammen spazieren.§Často chodíme společně na procházku.",
        "manchmal§někdy§Manchmal koche ich für meine Freunde.§Někdy vařím pro své přátele.",
        "selten§zřídka§Ich esse selten Fast Food.§Zřídka jím rychlé občerstvení.",
        "nie§nikdy§Ich komme nie zu spät.§Nikdy nechodím pozdě.",
        "hier§tady§Hier findest du den Eingang.§Tady najdeš vchod.",
        "dort§tam§Dort wartet meine Schwester.§Tam čeká moje sestra.",
        "draußen§venku§Die Kinder spielen draußen.§Děti si hrají venku.",
        "drinnen§uvnitř§Drinnen ist es warm.§Uvnitř je teplo.",
        "gern§rád / ráda§Ich lese gern am Abend.§Rád večer čtu.",
        "schnell§rychle§Bitte sprich nicht so schnell.§Prosím, nemluv tak rychle.",
        "langsam§pomalu§Der Bus fährt heute langsam.§Autobus dnes jede pomalu.",
        "vielleicht§možná§Vielleicht besuchen wir morgen ein Museum.§Možná zítra navštívíme muzeum.",
        "zuerst§nejprve§Zuerst frühstücke ich, danach gehe ich zur Arbeit.§Nejprve snídám, potom jdu do práce.",
        "danach§potom / poté§Danach rufe ich meine Mutter an.§Potom zavolám mamince.",
        "schließlich§nakonec§Schließlich finden wir den richtigen Weg.§Nakonec najdeme správnou cestu.",
        "normalerweise§obvykle§Normalerweise fahre ich mit dem Bus.§Obvykle jezdím autobusem.",
        "besonders§zvláště§Ich mag besonders die deutsche Sprache.§Zvláště se mi líbí němčina."
    )
)

private fun arabicNumeral(germanNumber: String): String = when (germanNumber) {
    "null" -> "0"
    "eins" -> "1"
    "zwei" -> "2"
    "drei" -> "3"
    "vier" -> "4"
    "fünf" -> "5"
    "sechs" -> "6"
    "sieben" -> "7"
    "acht" -> "8"
    "neun" -> "9"
    "zehn" -> "10"
    "elf" -> "11"
    "zwölf" -> "12"
    "dreizehn" -> "13"
    "vierzehn" -> "14"
    "fünfzehn" -> "15"
    "sechzehn" -> "16"
    "siebzehn" -> "17"
    "achtzehn" -> "18"
    "neunzehn" -> "19"
    "zwanzig" -> "20"
    "dreißig" -> "30"
    "fünfzig" -> "50"
    "hundert" -> "100"
    "tausend" -> "1 000"
    else -> germanNumber
}

private fun displayedWordToolAnswer(item: WordToolItem, modeId: String): String = when (modeId) {
    "words" -> item.czech
    "sentences" -> item.german
    else -> item.sentenceCzech
}

internal fun makeWordToolQuestions(group: WordToolGroup, mode: WordToolMode): List<WordToolQuestion> {
    val source = group.items
    return source.shuffled().map { item ->
        val correctAnswerKey = displayedWordToolAnswer(item, mode.id).trim().lowercase()
        val distractors = source.shuffled()
            .filter { it.german != item.german }
            .distinctBy { displayedWordToolAnswer(it, mode.id).trim().lowercase() }
            .filter { displayedWordToolAnswer(it, mode.id).trim().lowercase() != correctAnswerKey }
            .take(3)
        val options = (listOf(item) + distractors).shuffled()
        check(options.size == 4) { "Expected exactly four unique choices for ${group.id}/${mode.id}/${item.german}" }
        check(options.map { displayedWordToolAnswer(it, mode.id).trim().lowercase() }.toSet().size == 4) {
            "Duplicate answer choice for ${group.id}/${mode.id}/${item.german}"
        }
        when (mode.id) {
            "words" -> {
                val choices = options.map { it.czech }
                WordToolQuestion(
                    id = stableReviewId("wordtools", group.id, mode.id, item.german, item.sentenceGerman),
                    prompt = if (group.id == "possessives") {
                        "Jaký význam má tvar „${item.german}“ v této větě?\n${item.sentenceGerman}"
                    } else {
                        "Co znamená německý výraz „${item.german}“?"
                    },
                    choices = choices,
                    correctIndex = choices.indexOf(item.czech),
                    fullGerman = item.german,
                    fullCzech = item.czech,
                    explanation = if (group.id == "possessives") {
                        "Koncovka přivlastňovacího zájmena se řídí rodem, číslem a pádem podstatného jména. Sleduj celý příklad, nejen český překlad."
                    } else {
                        "Uč se výraz společně s jeho významem a příkladem použití."
                    }
                )
            }
            "sentences" -> {
                val pattern = Regex("(?i)(?<!\\p{L})" + Regex.escape(item.german) + "(?!\\p{L})")
                val sentenceWithBlank = pattern.replaceFirst(item.sentenceGerman, "_____")
                val choices = options.map { it.german }
                val numeral = arabicNumeral(item.german)
                val sentencePrompt = when {
                    group.id == "numbers" ->
                        "Jak se německy řekne číslo $numeral?\n$sentenceWithBlank"
                    group.id == "ordinal-numbers" ->
                        "Jak se německy řekne řadová číslovka „${item.czech}“?\n$sentenceWithBlank"
                    group.id == "multiplicative-numbers" ->
                        "Jak se německy řekne „${item.czech}“?\n$sentenceWithBlank"
                    group.id == "possessives" ->
                        "Doplň správný tvar podle věty a českého významu:\n$sentenceWithBlank\nČesky: ${item.sentenceCzech}"
                    else ->
                        "Vyber výraz, který správně doplní chybějící místo ve větě:\n$sentenceWithBlank"
                }
                WordToolQuestion(
                    id = stableReviewId("wordtools", group.id, mode.id, item.german, item.sentenceGerman),
                    prompt = sentencePrompt,
                    choices = choices,
                    correctIndex = choices.indexOf(item.german),
                    fullGerman = item.sentenceGerman,
                    fullCzech = item.sentenceCzech,
                    explanation = if (group.id == "possessives") "Tvar „${item.german}“ odpovídá rodu, číslu a pádu podstatného jména ve větě." else "Výraz „${item.german}“ zde znamená „${item.czech}“."
                )
            }
            else -> {
                val german = item.sentenceGerman
                val czech = item.sentenceCzech
                val choices = options.map { it.sentenceCzech }
                WordToolQuestion(
                    id = stableReviewId("wordtools", group.id, mode.id, item.german, item.sentenceGerman),
                    prompt = "Vyber správný český překlad celé věty:\n" + german,
                    choices = choices,
                    correctIndex = choices.indexOf(czech),
                    fullGerman = german,
                    fullCzech = czech,
                    explanation = if (group.id == "possessives") "Všimni si koncovky přivlastňovacího výrazu. Záleží na rodu, čísle a pádu podstatného jména." else "Zkus si přeložit celou větu a všímej si výrazu „" + item.german + "“ v kontextu."
                )
            }
        }
    }
}

@Composable
internal fun WordToolsScreen(
    onBack: () -> Unit,
    onComplete: (score: Int, total: Int, activityKey: String) -> Unit
) {
    val context = LocalContext.current
    var selectedGroup by remember { mutableStateOf<WordToolGroup?>(null) }
    var selectedMode by remember { mutableStateOf<WordToolMode?>(null) }
    var questions by remember { mutableStateOf<List<WordToolQuestion>>(emptyList()) }
    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var wrongQuestions by remember { mutableStateOf<List<WordToolQuestion>>(emptyList()) }
    var isErrorReview by remember { mutableStateOf(false) }

    fun startMode(mode: WordToolMode) {
        val group = selectedGroup ?: return
        selectedMode = mode
        questions = makeWordToolQuestions(group, mode)
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
        wrongQuestions = emptyList()
        isErrorReview = false
    }

    fun repeatWrongQuestions() {
        if (wrongQuestions.isEmpty()) return
        questions = wrongQuestions.shuffled()
        wrongQuestions = emptyList()
        questionIndex = 0
        selectedAnswer = null
        score = 0
        finished = false
        isErrorReview = true
    }

    fun goBackOneLevel() {
        when {
            selectedMode != null -> {
                selectedMode = null
                questions = emptyList()
                selectedAnswer = null
                finished = false
            }
            selectedGroup != null -> selectedGroup = null
            else -> onBack()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { goBackOneLevel() },
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text(
                        selectedMode?.title ?: selectedGroup?.title ?: "Další slovní druhy",
                        color = Muted,
                        fontSize = 13.sp
                    )
                }
            }

            when {
                selectedMode == null && selectedGroup == null -> {
                    Text("Co chceš procvičit?", color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "Vyber oblast a pak způsob procvičení. Každá sada má 25 promíchaných otázek.",
                        color = Muted, fontSize = 14.sp, lineHeight = 21.sp
                    )
                    wordToolGroups.forEach { group ->
                        Card(
                            onClick = { selectedGroup = group },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(46.dp).background(Mint, RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) { Icon(Icons.Default.Translate, contentDescription = null, tint = ForestText) }
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(group.title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                                    Text(group.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 18.sp)
                                    Text("25 výrazů · 3 způsoby procvičení", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    LatestResultLine(CourseResultStore.load(context, "wordtools_${group.id}"))
                                }
                            }
                        }
                    }
                }
                selectedMode == null && selectedGroup != null -> {
                    Text(selectedGroup!!.title, color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text(selectedGroup!!.subtitle, color = Muted, fontSize = 14.sp, lineHeight = 21.sp)
                    if (selectedGroup!!.id == "possessives") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Mint)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                Text("Rychlý přehled koncovek", color = ForestText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("1. pád · kdo/co", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("mein Vater · meine Mutter · mein Kind · meine Freunde", color = Ink, fontSize = 14.sp, lineHeight = 20.sp)
                                Text("4. pád · koho/co", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("meinen Vater · meine Mutter · mein Kind · meine Freunde", color = Ink, fontSize = 14.sp, lineHeight = 20.sp)
                                Text("3. pád · komu/čemu", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("meinem Vater · meiner Mutter · meinem Kind · meinen Freunden", color = Ink, fontSize = 14.sp, lineHeight = 20.sp)
                                Text(
                                    "Nejdřív určuj pád a rod/číslo podstatného jména. Podle nich vybereš koncovku přivlastňovacího zájmena.",
                                    color = ForestText,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                    wordToolModes.forEach { mode ->
                        Card(
                            onClick = { startMode(mode) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(mode.title, color = ForestText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                Text(mode.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                                Text("25 otázek", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                LatestResultLine(CourseResultStore.load(context, "wordtools_${selectedGroup!!.id}_${mode.id}"))
                            }
                        }
                    }
                }
                else -> {
                    val group = selectedGroup!!
                    val mode = selectedMode!!
                    val question = questions.getOrNull(questionIndex)
                    Text(group.subtitle, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                    if (finished) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Forest)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(if (isErrorReview) "Opakování chyb dokončeno" else "Sada dokončena", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                                Text("$score z ${questions.size} správně", color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    if (isErrorReview) {
                                        if (wrongQuestions.isEmpty()) "Skvělé! V tomto kole už nezůstala žádná chybná odpověď."
                                        else "Některé odpovědi ještě nejsou správně. Můžeš je zopakovat znovu."
                                    } else {
                                        "Při opakování se otázky zamíchají a můžeš si znovu procvičit celé věty."
                                    },
                                    color = Color(0xFFE0EAE5), fontSize = 14.sp, lineHeight = 21.sp
                                )
                            }
                        }
                        if (wrongQuestions.isNotEmpty()) {
                            Button(
                                onClick = { repeatWrongQuestions() },
                                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                                shape = RoundedCornerShape(14.dp)
                            ) { Text("Opakovat jen chyby (${wrongQuestions.size})", fontWeight = FontWeight.Bold) }
                        }
                        Button(
                            onClick = { startMode(mode) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text("Zopakovat — zamíchat otázky", fontWeight = FontWeight.Bold) }
                        Button(
                            onClick = { selectedMode = null; selectedAnswer = null; finished = false },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text("Vybrat jiný způsob procvičení", fontWeight = FontWeight.Bold) }
                    } else if (question != null) {
                        Text("OTÁZKA ${questionIndex + 1} Z ${questions.size}", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        LinearProgressIndicator(
                            progress = { (questionIndex + 1).toFloat() / questions.size.toFloat() },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface)
                        ) {
                            Column(modifier = Modifier.padding(21.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(question.prompt, color = Ink, fontSize = 20.sp, lineHeight = 29.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        question.choices.forEachIndexed { index, choice ->
                            val bg = when {
                                selectedAnswer == null -> OptionSurface
                                index == question.correctIndex -> Correct
                                index == selectedAnswer -> Incorrect
                                else -> OptionSurface
                            }
                            Card(
                                onClick = {
                                    if (selectedAnswer == null) {
                                        selectedAnswer = index
                                        val correct = index == question.correctIndex
                                        LearningReviewStore.recordAnswer(
                                            context = context,
                                            question = question.toReviewQuestion(group, mode),
                                            wasCorrect = correct,
                                            isMistakeReview = isErrorReview
                                        )
                                        if (correct) {
                                            score += 1
                                        } else {
                                            wrongQuestions = wrongQuestions + question
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(15.dp),
                                colors = CardDefaults.cardColors(containerColor = bg)
                            ) {
                                Text(
                                    choice, modifier = Modifier.fillMaxWidth().padding(17.dp),
                                    color = Ink, fontSize = 16.sp,
                                    fontWeight = if (selectedAnswer != null && index == question.correctIndex) FontWeight.Bold else FontWeight.Medium,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                        if (selectedAnswer != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = if (selectedAnswer == question.correctIndex) Correct else Incorrect)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                                    Text(
                                        if (selectedAnswer == question.correctIndex) "Správně! Gut gemacht!" else "Správná odpověď: ${question.choices[question.correctIndex]}",
                                        color = ForestText, fontWeight = FontWeight.Bold, fontSize = 16.sp
                                    )
                                    Text("Celá německá věta:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(question.fullGerman, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 23.sp)
                                    Text("Český překlad:", color = ForestText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(question.fullCzech, color = Ink, fontSize = 15.sp, lineHeight = 22.sp)
                                    Text(question.explanation, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
                                }
                            }
                            Button(
                                onClick = {
                                    if (questionIndex < questions.lastIndex) {
                                        questionIndex += 1
                                        selectedAnswer = null
                                    } else {
                                        if (!isErrorReview) {
                                            CourseResultStore.save(context, "wordtools_${group.id}_${mode.id}", score, questions.size)
                                            CourseResultStore.save(context, "wordtools_${group.id}", score, questions.size)
                                            onComplete(score, questions.size, "wordtools_${group.id}_${mode.id}")
                                        }
                                        finished = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(if (questionIndex == questions.lastIndex) "Zobrazit výsledek" else "Další otázka", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(
                                "Vyber odpověď a říkej si celou německou větu nahlas.",
                                color = Muted, fontSize = 13.sp, lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.heightIn(min = 4.dp))
        }
    }
}
