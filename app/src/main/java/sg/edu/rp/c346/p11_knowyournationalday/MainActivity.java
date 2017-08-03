package sg.edu.rp.c346.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static android.text.TextUtils.concat;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> al = new ArrayList<String>();
    AlertDialog dialog;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        al.clear();
        al.add("Singapore National Day is on 9 August.");
        al.add("Singapore is 52 years old.");
        al.add("Theme is #OneNationTogether .");

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase =
                (LinearLayout) inflater.inflate(R.layout.passphrase, null);
        final EditText etPassphrase = (EditText) passPhrase
                .findViewById(R.id.editTextPassPhrase);

        dialog = new AlertDialog.Builder(this)
                .setView(passPhrase)
                .setTitle("Please Login")
                .setPositiveButton("OK", null) //Set to null. We override the onclick
                .setNegativeButton("NO ACCESS CODE", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button buttonPositive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button buttonNegative = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                buttonPositive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (etPassphrase.getText().toString().equals("738964")) {
                            SharedPreferences prefs = PreferenceManager
                                    .getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor prefEdit = prefs.edit();
                            prefEdit.putString("key", "738964");
                            prefEdit.commit();
                            dialog.cancel();
                        }
                    }
                });

                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });



        dialog.show();

        lv = (ListView)findViewById(R.id.lv);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, al);

        lv.setAdapter(aa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit")
                    .setMessage("Are you sure?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (id == R.id.action_send) {
            final String [] list = new String[] { "Email", "SMS" };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                LayoutInflater inflater = (LayoutInflater)
                                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout email =
                                        (LinearLayout) inflater.inflate(R.layout.email, null);
                                final EditText etEmail = (EditText) email
                                        .findViewById(R.id.editTextEmail);

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Email")
                                        .setView(email)
                                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                String strEmail = etEmail.getText().toString();
                                                StringBuilder messages = new StringBuilder();
                                                for (int count=0; count < al.size(); count++) {
                                                    messages.append(al.get(count) + "\n");
                                                }

                                                Intent email = new Intent(Intent.ACTION_SEND);
                                                // Put essentials like email address, subject & body text
                                                email.putExtra(Intent.EXTRA_EMAIL,
                                                        new String[]{strEmail});
                                                email.putExtra(Intent.EXTRA_SUBJECT,
                                                        "P11-Know Your National Day");
                                                email.putExtra(Intent.EXTRA_TEXT,
                                                        String.valueOf(messages));
                                                // This MIME type indicates email
                                                email.setType("message/rfc822");
                                                // createChooser shows user a list of app that can handle
                                                // this MIME type, which is, email
                                                startActivity(Intent.createChooser(email,
                                                        "Choose an Email client :"));
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            } else if (which == 1) {
                                LayoutInflater inflater = (LayoutInflater)
                                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout sms =
                                        (LinearLayout) inflater.inflate(R.layout.sms, null);
                                final EditText etNumber = (EditText) sms
                                        .findViewById(R.id.editTextNumber);

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("SMS")
                                        .setView(sms)
                                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                String strNum = etNumber.getText().toString();
                                                StringBuilder messages = new StringBuilder();
                                                for (int count=0; count < al.size(); count++) {
                                                    messages.append(al.get(count) + "\n");
                                                }

                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(strNum, null, String.valueOf(messages), null, null);
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (id == R.id.action_quiz) {

            score = 0;

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            final RadioGroup rg1 = (RadioGroup)quiz.findViewById(R.id.rg1);
            final RadioGroup rg2 = (RadioGroup)quiz.findViewById(R.id.rg2);
            final RadioGroup rg3 = (RadioGroup)quiz.findViewById(R.id.rg3);



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(quiz)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int selectedButtonId1 = rg1.getCheckedRadioButtonId();
                            int selectedButtonId2 = rg2.getCheckedRadioButtonId();
                            int selectedButtonId3 = rg3.getCheckedRadioButtonId();

                            if (selectedButtonId1 == -1 || selectedButtonId2 == -1 || selectedButtonId3 == -1) {
                                Toast.makeText(MainActivity.this, "You did not complete the quiz. Try again.", Toast.LENGTH_LONG).show();

                            } else {
                                if (selectedButtonId1 == R.id.radioButtonNo1) {
                                    score = score + 1 ;
                                }

                                if (selectedButtonId2 == R.id.radioButtonYes2) {
                                    score = score + 1;
                                }

                                if (selectedButtonId3 == R.id.radioButtonYes3) {
                                    score = score + 1;
                                }

                                Toast.makeText(MainActivity.this, "Your total score is " + String.valueOf(score)+ ".", Toast.LENGTH_LONG).show();
                            }


                        }
                    })

                    .setNegativeButton("Don't Know", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "You failed the test.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String key = prefs.getString("key", "no key");

        // Checks if user has logged in before
        if (key.equals("738964")) {
            dialog.cancel();
        }

    }
}
