package com.prgm.eliotshekhtman.educreature;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * EduCreature by Eliot Seo Shekhtman, all rights reserved.
 *
 * Objective: Create a game to incentivize students to get better grades, with parents setting difficulty/cutoffs and then entering grades to unlock lootcrates for the students.
 * Lootcrates contain creatures, decorations (static and wearable), item boosts, food, and other such things.
 * Different items have different item rarities.
 * Students can battle each other with the creatures in non-graphic combat, leveling up their creatures if they win.
 */
public class MainActivity extends AppCompatActivity implements lootTable {

    public static String password; // Has to be entered by parents to open a lootcrate - "boop"
    public static int difficulty;

    public static int gold = 0;
    public static ArrayList<lootcrate> stash; // (click alt+enter to fix imports)
    public static ArrayList<creature> zoo;
    public static ArrayList<item> inventory;

    final Context context = this;
    private Button button;
    private boolean firsttime = true;
    public static boolean inzoo = false;
    
    public void onCreate(Bundle savedInstanceState) {
        stash = new ArrayList<lootcrate>();
        zoo = new ArrayList<creature>();
        inventory = new ArrayList<item>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadGame();

        if(firsttime)
            popUp("Welcome to Educreature!" + "\n" + "Type in your password!");
        firsttime = false;

        // components from main.xml
        button = (Button) findViewById(R.id.buttonPrompt);


        // add button listener
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.input_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final TextView promptText = (TextView) promptsView
                        .findViewById(R.id.textView1);
                promptText.setText("Input Password");

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // If password is correct, allows for the adding of grades for lootcrates
                                        if(password.equals(""))
                                            password = userInput.getText().toString();
                                        if(userInput.getText().toString().equals(password)) {
                                            inputGrade();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    // Uses a dialog box to input grades and get lootboxes
    public void inputGrade() {
        inzoo = false;
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView promptText = (TextView) promptsView
                .findViewById(R.id.textView1);
        promptText.setText("Input a Grade");

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and use it as a grade to calculate lootcrate gain
                                try {
                                    parseGrade(Integer.parseInt(userInput.getText().toString()));
                                }
                                catch(Exception e) {
                                    popUp("Please enter an integer, with no letters or symbols.");
                                }
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void popUp(String s) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.basic_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView promptText = (TextView) promptsView
                .findViewById(R.id.textView1);
        promptText.setText(s);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void parseGrade(int g) {
        lootcrate newcrate;
        switch (difficulty) {
            case 0: // Easiest difficulty, mainly for getting students to pass
                if(g > 55 & g < 105) {
                    newcrate = new lootcrate(((g - 55) / 10));
                    stash.add(newcrate);
                }
                else if(g > 100) {
                    newcrate = new lootcrate(4);
                    stash.add(newcrate);
                }
                break;
            case 1: // For struggling students who aren't quite A students
                newcrate = new lootcrate(0);
                if(g >= 95) {
                    newcrate = new lootcrate(4);
                }
                else if(g >= 90) {
                    newcrate = new lootcrate(3);
                }
                else if(g >= 85) {
                    newcrate = new lootcrate(2);
                }
                else if(g >= 80) {
                    newcrate = new lootcrate(1);
                }
                if(g >= 70) {
                    stash.add(newcrate);
                }
                break;
            case 2: // For A students who want an extra push
                newcrate = new lootcrate(0);
                if(g >= 97) {
                    newcrate = new lootcrate(4);
                }
                else if(g >= 95) {
                    newcrate = new lootcrate(3);
                }
                else if(g >= 93) {
                    newcrate = new lootcrate(2);
                }
                else if(g >= 90) {
                    newcrate = new lootcrate(1);
                }
                if(g >= 85) {
                    stash.add(newcrate);
                }
                break;
        }
        updateView();
    }

    public void openCrate(View view) {
        inzoo = false;
        openCrate();
        updateView();
    }

    public void openCrate() {
        openCrate(0);
    }

    public void openCrate(int c) {
        if (stash.size() > 0) {
            String[] rewards = stash.get(c).opencrate();
            Object[] rew = new Object[rewards.length];
            for(int i = 0; i < rewards.length; i++) {
                if(rewards[i].charAt(0) == 'c') {
                    rew[i] = cite[rewardFind(rewards[i].substring(1), cref)];
                }
                else if(rewards[i].charAt(0) == 'u') {
                    rew[i] = uite[rewardFind(rewards[i].substring(1), uref)];
                }
                else if(rewards[i].charAt(0) == 'r') {
                    rew[i] = rite[rewardFind(rewards[i].substring(1), rref)];
                }
                else {
                    rew[i] = aite[rewardFind(rewards[i].substring(1), aref)];
                }
            }
            for(int i = 0; i < rew.length; i++) {
                if(rew[i] instanceof Integer) {
                    gold += (int) rew[i];
                }
                else if(rew[i] instanceof creature) {
                    //zoo.add((creature) rew[i]);
                    if(rew[i] instanceof Sheeper) {
                        zoo.add(new Sheeper());
                    }
                    else if(rew[i] instanceof Human) {
                        zoo.add(new Human());
                    }
                }
                else if(rew[i] instanceof item) {
                    inventory.add((item) rew[i]);
                }
            }
            stash.remove(c);
        }
        else
            popUp("Oops!  No crates in storage.");
        updateView();
    }

    private static int rewardFind(String s, String[] a) {
        int i = 0;
        while( !a[i].equals(s) ) {
            i++;
        }
        return i;
    }

    public void loadGame() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        password = (String) prefs.getString("pswd", "");
        difficulty = (int) prefs.getInt("diff", 0);
    }

    public void saveGame() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pswd", (String) password);
        editor.putInt("diff", (int) difficulty);
        editor.commit();
    }


    public void updateView() {
        //saveGame();
        final TextView goldtext = (TextView) findViewById(R.id.gold);
        goldtext.setText("Gold: " + gold);
    }


    public void updateView(View view) {
        updateView();
    }


    public void checkInventory( View view ) {
        saveGame();
        updateView();
        button = (Button) findViewById(R.id.buttonPrompt);
        final Button invbut; final Button zoobut; final TextView goldee; final Button back; final Button open;
        invbut = (Button) findViewById(R.id.inventory);
        zoobut = (Button) findViewById(R.id.zoo);
        goldee = (TextView) findViewById(R.id.gold);
        open = (Button) findViewById(R.id.opncrt);
        back = (Button) findViewById(R.id.backbutton);


        open.setVisibility(View.GONE);
        invbut.setVisibility(View.GONE);
        zoobut.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        String s = "";
        for(int i = 0; i < stash.size(); i++) {
            s += stash.get(i).toString() + "\n";
        }
        for(int i = 0; i < inventory.size(); i++) {
            s += inventory.get(i).toString() + "\n";
        }
        goldee.setText(s);

        // add button listener
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                back.setVisibility(View.GONE);
                invbut.setVisibility(View.VISIBLE);
                zoobut.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                open.setVisibility((View.VISIBLE));
                goldee.setText("");
            }
        });

    }

    public void checkZoo( View view ) {
        saveGame();
        updateView();
        /*
        button = (Button) findViewById(R.id.buttonPrompt);
        final Button invbut; final Button zoobut; final TextView goldee; final Button back; final Button open;
        invbut = (Button) findViewById(R.id.inventory);
        zoobut = (Button) findViewById(R.id.zoo);
        goldee = (TextView) findViewById(R.id.gold);
        open = (Button) findViewById(R.id.opncrt);
        back = (Button) findViewById(R.id.backbutton);


        open.setVisibility(View.GONE);
        invbut.setVisibility(View.GONE);
        zoobut.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        String s = "";
        for(int i = 0; i < zoo.size(); i++) {
            s += zoo.get(i).toString() + "\n";
        }
        goldee.setText(s);

        // add button listener
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                back.setVisibility(View.GONE);
                invbut.setVisibility(View.VISIBLE);
                zoobut.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                open.setVisibility((View.VISIBLE));
                goldee.setText("");
            }
        });
        */
        saveGame();
        inzoo = true;
        Intent intent = new Intent(this, Zoo.class);
        startActivity(intent);

    }

    /*
    public void startQuest( View view ) {
        saveGame(); updateView();
        questing = true;
        int hhealth = health;
        Intent intent = new Intent(this, Questing.class);
        startActivity(intent);

    }
    */
}
