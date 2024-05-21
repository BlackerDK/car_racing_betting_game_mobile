package com.example.car_racing_betting_game_mobile;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class TimeDialogFragment extends DialogFragment {

    private static final String ARG_TIME_LEFT = "time_left";
    TextInputEditText tvCoins;
    Button enterButton;
    Button cancelButton;
    private TextView countdownTextView;
    private CountDownTimer countDownTimer;
    private long timeLeft;
    private OnAddCoinsListener listener;
    private final static int MAX_COINS = 100;

    public interface OnAddCoinsListener {
        void onAddCoins(int coins);
    }

    public void setOnAddCoinsListener(OnAddCoinsListener listener) {
        this.listener = listener;
    }

    public static TimeDialogFragment newInstance(long timeLeft) {
        TimeDialogFragment fragment = new TimeDialogFragment();
        Bundle args = new Bundle(); // Bundle is used to pass data between activities
        args.putLong(ARG_TIME_LEFT, timeLeft);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    // onCreateView is called to create the view for the fragment
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_addcoin, container, false);
        countdownTextView = view.findViewById(R.id.countdownTextView);
        tvCoins = view.findViewById(R.id.txtAddCoin);
        enterButton = view.findViewById(R.id.enterButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        if (getArguments() != null) {
            // get current time left
            timeLeft = getArguments().getLong(ARG_TIME_LEFT);
        }

        if (timeLeft > 0) {
            startCountDownTimer(timeLeft);
            // SET VISIBILITY OF PROPERTIES
            countdownTextView.setVisibility(View.VISIBLE);
            tvCoins.setVisibility(View.GONE);
            enterButton.setVisibility(View.GONE);
        } else {
            countdownTextView.setVisibility(View.GONE);
        }

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coinsText = tvCoins.getText().toString().trim();
                if (!TextUtils.isEmpty(coinsText) && Integer.parseInt(coinsText) <= MAX_COINS && Integer.parseInt(coinsText) > 0) {
                    int coinsToAdd = Integer.parseInt(coinsText); // Convert string to integer
                    if (listener != null) {
                        // implement onAddCoins method in MainActivity
                        listener.onAddCoins(coinsToAdd);
                    }
                    dismiss(); // close the dialog
                } else {
                    tvCoins.setError("Invalid coins amount (1 - 100) coins");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void startCountDownTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                countdownTextView.setText(String.format("Remaining time: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                countdownTextView.setVisibility(View.GONE);
                tvCoins.setVisibility(View.VISIBLE);
                enterButton.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }

}