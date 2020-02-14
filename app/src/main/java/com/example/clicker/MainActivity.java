package com.example.clicker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private int imageId;

    Button clickButton;
    Button upgradeButton;
    Button autoUpgradeButton;
    TextView pointsTextView;
    TextView autoTextView;
    TextView pointsGainedTextView;

    // Used as timer for tasks that run constantly
    Handler handler = new Handler();

    // Used to play sound effects
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.coin);

        points = 0;
        autoRate = 0;
        currentAttack = 1;
        upgradeCost = 20;
        autoUpgradeCost = 50;

        clickButton = (Button) findViewById(R.id.click_button);
        upgradeButton = (Button) findViewById(R.id.upgrade_button);
        autoUpgradeButton = (Button) findViewById(R.id.auto_upgrade_button);
        pointsTextView = (TextView) findViewById(R.id.points_textview);
        autoTextView = (TextView) findViewById(R.id.auto_textview);
        pointsGainedTextView = (TextView) findViewById(R.id.points_gained_textview);


        handler.post(runnableCode);
    }

    /**
     * Run this code anytime a button is clicked
     */
    public void buttonClicked(View view) {
        switch (view.getId()) {

            // Run this code when the main button is clicked
            case R.id.click_button:
                // Add to points value the click value and set it to the points textview
                pointsTextView = (TextView) findViewById(R.id.points_textview);
                points += Integer.parseInt(clickButton.getText().toString());
                pointsTextView.setText(String.valueOf(Math.round(points)));

                // Enable visibility of floating points gained text to prepare it for animation
                pointsGainedTextView.setVisibility(View.VISIBLE);

                mp.start();

                // Move main button by translation to randomize where it will be next and add
                // floating and fading away text position to show points gained on last position
                // clicked
                int randomX = new Random().nextInt(160 + 160) - 160;
                int randomY = new Random().nextInt(160 + 160) - 160;
                pointsGainedTextView.setX(clickButton.getX());
                pointsGainedTextView.setY(clickButton.getY() - 80);
                clickButton.setTranslationX(randomX);
                clickButton.setTranslationY(randomY);

                // Start animation and disable visibility of floating text after animation
                Animation slideUpAnimation = AnimationUtils.loadAnimation(this,R.anim.up_slide_animation);
                pointsGainedTextView.startAnimation(slideUpAnimation);
                pointsGainedTextView.setVisibility(View.INVISIBLE);
                break;

            // Run this code when the upgrade button is clicked
            case R.id.upgrade_button:
                if (points >= upgradeCost) {
                    currentAttack += 1;
                    currentAttack *= 1.2;
                    points -= upgradeCost;
                    upgradeCost *= 2;

                    // Set image based on which attack the current upgrade is on, so we
                    // do not have to hardcode changing the button image for each upgrade
                    imageId = this.getResources().getIdentifier(
                            "image"+currentAttack,
                            "drawable",
                            this.getPackageName());
                    clickButton.setBackgroundResource(imageId);

                    clickButton.setText(String.valueOf(currentAttack));
                    pointsGainedTextView.setText("+" + String.valueOf(currentAttack));
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
