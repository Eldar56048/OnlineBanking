package com.example.OnlineBanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(generator = "sequence-card-generator")
    @GenericGenerator(
            name = "sequence-card-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "card_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "100"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    private Long id;

    private String cardNumber;

    private int cash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Card(){}

    public Card(String cardNumber, int cash, User user) {
        this.cardNumber = cardNumber;
        this.cash = cash;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public float getDollars(){
        return (float)(cash)/(float) 400;
    }
    public float getEuro(){
        return (float)(cash)/(float) 500;
    }

    public void addMoneyToCard(float cash){
        this.cash+=cash;
    }
    public void minusMoney(float cash){this.cash-=cash;}
}
