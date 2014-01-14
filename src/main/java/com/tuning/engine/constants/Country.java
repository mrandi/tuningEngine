/*
 * 06.04.2013 | 12:28:28
 * Marcel Wieczorek
 * marcel@wieczorek-it.de
 */
package com.tuning.engine.constants;

import java.util.Locale;

/**
 * @author Marcel Wieczorek
 * @version 1.0
 * @since 1.0
 */
public enum Country {

    /** Germany de DE */
    GERMANY(Locale.GERMANY),

    /** Austria de AT */
    AUSTRIA(getLocale("de", "AT")),

    /** Switzerland de CH */
    SWITZERLAND(getLocale("de", "CH")),

    /**
     * France fr FR
     */
    FRANCE(Locale.FRANCE),

    /**
     * Japan ja JP
     */
    JAPAN(Locale.JAPAN),

    /**
     * Australia en AU
     */
    AUSTRALIA(getLocale("en", "AU")),

    /**
     * Canada en CA
     */
    CANADA(Locale.CANADA),

    /**
     * Canada fr CA
     */
    CANADA_FRENCH(Locale.CANADA_FRENCH),

    /**
     * Czech Republic cs CZ
     */
    CZECH_REPUBLIC(getLocale("cs", "CZ")),

    /**
     * Denmark da DK
     */
    DENMARK(getLocale("da", "DK")),

    /**
     * Finland fi FI
     */
    FINLAND(getLocale("fi", "FI")),

    /**
     * Greece el GR
     */
    GREECE(getLocale("el", "GR")),

    /**
     * Hungary hu HU
     */
    HUNGARY(getLocale("hu", "HU")),

    /**
     * Iceland is IS
     */
    ICELAND(getLocale("is", "IS")),

    /**
     * Ireland en IE
     */
    IRELAND(getLocale("en", "IE")),

    /**
     * Italy it IT
     */
    ITALY(Locale.ITALY),

    /**
     * Luxembourg fr LU
     */
    LUXEMBOURG(getLocale("fr", "LU")),

    /**
     * Mexico es MX
     */
    MEXICO(getLocale("es", "MX")),

    /**
     * Netherlands nl NL
     */
    NETHERLANDS(getLocale("nl", "NL")),

    /**
     * New Zealand en NZ
     */
    NEW_ZEALAND(getLocale("en", "NZ")),

    /**
     * Norway no NO
     */
    NORWAY(getLocale("no", "NO")),

    /**
     * Poland pl PL
     */
    POLAND(getLocale("pl", "PL")),

    /**
     * Portugal pt PT
     */
    PORTUGAL(getLocale("pt", "PT")),

    /**
     * Slovakia sk SK
     */
    SLOVAKIA(getLocale("sk", "SK")),

    /**
     * Spain es ES
     */
    SPAIN(getLocale("es", "ES")),

    /**
     * Sweden sv SE
     */
    SWEDEN(getLocale("sv", "SE")),

    /**
     * Turkey tr TR
     */
    TURKEY(getLocale("tr", "TR")),

    /**
     * United Kingdom en GB
     */
    UNITED_KINGDOM(Locale.UK),

    /**
     * United States en US
     */
    UNITED_STATES(Locale.US),

    /**
     * Israel iw IL
     */
    ISRAEL(getLocale("iw", "IL")),

    /**
     * Bulgaria bg BG
     */
    BULGARIA(getLocale("bg", "BG")),

    /**
     * Estonia et EE
     */
    ESTONIA(getLocale("et", "EE")),

    /**
     * India hi IN
     */
    INDIA(getLocale("hi", "IN")),

    /**
     * Croatia hr HR
     */
    CROATIA(getLocale("hr", "HR")),

    /**
     * Ukraine uk UA
     */
    UKRAINE(getLocale("uk", "UA")),

    /**
     * China zh CN
     */
    CHINA(Locale.CHINA);

    /**
     * Finds the {@link Country} for a given host.
     * 
     * @param host
     *            the host of the currently requested page
     * @return the {@link Country} for the given host; by default {@link #GERMANY}
     */
    public static final Country getByHost(final String host) {
	if (host != null && !host.isEmpty()) {
	    for (final Country elocale : values()) {
		if (host.toUpperCase().endsWith(elocale.locale.getCountry())) {
		    return elocale;
		}
	    }
	}
	return GERMANY;
    }

    /**
     * Finds the {@link Country} for a given ISO code
     * 
     * @param isoCode
     *            the ISO code of the country (e.g. DE for {@link #GERMANY})
     * @return the {@link Country} for the given ISO code; by default {@link #GERMANY}
     */
    public static final Country getByIsoCode(final String isoCode) {
	if (isoCode != null && !isoCode.isEmpty()) {
	    for (final Country elocale : values()) {
		if (elocale.locale.getCountry().equals(isoCode)) {
		    return elocale;
		}
	    }
	}
	return GERMANY;
    }

    /**
     * Finds the {@link Country} for a given language and ISO code
     * 
     * @param languageAndCountry
     *            the language and ISO code in the format xx_XX (e.g. de_DE for German_Germany)
     * @return the {@link Country} for the given language and ISO code; by default {@link #GERMANY}
     * @author Marcel Wieczorek
     * @since 1.0
     */
    public static final Country getByLanguageAndCountry(final String languageAndCountry) {
	if (languageAndCountry != null && !languageAndCountry.isEmpty()) {
	    final String[] parts = languageAndCountry.split("_");
	    if (parts != null && parts.length == 2) {
		for (final Country elocale : values()) {
		    final String compare = String.format("%s_%s", elocale.locale.getLanguage(), elocale.locale.getCountry());
		    if (compare.equals(languageAndCountry)) {
			return elocale;
		    }
		}
	    }
	}
	return GERMANY;
    }

    /**
     * Finds a {@link Locale} by language and country name
     * 
     * @param language
     *            the language code (e.g. de)
     * @param country
     *            the country code (e.g. DE)
     * @return the matching {@link Locale} from available ones
     */
    private static Locale getLocale(final String language, final String country) {
	if (language != null && country != null) {
	    for (final Locale locale : Locale.getAvailableLocales()) {
		if (locale.getLanguage().equals(language) && locale.getCountry().equals(country)) {
		    return locale;
		}
	    }
	}
	return GERMANY.getLocale();
    }

    private Locale locale;

    private Country(final Locale locale) {
	this.locale = locale;
    }

    /**
     * @return the {@link Locale}
     */
    public Locale getLocale() {
	return this.locale;
    }

}
