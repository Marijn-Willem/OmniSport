package com.sports.entity;

import com.sports.entity.key.DoublePersonSport1PersonSport2Key;

public class Double extends Participant {
    private int personSport1Id;
    private int personSport2Id;

    private Person person1;
    private Person person2;

    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                "" + personSport1Id,
                "" + personSport2Id
        };
    }

    public DoublePersonSport1PersonSport2Key getPerson1Person2Key() {
        return new DoublePersonSport1PersonSport2Key(personSport1Id, personSport2Id);
    }

    public int getPersonSport1Id() {
        return personSport1Id;
    }

    public void setPersonSport1Id(int personSport1Id) {
        this.personSport1Id = personSport1Id;
    }

    public int getPersonSport2Id() {
        return personSport2Id;
    }

    public void setPersonSport2Id(int personSport2Id) {
        this.personSport2Id = personSport2Id;
    }

    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }
}
