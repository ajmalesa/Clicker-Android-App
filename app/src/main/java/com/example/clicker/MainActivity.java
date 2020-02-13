package com.example.clicker;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private float points;
    private int currentAttack;
    private float autoRate;
    private int upgradeCost;
    private int autoUpgradeCost;

    Button clickButton;
    Button upgradeButton;
    Button autoUpgradeButton;
    TextView pointsTextView;
    TextView autoTextView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        points = 0;
        autoRate = 0;
        currentAttack = 1;
        upgradeCost = 20;
        autoUpgradeCost = 50;

        clickButton = (Button) findViewById(R.id.click_button);
        upgradeButton = (Button) findViewById(R.id.upgrade_button);
        autoUpgradeButton = (Button) findViewById(R.id.auto_upgrade_button);
        pointsTextView = (TextView) findViewById((R.id.points_textview));
        autoTextView = (TextView) findViewById((R.id.auto_textview));

        handler.post(runnableCode);
    }

    /**
     * Run this code anytime a button is clicked
     */
    public void buttonClicked(View view) {
        switch (view.getId()) {

            // Run this code when the main button is clicked
            case R.id.click_button:
                pointsTextView = (TextView) findViewById(R.id.points_textview);
                points += Integer.parseInt(clickButton.getText().toString());
                pointsTextView.setText(String.valueOf(Math.round(points)));

                // Move main button by translation to randomize where it will be next
                int randomX = new Random().nextInt(160 + 160) - 160;
                int randomY = new Random().nextInt(160 + 160) - 160;
                clickButton.setTranslationX(randomX);
                clickButton.setTranslationY(randomY);
                break;

            // Run this code when the upgrade button is clicked
            case R.id.upgrade_button:
                if (points >= upgradeCost) {
                    currentAttack += 1;
                    currentAttack *= 1.2;
                    points -= upgradeCost;
                    upgradeCost *= 2;
                    clickButton.setText(String.valueOf(currentAttack));
                }
                break;

            // Run this code when auto upgrade button is clicked
            case R.id.auto_upgrade_button:
                if (points >= autoUpgradeCost) {
                    autoRate += 1;
                    points -= autoUpgradeCost;
                    autoUpgradeCost *= 1.6;
                    autoUpgradeButton.setText("auto rate +1 costs " + String.valueOf(autoUpgradeCost));
                }
                break;
        }
    }

    /**
     * Run this code every 4th of a second
     */
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if (points >= upgradeCost) {
                upgradeButton.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
            } else {
                upgradeButton.setBackgroundColor(getResources().getColor(R.color.buttonUnselectableBackground));
            }
            if (points >= autoUpgradeCost) {
                autoUpgradeButton.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
            } else {
                autoUpgradeButton.setBackgroundColor(getResources().getColor(R.color.buttonUnselectableBackground));
            }
            if (points >= 50) {
                autoUpgradeButton.setVisibility(View.VISIBLE);
            }
            if (autoRate >= 1) {
                autoTextView.setVisibility(View.VISIBLE);
            }

            points += (autoRate / 8);

            upgradeButton.setText("upgrade costs " + String.valueOf(upgradeCost));
            autoUpgradeButton.setText("auto rate +1 costs " + String.valueOf(autoUpgradeCost));
            pointsTextView.setText(String.valueOf(Math.round(points)));
            autoTextView.setText("+" + String.valueOf(Math.round(autoRate)) + "/s");

            handler.postDelayed(this, 125);
        }
    };

}
