# Nesine Web Test POC

nesine.com için Selenium + Cucumber test otomasyonu. Java 17, Maven, JUnit 5, PicoContainer, Allure.

## Senaryolar

- `popular_coupons.feature` (`@coupon`): case study. Popüler Kuponlar'dan bir kupon "Hemen Oyna" ile sepete
  eklenir, sepetteki etkinlik isim ve tarihleri kontrol edilir. Sonra header'dan İddaa, Canlı Sonuçlar ve Dakika
  Bahis sayfalarına gidilir; her sayfada doğru sayfanın açıldığı ve sepetin değişmediği kontrol edilir. 1., 2. ve 3.
  kuponla koşar.
- `popular_coupons_list.feature` (`@filter`): maç sayısı filtreleri sadece o sayıda maçı olan kuponları listeliyor mu.
  Filtreler ve beklenen maç sayıları `testdata/coupon_filters.csv` dosyasından okunuyor.
- `basket_replace.feature` (`@basket-replace`): sepet doluyken başka kupon oynanınca çıkan onay. "Tamam" sepeti yeni
  kuponla değiştirmeli, "Vazgeç" eskisini bırakmalı; sayfa yenilenince de aynı kalmalı.

## Çalıştırma

Gereken: JDK 17+, Maven, Chrome (driver'ı Selenium Manager indiriyor).

```bash
mvn clean test
```

Varsayılan koşum 3 Chrome penceresi açar ve yaklaşık 2 dakika sürer.

```bash
mvn clean test -Dheadless=true -Dthreads=1 -Dcucumber.filter.tags=@coupon
```

- -Dheadless=true: arayüzsüz Chrome (varsayılan false)
- `-Dthreads=N`: aynı anda koşan senaryo sayısı (varsayılan 3)
- `-Dcucumber.filter.tags=...`: tag ile seçim
- `-Dcucumber.filter.name=".*#1 .*"`: senaryo adıyla seçim (regex, adın tamamıyla eşleşmeli)
- `-Denv=staging`: hangi `config/<env>.properties` dosyasının okunacağı (varsayılan `prod`, pom.xml'de)

`staging.properties` içindeki adres yer tutucu; gerçek adres dosyaya yazılmalı ya da `-Dbase.url=...` ile verilmeli.
Dosyalardaki her değer `-D` ile ezilebilir (ör. `-Dtimeout.explicit.seconds=20`).
PowerShell'de noktalı parametreleri tırnağa almak gerekiyor: `"-Dcucumber.filter.tags=@coupon"`.

IDE'den `CucumberRunner` çalıştırılabilir.

Rapor için önce testlerin koşmuş olması gerekiyor, sonuçlar `target/allure-results`'a yazılıyor:

```bash
mvn allure:serve
```

## Docker

```bash
docker compose up --build --abort-on-container-exit --exit-code-from tests
```

Testler Maven container'ında, Chrome `selenium/standalone-chrome` container'ında koşar (`grid.url` verilince
`RemoteWebDriver` kullanılıyor). Sonuçlar host'taki `target/allure-results` klasörüne düşüyor, rapor aynı `mvn allure:serve`
komutuyla açılıyor.
## Mimari

```
src/test/java/com/nesine/poc
├── steps     Gherkin adımları ve assertion'lar
├── pages     BasePage, HomePage, PopularCouponsPage, BetSlip, HeaderMenu, ConfirmationDialog
├── context   TestContext: senaryoya ait driver ve oynanan kuponlar
├── model     CouponEvent, CouponCard, CouponFilter
├── config    ConfigReader (ortam), TestData (CSV)
├── driver    DriverFactory (yerel Chrome ya da Grid)
├── hooks     tarayıcı aç/kapat, hata screenshot'ı
└── runner    CucumberRunner
src/test/resources
├── features, testdata, config/<env>.properties
└── junit-platform.properties (Cucumber ve paralellik ayarları)
```

Akış: feature → steps → pages → driver. Step'ler ve sayfalar birbirini `TestContext` üzerinden tanıyor, hepsini Pico
constructor ile veriyor. Ortam ayarları `ConfigReader`'dan okunuyor.

## Tasarım

JUnit 5 Platform'u seçtim çünkü Cucumber'ın engine'i paralel koşumu sadece properties ile hallediyor, ayrıca runner
kodu gerekmiyor. Assertion'larda AssertJ kullandım; hata mesajlarında hangi kuponun düştüğünü yazmak ve soft assertion
için işime yaradı.

Paralellik `-Dthreads` ile ayarlanıyor, pom bu değeri hem `parallelism` hem `max-pool-size` olarak geçiriyor.
`max-pool-size` verilmezse ForkJoinPool istenenden fazla worker açabiliyor.

Sadece explicit wait kullandım, implicit wait ile karışınca bekleme süreleri öngörülemez oluyor. Sayfa yükleme stratejisi
bütün senaryolarda `EAGER`: reklam ve tracker'ları beklemenin faydası yok, kullanılan elemanlar zaten bekleniyor.

Ortak bekleme ve tıklama işleri `BasePage`'de. Sepet, header ve onay penceresi her sayfada olduğu için ayrı sınıflar.
Assertion'lar step'lerde. Site class isimlerini hash'lediği için locator'larda önce `data-testid` ve `id`'ye baktım,
olmayan yerde görünen metni kullandım.

Driver ve oynanan kuponlar `TestContext`'te duruyor, sayfalara ve step'lere Pico veriyor. Pico her senaryoya yeni bir
`TestContext` açtığı ve senaryo tek thread'de koştuğu için static ya da `ThreadLocal` driver kullanmadım. Her senaryo
kendi tarayıcısını açıyor, sepet de boş başlıyor.

Ortam bilgisi `config/<env>.properties`'te. Kupon sırası, path ve başlıklar feature dosyalarında, filtre senaryosunun
verisi ise tamamen `testdata/coupon_filters.csv`'de. Hangi dosyanın okunacağı `testdata.filters` ile değişiyor.
Cucumber Examples tablosunu dış dosyadan üretemediği için bu senaryo satırları tek tarayıcıda sırayla dönüyor, yani
filtreler kendi aralarında paralel değil. Bir filtre yanlışsa diğerleri de kontrol edilsin diye soft assertion kullandım.

## Bilinenler

- Canlı sitede ve o günün popüler kuponlarıyla koşuyor; kupon başlamış bir maç içerirse senaryo düşebilir.
- Retry yok.

## AI kullanımı

Geliştirme sürecinde siteyi incelerken ve kod yazarken Claude Code'dan yardım aldım. Neyin test edileceğine,
senaryolara ve neyin çıkarılacağına ben karar verdim, kodu sınıf sınıf gözden geçirip sadeleştirdim. Üretilen kodu
olduğu gibi teslim etmedim, burada yazan her kararı açıklayabilirim.
