# Лаборатори №10: Өгөгдлийн урсгалын тест — Definition-Use хосууд

**Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест (2026)
**Нэр:** М.Алтан-Од
**Оюутны код:** B222270023

## Ажлын орчин

~~~
openjdk version "17.0.21.0.3" 2026-10-24
OpenJDK Runtime Environment (build 17.0.21.0.3+1-1-26.04.1-Ubuntu)
OpenJDK 64-Bit Server VM (build 17.0.21.0.3+1-1-26.04.1-Ubuntu, mixed mode, sharing)
Apache Maven 3.9.12
Maven home: /usr/share/maven
Java version: 17.0.21.0.3, vendor: Ubuntu, runtime: /usr/lib/jvm/java-17-openjdk-amd64
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "6.18.58.1-microsoft-standard-wsl2", arch: "amd64", family: "unix"
~~~

## Төслийн бүтэц

- `src/main/java/mn/edu/must/sqat/DataFlowTargets.java` — `gcd`, `bonus` (кодыг өөрчлөөгүй, starter-тэй ижил)
- `src/test/java/mn/edu/must/sqat/GcdTest.java`, `BonusTest.java` — тестүүд
- `cfg-bonus.png` — `bonus()`-ийн CFG
- `results/` — `mvn-verify-alldefs.txt`, `jacoco-alldefs.csv`, `mvn-test-M1.txt`, `mvn-test-M2.txt`, `mvn-test-M2-kill.txt`, `mvn-test-M3.txt`, `mvn-verify-final.txt`, `jacoco-final.csv`

## Хэсэг A: gcd

Def: `x`@13, `y`@13 (параметр), `tmp`@16, `x`@17, `y`@18. Use: `y`@15 (p), `x`@16 ба `y`@16 (c), `y`@17 (c), `tmp`@18 (c), `x`@20 (c). Лекцийн дугаарлалтыг файлын мөр рүү хөрвүүлсэн (1→13, 3→15, 4→16, 5→17, 6→18, 8→20).

| # | Хувьсагч | Def | Use | c/p | Def-clear зам | Тест | Хамрагдсан |
|---|---|---|---|---|---|---|---|
| 1 | x | 13 | 16 | c | 13→15→16 | T2 | тийм |
| 2 | x | 13 | 20 | c | 13→15→20 | T1 | тийм |
| 3 | x | 17 | 16 | c | 17→18→15→16 | T2 | тийм |
| 4 | x | 17 | 20 | c | 17→18→15→20 | T2 | тийм |
| 5 | y | 13 | 15 (T, →16) | p | 13→15 | T2 | тийм |
| 6 | y | 13 | 15 (F, →20) | p | 13→15 | T1 | тийм |
| 7 | y | 13 | 16 | c | 13→15→16 | T2 | тийм |
| 8 | y | 13 | 17 | c | 13→15→16→17 | T2 | тийм |
| 9 | y | 18 | 15 (T) | p | 18→15 | T2 | тийм |
| 10 | y | 18 | 15 (F) | p | 18→15 | T2 | тийм |
| 11 | y | 18 | 16 | c | 18→15→16 | T2 | тийм |
| 12 | y | 18 | 17 | c | 18→15→16→17 | T2 | тийм |
| 13 | tmp | 16 | 18 | c | 16→17→18 | T2 | тийм |

Тестүүд (`GcdTest`, `results/mvn-verify-alldefs.txt` дахь `GcdTest` — `Tests run: 2`):

- **T1** `gcd_7_0_skipsLoop` — `gcd(7,0)`: давталт 0 удаа, ганцаараа #2 ба #6-г хамарна.
- **T2** `gcd_12_8_twoIterations` — `gcd(12,8)`: давталт 2 удаа (12%8=4, 8%4=0); хоёр дахь давталт нь `x`@17 ба `y`@18-аас эхэлсэн хосуудыг (#3, #4, #9–#12) хамарна.

## Хэсэг B: bonus

### B1. CFG
`cfg-bonus.png` — зангилаа бүрт мөрийн дугаар, ирмэг бүрт тестийн метод нэр бичсэн.

### B2. Def/Use жагсаалт

| Мөр | Def | Use |
|---|---|---|
| 31 | `attendance`, `threshold` | |
| 32 | | p: `attendance`, `threshold` |
| 35 / 36 / 37 | `present` / `streak` / `best` | |
| 38 | `a` (давталт бүрт) | p: `attendance` (дараагийн элемент байгаа эсэх) |
| 39 | | p: `a` |
| 40 | `present` | c: `present` |
| 41 | `streak` | c: `streak` |
| 42 | | p: `streak`, `best` |
| 43 | `best` | c: `streak` |
| 46 | `streak` (kill) | |
| 49 / 51 / 54 | `score` | 54: c: `score` |
| 50 | | p: `present`, `threshold` |
| 53 | | p: `best` |
| 56 | | c: `score` |

### B3. DU хүснэгт

Тестийн ID-ууд: BT1 `bonus_streakWithBreak_is8`, BT2 `bonus_belowThreshold_is0`, BT3 `bonus_nullAttendance_throws`, BT4 `bonus_negativeThreshold_throws`, BT5 `bonus_emptyThresholdZero_is5`, BT6 `bonus_allAbsent_is0`, BT7 `bonus_streakBelowThreshold_is3`, BT8 `bonus_twoShortStreaks_is5` (M2-ийг унагах тест).

| # | Хувьсагч | Def→Use | c/p | Def-clear зам | Тест |
|---|---|---|---|---|---|
| 1 | attendance | 31→32 (T) | p | 31→32 | BT3 |
| 2 | attendance | 31→32 (F) | p | 31→32 | BT1 |
| 3 | attendance | 31→38 (T) | p | 31→32→35→36→37→38 | BT1 |
| 4 | attendance | 31→38 (F) | p | 31→32→35→36→37→38 | BT5 |
| 5 | threshold | 31→32 (T) | p | 31→32 | BT4 |
| 6 | threshold | 31→32 (F) | p | 31→32 | BT1 |
| 7 | threshold | 31→50 (T) | p | 31→…→50 | BT1 |
| 8 | threshold | 31→50 (F) | p | 31→…→50 | BT2 |
| 9 | a | 38→39 (T) | p | 38→39 | BT1 |
| 10 | a | 38→39 (F) | p | 38→39 | BT1 |
| 11 | present | 35→40 | c | 35→36→37→38→39→40 | BT1 |
| 12 | present | 35→50 (T) | p | 35→…→38→49→50 | BT5 |
| 13 | present | 35→50 (F) | p | 35→…→38→49→50 | BT6 |
| 14 | present | 40→40 | c | 40→41→42→38→39→40 | BT1 |
| 15 | present | 40→50 (T) | p | 40→…→38→49→50 | BT1 |
| 16 | present | 40→50 (F) | p | 40→…→38→49→50 | BT2 |
| 17 | streak | 36→41 | c | 36→37→38→39→40→41 | BT1 |
| 18 | streak | 41→41 | c | 41→42→38→39→40→41 | BT1 |
| 19 | streak | 46→41 | c | 46→38→39→40→41 | BT1 |
| 20 | streak | 41→42 (T) | p | 41→42 | BT1 |
| 21 | streak | 41→42 (F) | p | 41→42 | BT1 |
| 22 | streak | 41→43 | c | 41→42→43 | BT1 |
| 23 | best | 37→42 (T) | p | 37→…→41→42 | BT1 |
| 24 | best | 37→42 (F) | p | — | **биелэшгүй** |
| 25 | best | 37→53 (T) | p | — | **биелэшгүй** |
| 26 | best | 37→53 (F) | p | 37→38→…→49→50→53 | BT5 |
| 27 | best | 43→42 (T) | p | 43→38→39→40→41→42 | BT1 |
| 28 | best | 43→42 (F) | p | 43→38→39→40→41→42 | BT1 |
| 29 | best | 43→53 (T) | p | 43→38→49→50→53 | BT1 |
| 30 | best | 43→53 (F) | p | 43→38→49→50→53 | BT2 |
| 31 | score | 49→54 | c | 49→50→53→54 | BT7 |
| 32 | score | 49→56 | c | 49→50→53→56 | BT2 |
| 33 | score | 51→54 | c | 51→53→54 | BT1 |
| 34 | score | 51→56 | c | 51→53→56 | BT5 |
| 35 | score | 54→56 | c | 54→56 | BT1 |

**Kill:** мөр 46 (`streak = 0`) нь `streak`@36 ба `streak`@41-ийг алддаг. Иймд `streak`@36 ба `streak`@46 нь мөр 42, 43-т хүрэхгүй: тэдгээр рүү очих бүх зам мөр 41-ийг (`streak`-ийг дахин тодорхойлдог) дайрдаг тул def-clear зам байхгүй, эдгээр хослолыг DU хос гэж тоолоогүй.

**Биелэшгүй хосууд:** #24 — `best` мөр 37-ийн утгаараа (0) байх үед анхны ирсэн өдөр `streak` = 1 болдог тул `streak > best` үргэлж үнэн, F ирмэг боломжгүй. #25 — тэр үед `best` = 0 тул `best >= 5` үргэлж худал, T ирмэг боломжгүй.

### B4. All-defs ба all-DU-pairs

**All-defs: 2 тест (BT1, BT2).** `score` нь мөр 49, 51, 54-д тодорхойлогддог. Мөр 49-ийн def зөвхөн мөр 50 худал болсон үед use-д хүрдэг (үнэн бол мөр 51 түүнийг алддаг), мөр 51-ийн def зөвхөн үнэн үед үүснэ. Иймд нэг гүйцэтгэлд хоёуланг use-д хүргэх боломжгүй, 1 тест хүрэлцэхгүй. BT1 (`[1,1,1,1,1,0,1]`, 3 → 8) үнэн замаар `streak`@46 зэрэг бусад бүх def-ийг, BT2 (`[1]`, 2 → 0) худал замаар `score`@49-ийг use-д хүргэнэ. `results/mvn-verify-alldefs.txt`: `Tests run: 4` (gcd 2 + bonus 2), `Failures: 0`.

**All-DU-pairs:** BT3–BT7 нэмсэн (тусдаа commit), дараа нь Хэсэг C-д BT8 нэмэгдсэн. `bonus` нийт 8 тест, төсөл нийт 10 тест.

### B5. JaCoCo ба branch vs DU

| Файл | Branch | Line |
|---|---|---|
| `results/jacoco-alldefs.csv` | 14 / 16 (BRANCH_MISSED = 2) | 22 / 24 (LINE_MISSED = 2) |
| `results/jacoco-final.csv` | **16 / 16 (BRANCH_MISSED = 0)** | 24 / 25 (LINE_MISSED = 1) |

`jacoco-alldefs.csv`-д all-defs хангагдсан атал branch 100% биш (мөр 32-ийн `throw` салаанууд хамрагдаагүй). Эцсийн `LINE_MISSED = 1` нь мөр 7 — Java-гийн далд constructor.

Branch 100% болсон мөчид DU хос хамрагдаагүй үлдэж болно: BT1–BT6 аль хэдийн салаа бүрийг (мөр 32, 38, 39, 42, 50, 53-ын T/F) хамарсан тул branch 100%, гэвч #31 (`score`@49→54) хос зөвхөн BT7-аар хамрагддаг. Энэ хос нь "ирсэн өдөр босгоос бага, гэхдээ streak ≥ 5" гэсэн тохиолдол бөгөөд BT1–BT6 дотор байхгүй.

## Хэсэг C: Fault seeding

| № | Мутаци | Төрөл | Үр дүн | Унасан тест |
|---|---|---|---|---|
| M1 | мөр 41 `streak = streak + 1` устгасан | Missing update | `Tests run: 9, Failures: 2`, BUILD FAILURE (`results/mvn-test-M1.txt`) | `bonus_streakWithBreak_is8` (`expected: <8> but was: <5>`), `bonus_streakBelowThreshold_is3` (`expected: <3> but was: <0>`) |
| M2 | мөр 43 `best = streak` → `best = present` | Wrong variable | **Амьд үлдсэн:** `Tests run: 9, Failures: 0`, BUILD SUCCESS (`results/mvn-test-M2.txt`) | — |
| M2 + BT8 | ижил мутаци | | `Tests run: 10, Failures: 1`, BUILD FAILURE (`results/mvn-test-M2-kill.txt`) | `bonus_twoShortStreaks_is5` (`expected: <5> but was: <8>`) |
| M3 | мөр 48-ийн дараа `int total = present + best;` | Dead definition | `Tests run: 10, Failures: 0`, BUILD SUCCESS (`results/mvn-test-M3.txt`) | — |

**M2 яагаад амьд үлдсэн бэ.** DU хос нь def-ээс use хүртэл ЗАМ хамрагдсаныг шаарддаг, утгыг биш. BT1, BT7-д `best` шинэчлэгдэх үед `present` ба `streak` үргэлж тэнцүү байсан (завсарлагагүй ирц, эсвэл завсарлагаас хойш `best` шинэчлэгдээгүй) тул буруу хувьсагч ижил утга өгсөн. BT8 (`[1,1,0,1,1,1,1]`, босго 3) нь завсарлагатай: жинхэнэ хамгийн урт цуваа 4 (< 5, бонусгүй → 5 оноо), харин мутант `best = present` нь `present` = 5 болоход `best`-ийг 5 болгож 8 оноо буцаасан.

**M3 яагаад алдаа гаргаагүй вэ.** `total` хаана ч хэрэглэгдэхгүй тул үр дүнд нөлөөлөхгүй, тестээр ялгагдахгүй, `javac`-ийн алдаа/анхааруулга ч байхгүй. Үүнийг тест биш статик шинжилгээ (IDE, SpotBugs, PMD) барина. VS Code-ийн анхааруулга: `The value of the local variable total is not used`

## Дүгнэлт

DU анализ нь branch хамралтаас илүү нарийн: branch бүх салааг дайрсан эсэхийг л хэмждэг бол DU нь утга хаана тодорхойлогдож хаана хэрэглэгдэхийг холбодог тул #31 шиг хосыг branch 100% болсон ч илрүүлдэг. Гэхдээ үнэ нь өндөр: 25 мөрийн `bonus` функцэд 35 хос гарч, def-clear зам, kill, биелэшгүй хос бүрийг гараар шалгах шаардлагатай болсон. M2 нь all-DU-pairs хамралт хүртэл хангасан тестийг давж амьд үлдсэн — хос нь зам хамарсныг баталдаг боловч утгыг ялгах баталгаа биш тул мутацийн шалгалт хэвээр хэрэгтэй. M3 нь тест огт унагаагүй бөгөөд зөвхөн статик шинжилгээ л үхсэн def-ийг барьдаг гэдгийг харуулсан. JaCoCo нь DU хамралтыг хэмждэггүй тул гараар хийсэн хүснэгт нь энэ лабын гол хэмжүүр байв. Хамгийн их цаг авсан нь `bonus`-ийн DU хүснэгт, ялангуяа `streak = 0`-ийн kill-ийг хянах ба биелэшгүй хосыг тодорхойлох алхам байлаа.

## AI-ийн хэрэглээ

AI туслах (Claude)-ийг DU хүснэгтийн анализ, тестүүдийн жишээ код, мутаци хийх командууд болон README-ийн эхийг бэлдэхэд ашигласан. Бүх тест, мутацийг өөрийн машин (WSL2) дээр ажиллуулж, гаралтыг `results/`-д хадгалсан; README дахь тоо бүр эдгээр файлтай таарна.
