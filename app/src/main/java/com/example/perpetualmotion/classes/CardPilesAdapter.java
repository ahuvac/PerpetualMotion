package com.example.perpetualmotion.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perpetualmotion.R;
import com.example.perpetualmotion.interfaces.AdapterOnItemClickListener;
import com.mintedtech.perpetual_motion.pm_game.Card;

import java.util.Locale;

public class CardPilesAdapter extends RecyclerView.Adapter<cardPileTopViewHolder> {


    static AdapterOnItemClickListener sAdapterOnItemClickListener;
    //Adapter Primary Data Sourceprivate
    final Card[] mPILE_TOPS;
    // Arrays parallel to Adapter Primary Data Source
    // 1. which piles are checkedprivate
    final boolean[] mCHECKED_PILES;
    // 2. how many total cards are in each pileprivate
    final int[] mNUMBER_OF_CARDS_IN_PILE;
    // The message to show on the bottom of the outer card (e.g "Cards in Stack: ")
    private final String mMSG_CARDS_IN_STACK;

    public CardPilesAdapter(String mMSG_cards_in_stack) {
        final int NUMBER_OF_PILES = 4;
        mPILE_TOPS = new Card[NUMBER_OF_PILES];
        mCHECKED_PILES = new boolean[NUMBER_OF_PILES];
        mNUMBER_OF_CARDS_IN_PILE = new int[NUMBER_OF_PILES];


        mMSG_CARDS_IN_STACK = mMSG_cards_in_stack;
    }


    public void setOnItemClickListener(AdapterOnItemClickListener adapterOnItemClickListener) {
        sAdapterOnItemClickListener = adapterOnItemClickListener;
    }

    @NonNull
    @Override
    public cardPileTopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_pile_top_card, parent, false);
        return new cardPileTopViewHolder(itemView);
    }

    private void updateOuterCard(cardPileTopViewHolder holder, int position) {
        holder.tv_pile_card_cards_in_stack.setText(mMSG_CARDS_IN_STACK.concat(Integer.toString(mNUMBER_OF_CARDS_IN_PILE[position])));
    }

    private void updateInnerCard(cardPileTopViewHolder holder, int position) {
        if (mPILE_TOPS[position] != null) {
            populateAndShowInnerCard(holder, position);
        } else {
            clearAndHideInnerCard(holder);
        }
    }

    private void populateAndShowInnerCard(cardPileTopViewHolder holder, int position) {
        Card currentCard = mPILE_TOPS[position];
        setTextOfEachTextView(holder, currentCard);
        setColorOfAllTextViews(holder, currentCard);
        holder.cb_pile_card_checkbox.setChecked(mCHECKED_PILES[position]);
        holder.cv_pile_inner_Card.setVisibility(View.VISIBLE);
    }

    private void setTextOfEachTextView(cardPileTopViewHolder holder, Card currentCard) {
        String rankValue = String.format(Locale.getDefault(), "%d", currentCard.getRank().getValue());
        holder.tv_pile_card_rank_top.setText(rankValue);
        holder.tv_pile_card_suit_center.setText(Character.toString(currentCard.getSuit().getCharacter()));
        holder.tv_pile_card_name_bottom.setText(currentCard.getRank().toString());
    }

    private void setColorOfAllTextViews(cardPileTopViewHolder holder, Card currentCard) {
        int currentColor = currentCard.getSuit().getColor();
        holder.tv_pile_card_rank_top.setTextColor(currentColor);
        holder.tv_pile_card_suit_center.setTextColor(currentColor);
        holder.tv_pile_card_name_bottom.setTextColor(currentColor);
    }

    private void clearAndHideInnerCard(cardPileTopViewHolder holder) {
        holder.tv_pile_card_rank_top.setText("");
        holder.tv_pile_card_suit_center.setText("");
        holder.tv_pile_card_name_bottom.setText("");
        holder.cb_pile_card_checkbox.setChecked(false);
        holder.cv_pile_inner_Card.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mPILE_TOPS.length;
    }

    public void updatePile(int pileNumber, Card card, int numberOfCardsInStack) {
        mPILE_TOPS[pileNumber] = card;
        mNUMBER_OF_CARDS_IN_PILE[pileNumber] = numberOfCardsInStack;
        notifyItemChanged(pileNumber);
    }

    public Card getCardAt(int position) {
        return mPILE_TOPS[position] == null ? null : mPILE_TOPS[position].copy();
    }

    public void clearCheck(int position) {
        if (mCHECKED_PILES[position]) {
            mCHECKED_PILES[position] = false;
            notifyItemChanged(position);
        }
    }

    public void toggleCheck(int position) {
        mCHECKED_PILES[position] = !mCHECKED_PILES[position];
        notifyItemChanged(position);
    }

    public void overwriteChecksFrom(boolean[] newChecksSet) {
        System.arraycopy(newChecksSet, 0, mCHECKED_PILES, 0, mCHECKED_PILES.length);
    }

    public boolean[] getCheckedPiles() {
        return mCHECKED_PILES.clone();
    }

    @Override
    public void onBindViewHolder(@NonNull cardPileTopViewHolder holder, int position) {
        updateOuterCard(holder, position);
        updateInnerCard(holder, position);
    }


}
