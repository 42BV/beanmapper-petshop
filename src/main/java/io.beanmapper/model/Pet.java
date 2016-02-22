package io.beanmapper.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Pet extends BaseModel {

    public enum Sex {
        MALE("Mannelijk"), FEMALE("Vrouwelijk"), NEUTRAL("Onzijdig"), HERMAPHRODITIC("Tweeslachtig");

        private final String text;

        Sex(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private String nickname;
    private LocalDate birthDate;
    private Sex sex;
    @ManyToOne
    private PetType type;
    @ManyToOne
    private Owner owner;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
