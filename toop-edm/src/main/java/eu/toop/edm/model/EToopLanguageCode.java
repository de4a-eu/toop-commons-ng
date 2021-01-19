/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
 *
 * This work is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.edm.model;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.name.IHasDisplayName;

/**
 * Source: LanguageCode-2.2.gc<br>
 * Content created by MainCreateJavaCode_LanguageCode_GC
 *
 * @author Philip Helger
 * @since 2.0.0-rc1
 */
public enum EToopLanguageCode implements IHasID <String>, IHasDisplayName
{
  /** Afar */
  AA ("AA", "Afar"),
  /** Afar */
  AAR ("AAR", "Afar"),
  /** Abkhazian */
  AB ("AB", "Abkhazian"),
  /** Abkhazian */
  ABK ("ABK", "Abkhazian"),
  /** Achinese */
  ACE ("ACE", "Achinese"),
  /** Acoli */
  ACH ("ACH", "Acoli"),
  /** Adangme */
  ADA ("ADA", "Adangme"),
  /** Adyghe; Adygei */
  ADY ("ADY", "Adyghe; Adygei"),
  /** Avestan */
  AE ("AE", "Avestan"),
  /** Afrikaans */
  AF ("AF", "Afrikaans"),
  /** Afro-Asiatic languages */
  AFA ("AFA", "Afro-Asiatic languages"),
  /** Afrihili */
  AFH ("AFH", "Afrihili"),
  /** Afrikaans */
  AFR ("AFR", "Afrikaans"),
  /** Ainu */
  AIN ("AIN", "Ainu"),
  /** Akan */
  AK ("AK", "Akan"),
  /** Akan */
  AKA ("AKA", "Akan"),
  /** Akkadian */
  AKK ("AKK", "Akkadian"),
  /** Albanian */
  ALB_B_SQI_T ("ALB (B) SQI (T)", "Albanian"),
  /** Aleut */
  ALE ("ALE", "Aleut"),
  /** Algonquian languages */
  ALG ("ALG", "Algonquian languages"),
  /** Southern Altai */
  ALT ("ALT", "Southern Altai"),
  /** Amharic */
  AM ("AM", "Amharic"),
  /** Amharic */
  AMH ("AMH", "Amharic"),
  /** Aragonese */
  AN ("AN", "Aragonese"),
  /** English, Old (ca.450-1100) */
  ANG ("ANG", "English, Old (ca.450-1100)"),
  /** Angika */
  ANP ("ANP", "Angika"),
  /** Apache languages */
  APA ("APA", "Apache languages"),
  /** Arabic */
  AR ("AR", "Arabic"),
  /** Arabic */
  ARA ("ARA", "Arabic"),
  /** Official Aramaic (700-300 BCE); Imperial Aramaic (700-300 BCE) */
  ARC ("ARC", "Official Aramaic (700-300 BCE); Imperial Aramaic (700-300 BCE)"),
  /** Aragonese */
  ARG ("ARG", "Aragonese"),
  /** Armenian */
  ARM_B_HYE_T ("ARM (B) HYE (T)", "Armenian"),
  /** Mapudungun; Mapuche */
  ARN ("ARN", "Mapudungun; Mapuche"),
  /** Arapaho */
  ARP ("ARP", "Arapaho"),
  /** Artificial languages */
  ART ("ART", "Artificial languages"),
  /** Arawak */
  ARW ("ARW", "Arawak"),
  /** Assamese */
  AS ("AS", "Assamese"),
  /** Assamese */
  ASM ("ASM", "Assamese"),
  /** Asturian; Bable; Leonese; Asturleonese */
  AST ("AST", "Asturian; Bable; Leonese; Asturleonese"),
  /** Athapascan languages */
  ATH ("ATH", "Athapascan languages"),
  /** Australian languages */
  AUS ("AUS", "Australian languages"),
  /** Avaric */
  AV ("AV", "Avaric"),
  /** Avaric */
  AVA ("AVA", "Avaric"),
  /** Avestan */
  AVE ("AVE", "Avestan"),
  /** Awadhi */
  AWA ("AWA", "Awadhi"),
  /** Aymara */
  AY ("AY", "Aymara"),
  /** Aymara */
  AYM ("AYM", "Aymara"),
  /** Azerbaijani */
  AZ ("AZ", "Azerbaijani"),
  /** Azerbaijani */
  AZE ("AZE", "Azerbaijani"),
  /** Bashkir */
  BA ("BA", "Bashkir"),
  /** Banda languages */
  BAD ("BAD", "Banda languages"),
  /** Bamileke languages */
  BAI ("BAI", "Bamileke languages"),
  /** Bashkir */
  BAK ("BAK", "Bashkir"),
  /** Baluchi */
  BAL ("BAL", "Baluchi"),
  /** Bambara */
  BAM ("BAM", "Bambara"),
  /** Balinese */
  BAN ("BAN", "Balinese"),
  /** Basque */
  BAQ_B_EUS_T ("BAQ (B) EUS (T)", "Basque"),
  /** Basa */
  BAS ("BAS", "Basa"),
  /** Baltic languages */
  BAT ("BAT", "Baltic languages"),
  /** Belarusian */
  BE ("BE", "Belarusian"),
  /** Beja; Bedawiyet */
  BEJ ("BEJ", "Beja; Bedawiyet"),
  /** Belarusian */
  BEL ("BEL", "Belarusian"),
  /** Bemba */
  BEM ("BEM", "Bemba"),
  /** Bengali */
  BEN ("BEN", "Bengali"),
  /** Berber languages */
  BER ("BER", "Berber languages"),
  /** Bulgarian */
  BG ("BG", "Bulgarian"),
  /** Bihari languages */
  BH ("BH", "Bihari languages"),
  /** Bhojpuri */
  BHO ("BHO", "Bhojpuri"),
  /** Bislama */
  BI ("BI", "Bislama"),
  /** Bihari languages */
  BIH ("BIH", "Bihari languages"),
  /** Bikol */
  BIK ("BIK", "Bikol"),
  /** Bini; Edo */
  BIN ("BIN", "Bini; Edo"),
  /** Bislama */
  BIS ("BIS", "Bislama"),
  /** Siksika */
  BLA ("BLA", "Siksika"),
  /** Bambara */
  BM ("BM", "Bambara"),
  /** Bengali */
  BN ("BN", "Bengali"),
  /** Bantu languages */
  BNT ("BNT", "Bantu languages"),
  /** Tibetan */
  BO ("BO", "Tibetan"),
  /** Bosnian */
  BOS ("BOS", "Bosnian"),
  /** Breton */
  BR ("BR", "Breton"),
  /** Braj */
  BRA ("BRA", "Braj"),
  /** Breton */
  BRE ("BRE", "Breton"),
  /** Bosnian */
  BS ("BS", "Bosnian"),
  /** Batak languages */
  BTK ("BTK", "Batak languages"),
  /** Buriat */
  BUA ("BUA", "Buriat"),
  /** Buginese */
  BUG ("BUG", "Buginese"),
  /** Bulgarian */
  BUL ("BUL", "Bulgarian"),
  /** Burmese */
  BUR_B_MYA_T ("BUR (B) MYA (T)", "Burmese"),
  /** Blin; Bilin */
  BYN ("BYN", "Blin; Bilin"),
  /** Catalan; Valencian */
  CA ("CA", "Catalan; Valencian"),
  /** Caddo */
  CAD ("CAD", "Caddo"),
  /** Central American Indian languages */
  CAI ("CAI", "Central American Indian languages"),
  /** Galibi Carib */
  CAR ("CAR", "Galibi Carib"),
  /** Catalan; Valencian */
  CAT ("CAT", "Catalan; Valencian"),
  /** Caucasian languages */
  CAU ("CAU", "Caucasian languages"),
  /** Chechen */
  CE ("CE", "Chechen"),
  /** Cebuano */
  CEB ("CEB", "Cebuano"),
  /** Celtic languages */
  CEL ("CEL", "Celtic languages"),
  /** Chamorro */
  CH ("CH", "Chamorro"),
  /** Chamorro */
  CHA ("CHA", "Chamorro"),
  /** Chibcha */
  CHB ("CHB", "Chibcha"),
  /** Chechen */
  CHE ("CHE", "Chechen"),
  /** Chagatai */
  CHG ("CHG", "Chagatai"),
  /** Chinese */
  CHI_B_ZHO_T ("CHI (B) ZHO (T)", "Chinese"),
  /** Chuukese */
  CHK ("CHK", "Chuukese"),
  /** Mari */
  CHM ("CHM", "Mari"),
  /** Chinook jargon */
  CHN ("CHN", "Chinook jargon"),
  /** Choctaw */
  CHO ("CHO", "Choctaw"),
  /** Chipewyan; Dene Suline */
  CHP ("CHP", "Chipewyan; Dene Suline"),
  /** Cherokee */
  CHR ("CHR", "Cherokee"),
  /**
   * Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church
   * Slavonic
   */
  CHU ("CHU", "Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic"),
  /** Chuvash */
  CHV ("CHV", "Chuvash"),
  /** Cheyenne */
  CHY ("CHY", "Cheyenne"),
  /** Chamic languages */
  CMC ("CMC", "Chamic languages"),
  /** Montenegrin */
  CNR ("CNR", "Montenegrin"),
  /** Corsican */
  CO ("CO", "Corsican"),
  /** Coptic */
  COP ("COP", "Coptic"),
  /** Cornish */
  COR ("COR", "Cornish"),
  /** Corsican */
  COS ("COS", "Corsican"),
  /** Creoles and pidgins, English based */
  CPE ("CPE", "Creoles and pidgins, English based"),
  /** Creoles and pidgins, French-based */
  CPF ("CPF", "Creoles and pidgins, French-based"),
  /** Creoles and pidgins, Portuguese-based */
  CPP ("CPP", "Creoles and pidgins, Portuguese-based"),
  /** Cree */
  CR ("CR", "Cree"),
  /** Cree */
  CRE ("CRE", "Cree"),
  /** Crimean Tatar; Crimean Turkish */
  CRH ("CRH", "Crimean Tatar; Crimean Turkish"),
  /** Creoles and pidgins */
  CRP ("CRP", "Creoles and pidgins"),
  /** Czech */
  CS ("CS", "Czech"),
  /** Kashubian */
  CSB ("CSB", "Kashubian"),
  /**
   * Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church
   * Slavonic
   */
  CU ("CU", "Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic"),
  /** Cushitic languages */
  CUS ("CUS", "Cushitic languages"),
  /** Chuvash */
  CV ("CV", "Chuvash"),
  /** Welsh */
  CY ("CY", "Welsh"),
  /** Czech */
  CZE_B_CES_T ("CZE (B) CES (T)", "Czech"),
  /** Danish */
  DA ("DA", "Danish"),
  /** Dakota */
  DAK ("DAK", "Dakota"),
  /** Danish */
  DAN ("DAN", "Danish"),
  /** Dargwa */
  DAR ("DAR", "Dargwa"),
  /** Land Dayak languages */
  DAY ("DAY", "Land Dayak languages"),
  /** German */
  DE ("DE", "German"),
  /** Delaware */
  DEL ("DEL", "Delaware"),
  /** Slave (Athapascan) */
  DEN ("DEN", "Slave (Athapascan)"),
  /** Dogrib */
  DGR ("DGR", "Dogrib"),
  /** Dinka */
  DIN ("DIN", "Dinka"),
  /** Divehi; Dhivehi; Maldivian */
  DIV ("DIV", "Divehi; Dhivehi; Maldivian"),
  /** Dogri */
  DOI ("DOI", "Dogri"),
  /** Dravidian languages */
  DRA ("DRA", "Dravidian languages"),
  /** Lower Sorbian */
  DSB ("DSB", "Lower Sorbian"),
  /** Duala */
  DUA ("DUA", "Duala"),
  /** Dutch, Middle (ca.1050-1350) */
  DUM ("DUM", "Dutch, Middle (ca.1050-1350)"),
  /** Dutch; Flemish */
  DUT_B_NLD_T ("DUT (B) NLD (T)", "Dutch; Flemish"),
  /** Divehi; Dhivehi; Maldivian */
  DV ("DV", "Divehi; Dhivehi; Maldivian"),
  /** Dyula */
  DYU ("DYU", "Dyula"),
  /** Dzongkha */
  DZ ("DZ", "Dzongkha"),
  /** Dzongkha */
  DZO ("DZO", "Dzongkha"),
  /** Ewe */
  EE ("EE", "Ewe"),
  /** Efik */
  EFI ("EFI", "Efik"),
  /** Egyptian (Ancient) */
  EGY ("EGY", "Egyptian (Ancient)"),
  /** Ekajuk */
  EKA ("EKA", "Ekajuk"),
  /** Greek, Modern (1453-) */
  EL ("EL", "Greek, Modern (1453-)"),
  /** Elamite */
  ELX ("ELX", "Elamite"),
  /** English */
  EN ("EN", "English"),
  /** English */
  ENG ("ENG", "English"),
  /** English, Middle (1100-1500) */
  ENM ("ENM", "English, Middle (1100-1500)"),
  /** Esperanto */
  EO ("EO", "Esperanto"),
  /** Esperanto */
  EPO ("EPO", "Esperanto"),
  /** Spanish; Castilian */
  ES ("ES", "Spanish; Castilian"),
  /** Estonian */
  EST ("EST", "Estonian"),
  /** Estonian */
  ET ("ET", "Estonian"),
  /** Basque */
  EU ("EU", "Basque"),
  /** Ewe */
  EWE ("EWE", "Ewe"),
  /** Ewondo */
  EWO ("EWO", "Ewondo"),
  /** Persian */
  FA ("FA", "Persian"),
  /** Fang */
  FAN ("FAN", "Fang"),
  /** Faroese */
  FAO ("FAO", "Faroese"),
  /** Fanti */
  FAT ("FAT", "Fanti"),
  /** Fulah */
  FF ("FF", "Fulah"),
  /** Finnish */
  FI ("FI", "Finnish"),
  /** Fijian */
  FIJ ("FIJ", "Fijian"),
  /** Filipino; Pilipino */
  FIL ("FIL", "Filipino; Pilipino"),
  /** Finnish */
  FIN ("FIN", "Finnish"),
  /** Finno-Ugrian languages */
  FIU ("FIU", "Finno-Ugrian languages"),
  /** Fijian */
  FJ ("FJ", "Fijian"),
  /** Faroese */
  FO ("FO", "Faroese"),
  /** Fon */
  FON ("FON", "Fon"),
  /** French */
  FR ("FR", "French"),
  /** French */
  FRE_B_FRA_T ("FRE (B) FRA (T)", "French"),
  /** French, Middle (ca.1400-1600) */
  FRM ("FRM", "French, Middle (ca.1400-1600)"),
  /** French, Old (842-ca.1400) */
  FRO ("FRO", "French, Old (842-ca.1400)"),
  /** Northern Frisian */
  FRR ("FRR", "Northern Frisian"),
  /** Eastern Frisian */
  FRS ("FRS", "Eastern Frisian"),
  /** Western Frisian */
  FRY ("FRY", "Western Frisian"),
  /** Fulah */
  FUL ("FUL", "Fulah"),
  /** Friulian */
  FUR ("FUR", "Friulian"),
  /** Western Frisian */
  FY ("FY", "Western Frisian"),
  /** Irish */
  GA ("GA", "Irish"),
  /** Ga */
  GAA ("GAA", "Ga"),
  /** Gayo */
  GAY ("GAY", "Gayo"),
  /** Gbaya */
  GBA ("GBA", "Gbaya"),
  /** Gaelic; Scottish Gaelic */
  GD ("GD", "Gaelic; Scottish Gaelic"),
  /** Germanic languages */
  GEM ("GEM", "Germanic languages"),
  /** Georgian */
  GEO_B_KAT_T ("GEO (B) KAT (T)", "Georgian"),
  /** German */
  GER_B_DEU_T ("GER (B) DEU (T)", "German"),
  /** Geez */
  GEZ ("GEZ", "Geez"),
  /** Gilbertese */
  GIL ("GIL", "Gilbertese"),
  /** Galician */
  GL ("GL", "Galician"),
  /** Gaelic; Scottish Gaelic */
  GLA ("GLA", "Gaelic; Scottish Gaelic"),
  /** Irish */
  GLE ("GLE", "Irish"),
  /** Galician */
  GLG ("GLG", "Galician"),
  /** Manx */
  GLV ("GLV", "Manx"),
  /** German, Middle High (ca.1050-1500) */
  GMH ("GMH", "German, Middle High (ca.1050-1500)"),
  /** Guarani */
  GN ("GN", "Guarani"),
  /** German, Old High (ca.750-1050) */
  GOH ("GOH", "German, Old High (ca.750-1050)"),
  /** Gondi */
  GON ("GON", "Gondi"),
  /** Gorontalo */
  GOR ("GOR", "Gorontalo"),
  /** Gothic */
  GOT ("GOT", "Gothic"),
  /** Grebo */
  GRB ("GRB", "Grebo"),
  /** Greek, Ancient (to 1453) */
  GRC ("GRC", "Greek, Ancient (to 1453)"),
  /** Greek, Modern (1453-) */
  GRE_B_ELL_T ("GRE (B) ELL (T)", "Greek, Modern (1453-)"),
  /** Guarani */
  GRN ("GRN", "Guarani"),
  /** Swiss German; Alemannic; Alsatian */
  GSW ("GSW", "Swiss German; Alemannic; Alsatian"),
  /** Gujarati */
  GU ("GU", "Gujarati"),
  /** Gujarati */
  GUJ ("GUJ", "Gujarati"),
  /** Manx */
  GV ("GV", "Manx"),
  /** Gwich'in */
  GWI ("GWI", "Gwich'in"),
  /** Hausa */
  HA ("HA", "Hausa"),
  /** Haida */
  HAI ("HAI", "Haida"),
  /** Haitian; Haitian Creole */
  HAT ("HAT", "Haitian; Haitian Creole"),
  /** Hausa */
  HAU ("HAU", "Hausa"),
  /** Hawaiian */
  HAW ("HAW", "Hawaiian"),
  /** Hebrew */
  HE ("HE", "Hebrew"),
  /** Hebrew */
  HEB ("HEB", "Hebrew"),
  /** Herero */
  HER ("HER", "Herero"),
  /** Hindi */
  HI ("HI", "Hindi"),
  /** Hiligaynon */
  HIL ("HIL", "Hiligaynon"),
  /** Himachali languages; Western Pahari languages */
  HIM ("HIM", "Himachali languages; Western Pahari languages"),
  /** Hindi */
  HIN ("HIN", "Hindi"),
  /** Hittite */
  HIT ("HIT", "Hittite"),
  /** Hmong; Mong */
  HMN ("HMN", "Hmong; Mong"),
  /** Hiri Motu */
  HMO ("HMO", "Hiri Motu"),
  /** Hiri Motu */
  HO ("HO", "Hiri Motu"),
  /** Croatian */
  HR ("HR", "Croatian"),
  /** Croatian */
  HRV ("HRV", "Croatian"),
  /** Upper Sorbian */
  HSB ("HSB", "Upper Sorbian"),
  /** Haitian; Haitian Creole */
  HT ("HT", "Haitian; Haitian Creole"),
  /** Hungarian */
  HU ("HU", "Hungarian"),
  /** Hungarian */
  HUN ("HUN", "Hungarian"),
  /** Hupa */
  HUP ("HUP", "Hupa"),
  /** Armenian */
  HY ("HY", "Armenian"),
  /** Herero */
  HZ ("HZ", "Herero"),
  /** Interlingua (International Auxiliary Language Association) */
  IA ("IA", "Interlingua (International Auxiliary Language Association)"),
  /** Iban */
  IBA ("IBA", "Iban"),
  /** Igbo */
  IBO ("IBO", "Igbo"),
  /** Icelandic */
  ICE_B_ISL_T ("ICE (B) ISL (T)", "Icelandic"),
  /** Indonesian */
  ID ("ID", "Indonesian"),
  /** Ido */
  IDO ("IDO", "Ido"),
  /** Interlingue; Occidental */
  IE ("IE", "Interlingue; Occidental"),
  /** Igbo */
  IG ("IG", "Igbo"),
  /** Sichuan Yi; Nuosu */
  II ("II", "Sichuan Yi; Nuosu"),
  /** Sichuan Yi; Nuosu */
  III ("III", "Sichuan Yi; Nuosu"),
  /** Ijo languages */
  IJO ("IJO", "Ijo languages"),
  /** Inupiaq */
  IK ("IK", "Inupiaq"),
  /** Inuktitut */
  IKU ("IKU", "Inuktitut"),
  /** Interlingue; Occidental */
  ILE ("ILE", "Interlingue; Occidental"),
  /** Iloko */
  ILO ("ILO", "Iloko"),
  /** Interlingua (International Auxiliary Language Association) */
  INA ("INA", "Interlingua (International Auxiliary Language Association)"),
  /** Indic languages */
  INC ("INC", "Indic languages"),
  /** Indonesian */
  IND ("IND", "Indonesian"),
  /** Indo-European languages */
  INE ("INE", "Indo-European languages"),
  /** Ingush */
  INH ("INH", "Ingush"),
  /** Ido */
  IO ("IO", "Ido"),
  /** Inupiaq */
  IPK ("IPK", "Inupiaq"),
  /** Iranian languages */
  IRA ("IRA", "Iranian languages"),
  /** Iroquoian languages */
  IRO ("IRO", "Iroquoian languages"),
  /** Icelandic */
  IS ("IS", "Icelandic"),
  /** Italian */
  IT ("IT", "Italian"),
  /** Italian */
  ITA ("ITA", "Italian"),
  /** Inuktitut */
  IU ("IU", "Inuktitut"),
  /** Japanese */
  JA ("JA", "Japanese"),
  /** Javanese */
  JAV ("JAV", "Javanese"),
  /** Lojban */
  JBO ("JBO", "Lojban"),
  /** Japanese */
  JPN ("JPN", "Japanese"),
  /** Judeo-Persian */
  JPR ("JPR", "Judeo-Persian"),
  /** Judeo-Arabic */
  JRB ("JRB", "Judeo-Arabic"),
  /** Javanese */
  JV ("JV", "Javanese"),
  /** Georgian */
  KA ("KA", "Georgian"),
  /** Kara-Kalpak */
  KAA ("KAA", "Kara-Kalpak"),
  /** Kabyle */
  KAB ("KAB", "Kabyle"),
  /** Kachin; Jingpho */
  KAC ("KAC", "Kachin; Jingpho"),
  /** Kalaallisut; Greenlandic */
  KAL ("KAL", "Kalaallisut; Greenlandic"),
  /** Kamba */
  KAM ("KAM", "Kamba"),
  /** Kannada */
  KAN ("KAN", "Kannada"),
  /** Karen languages */
  KAR ("KAR", "Karen languages"),
  /** Kashmiri */
  KAS ("KAS", "Kashmiri"),
  /** Kanuri */
  KAU ("KAU", "Kanuri"),
  /** Kawi */
  KAW ("KAW", "Kawi"),
  /** Kazakh */
  KAZ ("KAZ", "Kazakh"),
  /** Kabardian */
  KBD ("KBD", "Kabardian"),
  /** Kongo */
  KG ("KG", "Kongo"),
  /** Khasi */
  KHA ("KHA", "Khasi"),
  /** Khoisan languages */
  KHI ("KHI", "Khoisan languages"),
  /** Central Khmer */
  KHM ("KHM", "Central Khmer"),
  /** Khotanese; Sakan */
  KHO ("KHO", "Khotanese; Sakan"),
  /** Kikuyu; Gikuyu */
  KI ("KI", "Kikuyu; Gikuyu"),
  /** Kikuyu; Gikuyu */
  KIK ("KIK", "Kikuyu; Gikuyu"),
  /** Kinyarwanda */
  KIN ("KIN", "Kinyarwanda"),
  /** Kirghiz; Kyrgyz */
  KIR ("KIR", "Kirghiz; Kyrgyz"),
  /** Kuanyama; Kwanyama */
  KJ ("KJ", "Kuanyama; Kwanyama"),
  /** Kazakh */
  KK ("KK", "Kazakh"),
  /** Kalaallisut; Greenlandic */
  KL ("KL", "Kalaallisut; Greenlandic"),
  /** Central Khmer */
  KM ("KM", "Central Khmer"),
  /** Kimbundu */
  KMB ("KMB", "Kimbundu"),
  /** Kannada */
  KN ("KN", "Kannada"),
  /** Korean */
  KO ("KO", "Korean"),
  /** Konkani */
  KOK ("KOK", "Konkani"),
  /** Komi */
  KOM ("KOM", "Komi"),
  /** Kongo */
  KON ("KON", "Kongo"),
  /** Korean */
  KOR ("KOR", "Korean"),
  /** Kosraean */
  KOS ("KOS", "Kosraean"),
  /** Kpelle */
  KPE ("KPE", "Kpelle"),
  /** Kanuri */
  KR ("KR", "Kanuri"),
  /** Karachay-Balkar */
  KRC ("KRC", "Karachay-Balkar"),
  /** Karelian */
  KRL ("KRL", "Karelian"),
  /** Kru languages */
  KRO ("KRO", "Kru languages"),
  /** Kurukh */
  KRU ("KRU", "Kurukh"),
  /** Kashmiri */
  KS ("KS", "Kashmiri"),
  /** Kurdish */
  KU ("KU", "Kurdish"),
  /** Kuanyama; Kwanyama */
  KUA ("KUA", "Kuanyama; Kwanyama"),
  /** Kumyk */
  KUM ("KUM", "Kumyk"),
  /** Kurdish */
  KUR ("KUR", "Kurdish"),
  /** Kutenai */
  KUT ("KUT", "Kutenai"),
  /** Komi */
  KV ("KV", "Komi"),
  /** Cornish */
  KW ("KW", "Cornish"),
  /** Kirghiz; Kyrgyz */
  KY ("KY", "Kirghiz; Kyrgyz"),
  /** Latin */
  LA ("LA", "Latin"),
  /** Ladino */
  LAD ("LAD", "Ladino"),
  /** Lahnda */
  LAH ("LAH", "Lahnda"),
  /** Lamba */
  LAM ("LAM", "Lamba"),
  /** Lao */
  LAO ("LAO", "Lao"),
  /** Latin */
  LAT ("LAT", "Latin"),
  /** Latvian */
  LAV ("LAV", "Latvian"),
  /** Luxembourgish; Letzeburgesch */
  LB ("LB", "Luxembourgish; Letzeburgesch"),
  /** Lezghian */
  LEZ ("LEZ", "Lezghian"),
  /** Ganda */
  LG ("LG", "Ganda"),
  /** Limburgan; Limburger; Limburgish */
  LI ("LI", "Limburgan; Limburger; Limburgish"),
  /** Limburgan; Limburger; Limburgish */
  LIM ("LIM", "Limburgan; Limburger; Limburgish"),
  /** Lingala */
  LIN ("LIN", "Lingala"),
  /** Lithuanian */
  LIT ("LIT", "Lithuanian"),
  /** Lingala */
  LN ("LN", "Lingala"),
  /** Lao */
  LO ("LO", "Lao"),
  /** Mongo */
  LOL ("LOL", "Mongo"),
  /** Lozi */
  LOZ ("LOZ", "Lozi"),
  /** Lithuanian */
  LT ("LT", "Lithuanian"),
  /** Luxembourgish; Letzeburgesch */
  LTZ ("LTZ", "Luxembourgish; Letzeburgesch"),
  /** Luba-Katanga */
  LU ("LU", "Luba-Katanga"),
  /** Luba-Lulua */
  LUA ("LUA", "Luba-Lulua"),
  /** Luba-Katanga */
  LUB ("LUB", "Luba-Katanga"),
  /** Ganda */
  LUG ("LUG", "Ganda"),
  /** Luiseno */
  LUI ("LUI", "Luiseno"),
  /** Lunda */
  LUN ("LUN", "Lunda"),
  /** Luo (Kenya and Tanzania) */
  LUO ("LUO", "Luo (Kenya and Tanzania)"),
  /** Lushai */
  LUS ("LUS", "Lushai"),
  /** Latvian */
  LV ("LV", "Latvian"),
  /** Macedonian */
  MAC_B_MKD_T ("MAC (B) MKD (T)", "Macedonian"),
  /** Madurese */
  MAD ("MAD", "Madurese"),
  /** Magahi */
  MAG ("MAG", "Magahi"),
  /** Marshallese */
  MAH ("MAH", "Marshallese"),
  /** Maithili */
  MAI ("MAI", "Maithili"),
  /** Makasar */
  MAK ("MAK", "Makasar"),
  /** Malayalam */
  MAL ("MAL", "Malayalam"),
  /** Mandingo */
  MAN ("MAN", "Mandingo"),
  /** Maori */
  MAO_B_MRI_T ("MAO (B) MRI (T)", "Maori"),
  /** Austronesian languages */
  MAP ("MAP", "Austronesian languages"),
  /** Marathi */
  MAR ("MAR", "Marathi"),
  /** Masai */
  MAS ("MAS", "Masai"),
  /** Malay */
  MAY_B_MSA_T ("MAY (B) MSA (T)", "Malay"),
  /** Moksha */
  MDF ("MDF", "Moksha"),
  /** Mandar */
  MDR ("MDR", "Mandar"),
  /** Mende */
  MEN ("MEN", "Mende"),
  /** Malagasy */
  MG ("MG", "Malagasy"),
  /** Irish, Middle (900-1200) */
  MGA ("MGA", "Irish, Middle (900-1200)"),
  /** Marshallese */
  MH ("MH", "Marshallese"),
  /** Maori */
  MI ("MI", "Maori"),
  /** Mi'kmaq; Micmac */
  MIC ("MIC", "Mi'kmaq; Micmac"),
  /** Minangkabau */
  MIN ("MIN", "Minangkabau"),
  /** Uncoded languages */
  MIS ("MIS", "Uncoded languages"),
  /** Macedonian */
  MK ("MK", "Macedonian"),
  /** Mon-Khmer languages */
  MKH ("MKH", "Mon-Khmer languages"),
  /** Malayalam */
  ML ("ML", "Malayalam"),
  /** Malagasy */
  MLG ("MLG", "Malagasy"),
  /** Maltese */
  MLT ("MLT", "Maltese"),
  /** Mongolian */
  MN ("MN", "Mongolian"),
  /** Manchu */
  MNC ("MNC", "Manchu"),
  /** Manipuri */
  MNI ("MNI", "Manipuri"),
  /** Manobo languages */
  MNO ("MNO", "Manobo languages"),
  /** Mohawk */
  MOH ("MOH", "Mohawk"),
  /** Mongolian */
  MON ("MON", "Mongolian"),
  /** Mossi */
  MOS ("MOS", "Mossi"),
  /** Marathi */
  MR ("MR", "Marathi"),
  /** Malay */
  MS ("MS", "Malay"),
  /** Maltese */
  MT ("MT", "Maltese"),
  /** Multiple languages */
  MUL ("MUL", "Multiple languages"),
  /** Munda languages */
  MUN ("MUN", "Munda languages"),
  /** Creek */
  MUS ("MUS", "Creek"),
  /** Mirandese */
  MWL ("MWL", "Mirandese"),
  /** Marwari */
  MWR ("MWR", "Marwari"),
  /** Burmese */
  MY ("MY", "Burmese"),
  /** Mayan languages */
  MYN ("MYN", "Mayan languages"),
  /** Erzya */
  MYV ("MYV", "Erzya"),
  /** Nauru */
  NA ("NA", "Nauru"),
  /** Nahuatl languages */
  NAH ("NAH", "Nahuatl languages"),
  /** North American Indian languages */
  NAI ("NAI", "North American Indian languages"),
  /** Neapolitan */
  NAP ("NAP", "Neapolitan"),
  /** Nauru */
  NAU ("NAU", "Nauru"),
  /** Navajo; Navaho */
  NAV ("NAV", "Navajo; Navaho"),
  /** Bokmål, Norwegian; Norwegian Bokmål */
  NB ("NB", "Bokmål, Norwegian; Norwegian Bokmål"),
  /** Ndebele, South; South Ndebele */
  NBL ("NBL", "Ndebele, South; South Ndebele"),
  /** Ndebele, North; North Ndebele */
  ND ("ND", "Ndebele, North; North Ndebele"),
  /** Ndebele, North; North Ndebele */
  NDE ("NDE", "Ndebele, North; North Ndebele"),
  /** Ndonga */
  NDO ("NDO", "Ndonga"),
  /** Low German; Low Saxon; German, Low; Saxon, Low */
  NDS ("NDS", "Low German; Low Saxon; German, Low; Saxon, Low"),
  /** Nepali */
  NE ("NE", "Nepali"),
  /** Nepali */
  NEP ("NEP", "Nepali"),
  /** Nepal Bhasa; Newari */
  NEW ("NEW", "Nepal Bhasa; Newari"),
  /** Ndonga */
  NG ("NG", "Ndonga"),
  /** Nias */
  NIA ("NIA", "Nias"),
  /** Niger-Kordofanian languages */
  NIC ("NIC", "Niger-Kordofanian languages"),
  /** Niuean */
  NIU ("NIU", "Niuean"),
  /** Dutch; Flemish */
  NL ("NL", "Dutch; Flemish"),
  /** Norwegian Nynorsk; Nynorsk, Norwegian */
  NN ("NN", "Norwegian Nynorsk; Nynorsk, Norwegian"),
  /** Norwegian Nynorsk; Nynorsk, Norwegian */
  NNO ("NNO", "Norwegian Nynorsk; Nynorsk, Norwegian"),
  /** Norwegian */
  NO ("NO", "Norwegian"),
  /** Bokmål, Norwegian; Norwegian Bokmål */
  NOB ("NOB", "Bokmål, Norwegian; Norwegian Bokmål"),
  /** Nogai */
  NOG ("NOG", "Nogai"),
  /** Norse, Old */
  NON ("NON", "Norse, Old"),
  /** Norwegian */
  NOR ("NOR", "Norwegian"),
  /** N'Ko */
  NQO ("NQO", "N'Ko"),
  /** Ndebele, South; South Ndebele */
  NR ("NR", "Ndebele, South; South Ndebele"),
  /** Pedi; Sepedi; Northern Sotho */
  NSO ("NSO", "Pedi; Sepedi; Northern Sotho"),
  /** Nubian languages */
  NUB ("NUB", "Nubian languages"),
  /** Navajo; Navaho */
  NV ("NV", "Navajo; Navaho"),
  /** Classical Newari; Old Newari; Classical Nepal Bhasa */
  NWC ("NWC", "Classical Newari; Old Newari; Classical Nepal Bhasa"),
  /** Chichewa; Chewa; Nyanja */
  NY ("NY", "Chichewa; Chewa; Nyanja"),
  /** Chichewa; Chewa; Nyanja */
  NYA ("NYA", "Chichewa; Chewa; Nyanja"),
  /** Nyamwezi */
  NYM ("NYM", "Nyamwezi"),
  /** Nyankole */
  NYN ("NYN", "Nyankole"),
  /** Nyoro */
  NYO ("NYO", "Nyoro"),
  /** Nzima */
  NZI ("NZI", "Nzima"),
  /** Occitan (post 1500) */
  OC ("OC", "Occitan (post 1500)"),
  /** Occitan (post 1500) */
  OCI ("OCI", "Occitan (post 1500)"),
  /** Ojibwa */
  OJ ("OJ", "Ojibwa"),
  /** Ojibwa */
  OJI ("OJI", "Ojibwa"),
  /** Oromo */
  OM ("OM", "Oromo"),
  /** Oriya */
  OR ("OR", "Oriya"),
  /** Oriya */
  ORI ("ORI", "Oriya"),
  /** Oromo */
  ORM ("ORM", "Oromo"),
  /** Ossetian; Ossetic */
  OS ("OS", "Ossetian; Ossetic"),
  /** Osage */
  OSA ("OSA", "Osage"),
  /** Ossetian; Ossetic */
  OSS ("OSS", "Ossetian; Ossetic"),
  /** Turkish, Ottoman (1500-1928) */
  OTA ("OTA", "Turkish, Ottoman (1500-1928)"),
  /** Otomian languages */
  OTO ("OTO", "Otomian languages"),
  /** Panjabi; Punjabi */
  PA ("PA", "Panjabi; Punjabi"),
  /** Papuan languages */
  PAA ("PAA", "Papuan languages"),
  /** Pangasinan */
  PAG ("PAG", "Pangasinan"),
  /** Pahlavi */
  PAL ("PAL", "Pahlavi"),
  /** Pampanga; Kapampangan */
  PAM ("PAM", "Pampanga; Kapampangan"),
  /** Panjabi; Punjabi */
  PAN ("PAN", "Panjabi; Punjabi"),
  /** Papiamento */
  PAP ("PAP", "Papiamento"),
  /** Palauan */
  PAU ("PAU", "Palauan"),
  /** Persian, Old (ca.600-400 B.C.) */
  PEO ("PEO", "Persian, Old (ca.600-400 B.C.)"),
  /** Persian */
  PER_B_FAS_T ("PER (B) FAS (T)", "Persian"),
  /** Philippine languages */
  PHI ("PHI", "Philippine languages"),
  /** Phoenician */
  PHN ("PHN", "Phoenician"),
  /** Pali */
  PI ("PI", "Pali"),
  /** Polish */
  PL ("PL", "Polish"),
  /** Pali */
  PLI ("PLI", "Pali"),
  /** Polish */
  POL ("POL", "Polish"),
  /** Pohnpeian */
  PON ("PON", "Pohnpeian"),
  /** Portuguese */
  POR ("POR", "Portuguese"),
  /** Prakrit languages */
  PRA ("PRA", "Prakrit languages"),
  /** Provençal, Old (to 1500);Occitan, Old (to 1500) */
  PRO ("PRO", "Provençal, Old (to 1500);Occitan, Old (to 1500)"),
  /** Pushto; Pashto */
  PS ("PS", "Pushto; Pashto"),
  /** Portuguese */
  PT ("PT", "Portuguese"),
  /** Pushto; Pashto */
  PUS ("PUS", "Pushto; Pashto"),
  /** Reserved for local use */
  QAA_QTZ ("QAA-QTZ", "Reserved for local use"),
  /** Quechua */
  QU ("QU", "Quechua"),
  /** Quechua */
  QUE ("QUE", "Quechua"),
  /** Rajasthani */
  RAJ ("RAJ", "Rajasthani"),
  /** Rapanui */
  RAP ("RAP", "Rapanui"),
  /** Rarotongan; Cook Islands Maori */
  RAR ("RAR", "Rarotongan; Cook Islands Maori"),
  /** Romansh */
  RM ("RM", "Romansh"),
  /** Rundi */
  RN ("RN", "Rundi"),
  /** Romanian; Moldavian; Moldovan */
  RO ("RO", "Romanian; Moldavian; Moldovan"),
  /** Romance languages */
  ROA ("ROA", "Romance languages"),
  /** Romansh */
  ROH ("ROH", "Romansh"),
  /** Romany */
  ROM ("ROM", "Romany"),
  /** Russian */
  RU ("RU", "Russian"),
  /** Romanian; Moldavian; Moldovan */
  RUM_B_RON_T ("RUM (B) RON (T)", "Romanian; Moldavian; Moldovan"),
  /** Rundi */
  RUN ("RUN", "Rundi"),
  /** Aromanian; Arumanian; Macedo-Romanian */
  RUP ("RUP", "Aromanian; Arumanian; Macedo-Romanian"),
  /** Russian */
  RUS ("RUS", "Russian"),
  /** Kinyarwanda */
  RW ("RW", "Kinyarwanda"),
  /** Sanskrit */
  SA ("SA", "Sanskrit"),
  /** Sandawe */
  SAD ("SAD", "Sandawe"),
  /** Sango */
  SAG ("SAG", "Sango"),
  /** Yakut */
  SAH ("SAH", "Yakut"),
  /** South American Indian languages */
  SAI ("SAI", "South American Indian languages"),
  /** Salishan languages */
  SAL ("SAL", "Salishan languages"),
  /** Samaritan Aramaic */
  SAM ("SAM", "Samaritan Aramaic"),
  /** Sanskrit */
  SAN ("SAN", "Sanskrit"),
  /** Sasak */
  SAS ("SAS", "Sasak"),
  /** Santali */
  SAT ("SAT", "Santali"),
  /** Sardinian */
  SC ("SC", "Sardinian"),
  /** Sicilian */
  SCN ("SCN", "Sicilian"),
  /** Scots */
  SCO ("SCO", "Scots"),
  /** Sindhi */
  SD ("SD", "Sindhi"),
  /** Northern Sami */
  SE ("SE", "Northern Sami"),
  /** Selkup */
  SEL ("SEL", "Selkup"),
  /** Semitic languages */
  SEM ("SEM", "Semitic languages"),
  /** Sango */
  SG ("SG", "Sango"),
  /** Irish, Old (to 900) */
  SGA ("SGA", "Irish, Old (to 900)"),
  /** Sign Languages */
  SGN ("SGN", "Sign Languages"),
  /** Shan */
  SHN ("SHN", "Shan"),
  /** Sinhala; Sinhalese */
  SI ("SI", "Sinhala; Sinhalese"),
  /** Sidamo */
  SID ("SID", "Sidamo"),
  /** Sinhala; Sinhalese */
  SIN ("SIN", "Sinhala; Sinhalese"),
  /** Siouan languages */
  SIO ("SIO", "Siouan languages"),
  /** Sino-Tibetan languages */
  SIT ("SIT", "Sino-Tibetan languages"),
  /** Slovak */
  SK ("SK", "Slovak"),
  /** Slovenian */
  SL ("SL", "Slovenian"),
  /** Slavic languages */
  SLA ("SLA", "Slavic languages"),
  /** Slovak */
  SLO_B_SLK_T ("SLO (B) SLK (T)", "Slovak"),
  /** Slovenian */
  SLV ("SLV", "Slovenian"),
  /** Samoan */
  SM ("SM", "Samoan"),
  /** Southern Sami */
  SMA ("SMA", "Southern Sami"),
  /** Northern Sami */
  SME ("SME", "Northern Sami"),
  /** Sami languages */
  SMI ("SMI", "Sami languages"),
  /** Lule Sami */
  SMJ ("SMJ", "Lule Sami"),
  /** Inari Sami */
  SMN ("SMN", "Inari Sami"),
  /** Samoan */
  SMO ("SMO", "Samoan"),
  /** Skolt Sami */
  SMS ("SMS", "Skolt Sami"),
  /** Shona */
  SN ("SN", "Shona"),
  /** Shona */
  SNA ("SNA", "Shona"),
  /** Sindhi */
  SND ("SND", "Sindhi"),
  /** Soninke */
  SNK ("SNK", "Soninke"),
  /** Somali */
  SO ("SO", "Somali"),
  /** Sogdian */
  SOG ("SOG", "Sogdian"),
  /** Somali */
  SOM ("SOM", "Somali"),
  /** Songhai languages */
  SON ("SON", "Songhai languages"),
  /** Sotho, Southern */
  SOT ("SOT", "Sotho, Southern"),
  /** Spanish; Castilian */
  SPA ("SPA", "Spanish; Castilian"),
  /** Albanian */
  SQ ("SQ", "Albanian"),
  /** Serbian */
  SR ("SR", "Serbian"),
  /** Sardinian */
  SRD ("SRD", "Sardinian"),
  /** Sranan Tongo */
  SRN ("SRN", "Sranan Tongo"),
  /** Serbian */
  SRP ("SRP", "Serbian"),
  /** Serer */
  SRR ("SRR", "Serer"),
  /** Swati */
  SS ("SS", "Swati"),
  /** Nilo-Saharan languages */
  SSA ("SSA", "Nilo-Saharan languages"),
  /** Swati */
  SSW ("SSW", "Swati"),
  /** Sotho, Southern */
  ST ("ST", "Sotho, Southern"),
  /** Sundanese */
  SU ("SU", "Sundanese"),
  /** Sukuma */
  SUK ("SUK", "Sukuma"),
  /** Sundanese */
  SUN ("SUN", "Sundanese"),
  /** Susu */
  SUS ("SUS", "Susu"),
  /** Sumerian */
  SUX ("SUX", "Sumerian"),
  /** Swedish */
  SV ("SV", "Swedish"),
  /** Swahili */
  SW ("SW", "Swahili"),
  /** Swahili */
  SWA ("SWA", "Swahili"),
  /** Swedish */
  SWE ("SWE", "Swedish"),
  /** Classical Syriac */
  SYC ("SYC", "Classical Syriac"),
  /** Syriac */
  SYR ("SYR", "Syriac"),
  /** Tamil */
  TA ("TA", "Tamil"),
  /** Tahitian */
  TAH ("TAH", "Tahitian"),
  /** Tai languages */
  TAI ("TAI", "Tai languages"),
  /** Tamil */
  TAM ("TAM", "Tamil"),
  /** Tatar */
  TAT ("TAT", "Tatar"),
  /** Telugu */
  TE ("TE", "Telugu"),
  /** Telugu */
  TEL ("TEL", "Telugu"),
  /** Timne */
  TEM ("TEM", "Timne"),
  /** Tereno */
  TER ("TER", "Tereno"),
  /** Tetum */
  TET ("TET", "Tetum"),
  /** Tajik */
  TG ("TG", "Tajik"),
  /** Tajik */
  TGK ("TGK", "Tajik"),
  /** Tagalog */
  TGL ("TGL", "Tagalog"),
  /** Thai */
  TH ("TH", "Thai"),
  /** Thai */
  THA ("THA", "Thai"),
  /** Tigrinya */
  TI ("TI", "Tigrinya"),
  /** Tibetan */
  TIB_B_BOD_T ("TIB (B) BOD (T)", "Tibetan"),
  /** Tigre */
  TIG ("TIG", "Tigre"),
  /** Tigrinya */
  TIR ("TIR", "Tigrinya"),
  /** Tiv */
  TIV ("TIV", "Tiv"),
  /** Turkmen */
  TK ("TK", "Turkmen"),
  /** Tokelau */
  TKL ("TKL", "Tokelau"),
  /** Tagalog */
  TL ("TL", "Tagalog"),
  /** Klingon; tlhIngan-Hol */
  TLH ("TLH", "Klingon; tlhIngan-Hol"),
  /** Tlingit */
  TLI ("TLI", "Tlingit"),
  /** Tamashek */
  TMH ("TMH", "Tamashek"),
  /** Tswana */
  TN ("TN", "Tswana"),
  /** Tonga (Tonga Islands) */
  TO ("TO", "Tonga (Tonga Islands)"),
  /** Tonga (Nyasa) */
  TOG ("TOG", "Tonga (Nyasa)"),
  /** Tonga (Tonga Islands) */
  TON ("TON", "Tonga (Tonga Islands)"),
  /** Tok Pisin */
  TPI ("TPI", "Tok Pisin"),
  /** Turkish */
  TR ("TR", "Turkish"),
  /** Tsonga */
  TS ("TS", "Tsonga"),
  /** Tsimshian */
  TSI ("TSI", "Tsimshian"),
  /** Tswana */
  TSN ("TSN", "Tswana"),
  /** Tsonga */
  TSO ("TSO", "Tsonga"),
  /** Tatar */
  TT ("TT", "Tatar"),
  /** Turkmen */
  TUK ("TUK", "Turkmen"),
  /** Tumbuka */
  TUM ("TUM", "Tumbuka"),
  /** Tupi languages */
  TUP ("TUP", "Tupi languages"),
  /** Turkish */
  TUR ("TUR", "Turkish"),
  /** Altaic languages */
  TUT ("TUT", "Altaic languages"),
  /** Tuvalu */
  TVL ("TVL", "Tuvalu"),
  /** Twi */
  TW ("TW", "Twi"),
  /** Twi */
  TWI ("TWI", "Twi"),
  /** Tahitian */
  TY ("TY", "Tahitian"),
  /** Tuvinian */
  TYV ("TYV", "Tuvinian"),
  /** Udmurt */
  UDM ("UDM", "Udmurt"),
  /** Uighur; Uyghur */
  UG ("UG", "Uighur; Uyghur"),
  /** Ugaritic */
  UGA ("UGA", "Ugaritic"),
  /** Uighur; Uyghur */
  UIG ("UIG", "Uighur; Uyghur"),
  /** Ukrainian */
  UK ("UK", "Ukrainian"),
  /** Ukrainian */
  UKR ("UKR", "Ukrainian"),
  /** Umbundu */
  UMB ("UMB", "Umbundu"),
  /** Undetermined */
  UND ("UND", "Undetermined"),
  /** Urdu */
  UR ("UR", "Urdu"),
  /** Urdu */
  URD ("URD", "Urdu"),
  /** Uzbek */
  UZ ("UZ", "Uzbek"),
  /** Uzbek */
  UZB ("UZB", "Uzbek"),
  /** Vai */
  VAI ("VAI", "Vai"),
  /** Venda */
  VE ("VE", "Venda"),
  /** Venda */
  VEN ("VEN", "Venda"),
  /** Vietnamese */
  VI ("VI", "Vietnamese"),
  /** Vietnamese */
  VIE ("VIE", "Vietnamese"),
  /** Volapük */
  VO ("VO", "Volapük"),
  /** Volapük */
  VOL ("VOL", "Volapük"),
  /** Votic */
  VOT ("VOT", "Votic"),
  /** Walloon */
  WA ("WA", "Walloon"),
  /** Wakashan languages */
  WAK ("WAK", "Wakashan languages"),
  /** Wolaitta; Wolaytta */
  WAL ("WAL", "Wolaitta; Wolaytta"),
  /** Waray */
  WAR ("WAR", "Waray"),
  /** Washo */
  WAS ("WAS", "Washo"),
  /** Welsh */
  WEL_B_CYM_T ("WEL (B) CYM (T)", "Welsh"),
  /** Sorbian languages */
  WEN ("WEN", "Sorbian languages"),
  /** Walloon */
  WLN ("WLN", "Walloon"),
  /** Wolof */
  WO ("WO", "Wolof"),
  /** Wolof */
  WOL ("WOL", "Wolof"),
  /** Kalmyk; Oirat */
  XAL ("XAL", "Kalmyk; Oirat"),
  /** Xhosa */
  XH ("XH", "Xhosa"),
  /** Xhosa */
  XHO ("XHO", "Xhosa"),
  /** Yao */
  YAO ("YAO", "Yao"),
  /** Yapese */
  YAP ("YAP", "Yapese"),
  /** Yiddish */
  YI ("YI", "Yiddish"),
  /** Yiddish */
  YID ("YID", "Yiddish"),
  /** Yoruba */
  YO ("YO", "Yoruba"),
  /** Yoruba */
  YOR ("YOR", "Yoruba"),
  /** Yupik languages */
  YPK ("YPK", "Yupik languages"),
  /** Zhuang; Chuang */
  ZA ("ZA", "Zhuang; Chuang"),
  /** Zapotec */
  ZAP ("ZAP", "Zapotec"),
  /** Blissymbols; Blissymbolics; Bliss */
  ZBL ("ZBL", "Blissymbols; Blissymbolics; Bliss"),
  /** Zenaga */
  ZEN ("ZEN", "Zenaga"),
  /** Standard Moroccan Tamazight */
  ZGH ("ZGH", "Standard Moroccan Tamazight"),
  /** Chinese */
  ZH ("ZH", "Chinese"),
  /** Zhuang; Chuang */
  ZHA ("ZHA", "Zhuang; Chuang"),
  /** Zande languages */
  ZND ("ZND", "Zande languages"),
  /** Zulu */
  ZU ("ZU", "Zulu"),
  /** Zulu */
  ZUL ("ZUL", "Zulu"),
  /** Zuni */
  ZUN ("ZUN", "Zuni"),
  /** No linguistic content; Not applicable */
  ZXX ("ZXX", "No linguistic content; Not applicable"),
  /** Zaza; Dimili; Dimli; Kirdki; Kirmanjki; Zazaki */
  ZZA ("ZZA", "Zaza; Dimili; Dimli; Kirdki; Kirmanjki; Zazaki");

  private final String m_sID;
  private final String m_sDisplayName;

  EToopLanguageCode (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sDisplayName)
  {
    m_sID = sID;
    m_sDisplayName = sDisplayName;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }
}
