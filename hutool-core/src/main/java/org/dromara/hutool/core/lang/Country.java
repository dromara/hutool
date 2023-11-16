

package org.dromara.hutool.core.lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 * 国家/地区，全称，缩写
 *
 * @author wh
 *
 */
public enum Country  {
    AFGHANISTAN("Afghanistan", "AF"),
    ÅLAND_ISLANDS("Åland Islands", "AX"),
    ALBANIA("Albania", "AL"),
    ALGERIA("Algeria", "DZ"),
    AMERICAN_SAMOA("American Samoa", "AS"),
    ANDORRA("Andorra", "AD"),
    ANGOLA("Angola", "AO"),
    ANGUILLA("Anguilla", "AI"),
    ANTARCTICA("Antarctica", "AQ"),
    ANTIGUA_AND_BARBUDA("Antigua and Barbuda", "AG"),
    ARGENTINA("Argentina", "AR"),
    ARMENIA("Armenia", "AM"),
    ARUBA("Aruba", "AW"),
    AUSTRALIA("Australia", "AU"),
    AUSTRIA("Austria", "AT"),
    AZERBAIJAN("Azerbaijan", "AZ"),
    BAHAMAS("Bahamas", "BS"),
    BAHRAIN("Bahrain", "BH"),
    BANGLADESH("Bangladesh", "BD"),
    BARBADOS("Barbados", "BB"),
    BELARUS("Belarus", "BY"),
    BELGIUM("Belgium", "BE"),
    BELIZE("Belize", "BZ"),
    BENIN("Benin", "BJ"),
    BERMUDA("Bermuda", "BM"),
    BHUTAN("Bhutan", "BT"),
    BOLIVIA_PLURINATIONAL_STATE_OF("Bolivia, Plurinational State of", "BO"),
    BONAIRE_SINT_EUSTATIUS_AND_SABA("Bonaire, Sint Eustatius and Saba", "BQ"),
    BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina", "BA"),
    BOTSWANA("Botswana", "BW"),
    BOUVET_ISLAND("Bouvet Island", "BV"),
    BRAZIL("Brazil", "BR"),
    BRITISH_INDIAN_OCEAN_TERRITORY("British Indian Ocean Territory", "IO"),
    BRUNEI_DARUSSALAM("Brunei Darussalam", "BN"),
    BULGARIA("Bulgaria", "BG"),
    BURKINA_FASO("Burkina Faso", "BF"),
    BURUNDI("Burundi", "BI"),
    CAMBODIA("Cambodia", "KH"),
    CAMEROON("Cameroon", "CM"),
    CANADA("Canada", "CA"),
    CANARY_ISLANDS("Canary Islands", "IC"),
    CAPE_VERDE("Cape Verde", "CV"),
    CAYMAN_ISLANDS("Cayman Islands", "KY"),
    CENTRAL_AFRICAN_REPUBLIC("Central African Republic", "CF"),
    CEUTA("Ceuta", "EA"),
    CHAD("Chad", "TD"),
    CHILE("Chile", "CL"),
    CHINA("China", "CN"),
    CHRISTMAS_ISLAND("Christmas Island", "CX"),
    COCOS_KEELING_ISLANDS("Cocos (Keeling) Islands", "CC"),
    COLOMBIA("Colombia", "CO"),
    COMOROS("Comoros", "KM"),
    CONGO("Congo", "CG"),
    CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE("Congo, The Democratic Republic of the", "CD"),
    COOK_ISLANDS("Cook Islands", "CK"),
    COSTA_RICA("Costa Rica", "CR"),
    CÔTE_DIVOIRE("Côte D'ivoire", "CI"),
    CROATIA("Croatia", "HR"),
    CUBA("Cuba", "CU"),
    CURAÇAO("Curaçao", "CW"),
    CYPRUS("Cyprus", "CY"),
    CZECH_REPUBLIC("Czech Republic", "CZ"),
    DENMARK("Denmark", "DK"),
    DJIBOUTI("Djibouti", "DJ"),
    DOMINICA("Dominica", "DM"),
    DOMINICAN_REPUBLIC("Dominican Republic", "DO"),
    ECUADOR("Ecuador", "EC"),
    EGYPT("Egypt", "EG"),
    EL_SALVADOR("El Salvador", "SV"),
    EQUATORIAL_GUINEA("Equatorial Guinea", "GQ"),
    ERITREA("Eritrea", "ER"),
    ESTONIA("Estonia", "EE"),
    ETHIOPIA("Ethiopia", "ET"),
    FALKLAND_ISLANDS_MALVINAS("Falkland Islands (Malvinas)", "FK"),
    FAROE_ISLANDS("Faroe Islands", "FO"),
    FIJI("Fiji", "FJ"),
    FINLAND("Finland", "FI"),
    FRANCE("France", "FR"),
    FRENCH_GUIANA("French Guiana", "GF"),
    FRENCH_POLYNESIA("French Polynesia", "PF"),
    FRENCH_SOUTHERN_TERRITORIES("French Southern Territories", "TF"),
    GABON("Gabon", "GA"),
    GAMBIA("Gambia", "GM"),
    GEORGIA("Georgia", "GE"),
    GERMANY("Germany", "DE"),
    GHANA("Ghana", "GH"),
    GIBRALTAR("Gibraltar", "GI"),
    GREECE("Greece", "GR"),
    GREENLAND("Greenland", "GL"),
    GRENADA("Grenada", "GD"),
    GUADELOUPE("Guadeloupe", "GP"),
    GUAM("Guam", "GU"),
    GUATEMALA("Guatemala", "GT"),
    GUERNSEY("Guernsey", "GG"),
    GUINEA("Guinea", "GN"),
    GUINEA_BISSAU("Guinea-Bissau", "GW"),
    GUYANA("Guyana", "GY"),
    HAITI("Haiti", "HT"),
    HEARD_ISLAND_AND_MCDONALD_ISLANDS("Heard Island and McDonald Islands", "HM"),
    HOLY_SEE_VATICAN_CITY_STATE("Holy See (Vatican City State)", "VA"),
    HONDURAS("Honduras", "HN"),
    HONG_KONG("Hong Kong", "HK"),
    HUNGARY("Hungary", "HU"),
    ICELAND("Iceland", "IS"),
    INDIA("India", "IN"),
    INDONESIA("Indonesia", "ID"),
    IRAN_ISLAMIC_REPUBLIC_OF("Iran, Islamic Republic of", "IR"),
    IRAQ("Iraq", "IQ"),
    IRELAND("Ireland", "IE"),
    ISLE_OF_MAN("Isle of Man", "IM"),
    ISRAEL("Israel", "IL"),
    ITALY("Italy", "IT"),
    JAMAICA("Jamaica", "JM"),
    JAPAN("Japan", "JP"),
    JERSEY("Jersey", "JE"),
    JORDAN("Jordan", "JO"),
    KAZAKHSTAN("Kazakhstan", "KZ"),
    KENYA("Kenya", "KE"),
    KIRIBATI("Kiribati", "KI"),
    KOREA_DEMOCRATIC_PEOPLES_REPUBLIC_OF("Korea, Democratic People's Republic of", "KP"),
    KOREA_REPUBLIC_OF("Korea, Republic of", "KR"),
    KUWAIT("Kuwait", "KW"),
    KYRGYZSTAN("Kyrgyzstan", "KG"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("Lao People's Democratic Republic", "LA"),
    LATVIA("Latvia", "LV"),
    LEBANON("Lebanon", "LB"),
    LESOTHO("Lesotho", "LS"),
    LIBERIA("Liberia", "LR"),
    LIBYA("Libya", "LY"),
    LIECHTENSTEIN("Liechtenstein", "LI"),
    LITHUANIA("Lithuania", "LT"),
    LUXEMBOURG("Luxembourg", "LU"),
    MACAO("Macao", "MO"),
    MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF("Macedonia, The Former Yugoslav Republic of", "MK"),
    MADAGASCAR("Madagascar", "MG"),
    MALAWI("Malawi", "MW"),
    MALAYSIA("Malaysia", "MY"),
    MALDIVES("Maldives", "MV"),
    MALI("Mali", "ML"),
    MALTA("Malta", "MT"),
    MARSHALL_ISLANDS("Marshall Islands", "MH"),
    MARTINIQUE("Martinique", "MQ"),
    MAURITANIA("Mauritania", "MR"),
    MAURITIUS("Mauritius", "MU"),
    MAYOTTE("Mayotte", "YT"),
    MEXICO("Mexico", "MX"),
    MICRONESIA_FEDERATED_STATES_OF("Micronesia, Federated States of", "FM"),
    MOLDOVA_REPUBLIC_OF("Moldova, Republic of", "MD"),
    MONACO("Monaco", "MC"),
    MONGOLIA("Mongolia", "MN"),
    MONTENEGRO("Montenegro", "ME"),
    MONTSERRAT("Montserrat", "MS"),
    MOROCCO("Morocco", "MA"),
    MOZAMBIQUE("Mozambique", "MZ"),
    MYANMAR("Myanmar", "MM"),
    NAMIBIA("Namibia", "NA"),
    NAURU("Nauru", "NR"),
    NEPAL("Nepal", "NP"),
    NETHERLANDS("Netherlands", "NL"),
    NEW_CALEDONIA("New Caledonia", "NC"),
    NEW_ZEALAND("New Zealand", "NZ"),
    NICARAGUA("Nicaragua", "NI"),
    NIGER("Niger", "NE"),
    NIGERIA("Nigeria", "NG"),
    NIUE("Niue", "NU"),
    NORFOLK_ISLAND("Norfolk Island", "NF"),
    NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands", "MP"),
    NORWAY("Norway", "NO"),
    OMAN("Oman", "OM"),
    PAKISTAN("Pakistan", "PK"),
    PALAU("Palau", "PW"),
    PALESTINE("Palestinian Territory, Occupied", "PS"),
    PANAMA("Panama", "PA"),
    PAPUA_NEW_GUINEA("Papua New Guinea", "PG"),
    PARAGUAY("Paraguay", "PY"),
    PERU("Peru", "PE"),
    PHILIPPINES("Philippines", "PH"),
    PITCAIRN("Pitcairn", "PN"),
    POLAND("Poland", "PL"),
    PORTUGAL("Portugal", "PT"),
    PUERTO_RICO("Puerto Rico", "PR"),
    QATAR("Qatar", "QA"),
    RÉUNION("Réunion", "RE"),
    ROMANIA("Romania", "RO"),
    RUSSIAN_FEDERATION("Russian Federation", "RU"),
    RWANDA("Rwanda", "RW"),
    SAINT_BARTHÉLEMY("Saint Barthélemy", "BL"),
    SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA("Saint Helena, Ascension and Tristan Da Cunha", "SH"),
    SAINT_KITTS_AND_NEVIS("Saint Kitts and Nevis", "KN"),
    SAINT_LUCIA("Saint Lucia", "LC"),
    SAINT_MARTIN_FRENCH_PART("Saint Martin (French Part)", "MF"),
    SAINT_PIERRE_AND_MIQUELON("Saint Pierre and Miquelon", "PM"),
    SAINT_VINCENT_AND_THE_GRENADINES("Saint Vincent and The Grenadines", "VC"),
    SAMOA("Samoa", "WS"),
    SAN_MARINO("San Marino", "SM"),
    SAO_TOME_AND_PRINCIPE("Sao Tome and Principe", "ST"),
    SAUDI_ARABIA("Saudi Arabia", "SA"),
    SENEGAL("Senegal", "SN"),
    SERBIA("Serbia", "RS"),
    SEYCHELLES("Seychelles", "SC"),
    SIERRA_LEONE("Sierra Leone", "SL"),
    SINGAPORE("Singapore", "SG"),
    SINT_MAARTEN_DUTCH_PART("Sint Maarten (Dutch Part)", "SX"),
    SLOVAKIA("Slovakia", "SK"),
    SLOVENIA("Slovenia", "SI"),
    SOLOMON_ISLANDS("Solomon Islands", "SB"),
    SOMALIA("Somalia", "SO"),
    SOUTH_AFRICA("South Africa", "ZA"),
    SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("South Georgia and The South Sandwich Islands", "GS"),
    SOUTH_SUDAN("South Sudan", "SS"),
    SPAIN("Spain", "ES"),
    SRI_LANKA("Sri Lanka", "LK"),
    SUDAN("Sudan", "SD"),
    SURINAME("Suriname", "SR"),
    SVALBARD_AND_JAN_MAYEN("Svalbard and Jan Mayen", "SJ"),
    SWAZILAND("Swaziland", "SZ"),
    SWEDEN("Sweden", "SE"),
    SWITZERLAND("Switzerland", "CH"),
    SYRIAN_ARAB_REPUBLIC("Syrian Arab Republic", "SY"),
    TAIWAN_PROVINCE_OF_CHINA("Taiwan, Province of China", "Taiwan", "TW"),
    TAJIKISTAN("Tajikistan", "TJ"),
    TANZANIA_UNITED_REPUBLIC_OF("Tanzania, United Republic of", "TZ"),
    THAILAND("Thailand", "TH"),
    TIMOR_LESTE("Timor-Leste", "TL"),
    TOGO("Togo", "TG"),
    TOKELAU("Tokelau", "TK"),
    TONGA("Tonga", "TO"),
    TRINIDAD_AND_TOBAGO("Trinidad and Tobago", "TT"),
    TUNISIA("Tunisia", "TN"),
    TURKEY("Turkey", "TR"),
    TURKMENISTAN("Turkmenistan", "TM"),
    TURKS_AND_CAICOS_ISLANDS("Turks and Caicos Islands", "TC"),
    TUVALU("Tuvalu", "TV"),
    UGANDA("Uganda", "UG"),
    UKRAINE("Ukraine", "UA"),
    UNITED_ARAB_EMIRATES("United Arab Emirates", "AE"),
    UNITED_KINGDOM("United Kingdom", "GB"),
    UNITED_STATES("United States", "US"),
    UNITED_STATES_MINOR_OUTLYING_ISLANDS("United States Minor Outlying Islands", "UM"),
    URUGUAY("Uruguay", "UY"),
    UZBEKISTAN("Uzbekistan", "UZ"),
    VANUATU("Vanuatu", "VU"),
    VENEZUELA_BOLIVARIAN_REPUBLIC_OF("Venezuela, Bolivarian Republic of", "VE"),
    VIET_NAM("Viet Nam", "Vietnam", "VN"),
    VIRGIN_ISLANDS_BRITISH("Virgin Islands, British", "VG"),
    VIRGIN_ISLANDS_U_S_("Virgin Islands, U.S.", "VI"),
    WALLIS_AND_FUTUNA("Wallis and Futuna", "WF"),
    WESTERN_SAHARA("Western Sahara", "EH"),
    YEMEN("Yemen", "YE"),
    ZAMBIA("Zambia", "ZM"),
    ZIMBABWE("Zimbabwe", "ZW");

    private final String _code;
    private final String _displayName;
    private final String _altDisplayName;
    private final Set<String> _allNames;

    private Country(String pDisplayName, String pCode) {
        this(pDisplayName, (String)null, pCode);
    }

    private Country(String pDisplayName, String pAltDisplayName, String pCode) {
        this._code = pCode;
        this._displayName = pDisplayName;
        this._altDisplayName = pAltDisplayName;
        this._allNames = new HashSet();
        this._allNames.add(pDisplayName.toLowerCase());
        this._allNames.add(pCode.toLowerCase());
        if (pAltDisplayName != null) {
            this._allNames.add(pAltDisplayName.toLowerCase());
        }

    }

    public String getCode() {
        return this._code;
    }

    public String getAltDisplayName() {
        return this._altDisplayName;
    }

    public String getDisplayName() {
        return this._displayName;
    }

    public static Country findByCodeOrDisplayName(String pCodeOrDisplayName) {
        String name = pCodeOrDisplayName.toLowerCase();
        return (Country)Arrays.stream(values()).filter((c) -> {
            return c._allNames.contains(name);
        }).findFirst().orElseThrow(() -> {
            return new IllegalArgumentException("Invalid code/display name: " + pCodeOrDisplayName);
        });
    }

    public static Country findCountryByCode(String pCode) {
        String code = pCode.toUpperCase();
        return (Country)Arrays.stream(values()).filter((c) -> {
            return c._code.equals(code);
        }).findFirst().orElseThrow(() -> {
            return new IllegalArgumentException("Invalid code: " + pCode);
        });
    }

    public static Optional<String> findByCodeOrDisplayNameSafe(String pCodeOrDisplayName) {
        try {
            if (pCodeOrDisplayName != null) {
                Country country = findByCodeOrDisplayName(pCodeOrDisplayName);
                return Optional.ofNullable(country.getDisplayName());
            }
        } catch (IllegalArgumentException var2) {
        }

        return Optional.empty();
    }
}
