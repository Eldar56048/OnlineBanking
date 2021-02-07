package com.example.OnlineBanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "card_history")
public class History {
    @Id
    @GeneratedValue(generator = "sequence-history-generator")
    @GenericGenerator(
            name = "sequence-history-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "history_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "100"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @ManyToOne
    private Card card;
    private Transaction transaction;
    private Type paymentType;
    private float cash;
    private float commission;
    private String toCard;

    public History(){}

    public History(Card card,Transaction transaction,Type paymentType,float cash,float commission){
        this.card = card;
        this.transaction = transaction;
        this.paymentType = paymentType;
        this.cash = cash;
        this.commission = commission;
    }
    public History(Card card,Transaction transaction,Type paymentType,float cash,float commission,String toCard){
        this.card = card;
        this.transaction = transaction;
        this.paymentType = paymentType;
        this.cash = cash;
        this.commission = commission;
        this.toCard = toCard;
    }
    public String getToCard() {
        return toCard;
    }

    public void setToCard(String to) {
        this.toCard = to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Type getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Type paymentType) {
        this.paymentType = paymentType;
    }

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public float getOutcome(){
        return (cash+(cash*(commission)));
    }
}
