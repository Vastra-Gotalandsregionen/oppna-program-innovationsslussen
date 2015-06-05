/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */
package se.vgregion.service.innovationsslussen.ldap;


import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

/**
 * Value object for information stored in the user ldap database. The properties are supposed to match those in the
 * db. This enables the LdapService to use this as an structure for searching and packing results from queries from
 * this db.
 *
 * @author claes.lundahl
 */
public abstract class Person {

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public IdeaPerson.Gender getGender() {

        final int hsaPersonIdentityNumberMaxLength = 12;

        String personalNumber = getPersonalNumber();

        if (personalNumber == null
                || "".equals(personalNumber)
                || personalNumber.length() != hsaPersonIdentityNumberMaxLength) {
            return IdeaPerson.Gender.UNKNOWN;
        }

        final int hsaPersonIdentityNumberLength = 10;
        char c = getPersonalNumber().charAt(hsaPersonIdentityNumberLength);
        if (!Character.isDigit(c)) {
            return IdeaPerson.Gender.UNKNOWN;
        }
        int i = Integer.parseInt(Character.toString(c));
        if (i % 2 == 0) {
            return IdeaPerson.Gender.FEMALE;
        }
        return IdeaPerson.Gender.MALE;
    }

    /**l
     * Gets the birth year.
     *
     * @return the birth year
     */
    public Short getBirthYear() {

        final int three = 3;
        final int four = 4;

        String personalNumber = getPersonalNumber();

        if (personalNumber != null && personalNumber.length() > three
                && Character.isDigit(personalNumber.charAt(0))
                && Character.isDigit(personalNumber.charAt(1))
                && Character.isDigit(personalNumber.charAt(2))
                && Character.isDigit(personalNumber.charAt(three))) {
            return Short.parseShort(personalNumber.substring(0, four));
        }
        return null;
    }


    public abstract String getPersonalNumber();

}
