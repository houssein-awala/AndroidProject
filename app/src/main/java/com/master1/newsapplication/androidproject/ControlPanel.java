package com.master1.newsapplication.androidproject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobeta.android.dslv.DragSortListView;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerPreference;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class ControlPanel extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
   public static String background_color,toolbar_colorr,toolbar_color,s,f;
   static Set<String> notifications;
    static Set<String> savecategory;
    static ArrayList<String> savedd;
   static ArrayList<String> categorie=new ArrayList<>();
   static Fragment fragment;
   public static ArrayList<String> getcategories()
   {

       return MainActivity.categories;
   }
   public ControlPanel()
   {}
   public ControlPanel(String background_color,String toolbar_color,Set<String> notifications)
   {
       this.background_color=background_color;
       this.toolbar_color=toolbar_color;
       this.notifications=notifications;
   }
   public static String getBackgroundcolor()
   {
       return background_color;
   }
   public String gettoolbarcolor()
   {
       return toolbar_color;
   }
   public void setBackground_color(String background_color)
   {
       this.background_color=background_color;
   }
   public void setToolbar_color(String toolbar_color)
   {
       this.toolbar_color=toolbar_color;
   }

   public void setNotifications(Set<String> notifications)
   {
       this.notifications=notifications;
   }
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
       // System.out.println(background_color);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @SuppressLint("ValidFragment")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
      SharedPreferences  pref;
      //  public static String background_color,toolbar_color,s,f;
        SharedPreferences.Editor editor;
        public static final String Mypref="pref";
        public  String color_back="color_back";
        public  String color_tool="color_tool";
        public ColorPickerPreference colorPickerPreferencebackground;
        public ColorPickerPreference colorPickerPreferencetoolbar;
        public DragSortListView dragSortListView;
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            ColorPickerView colorPickerView;
            pref=getPreferenceManager().getSharedPreferences();
          //  dragSortListView=(DragSortListView)getPreferenceManager().findPreference("drag");
            colorPickerPreferencebackground=(ColorPickerPreference)getPreferenceManager().findPreference("BackgroundColorPickerPreference");
            ColorPickerDialog.Builder builder=colorPickerPreferencebackground.getColorPickerDialogBuilder();

            colorPickerView = builder.getColorPickerView();
            colorPickerView.setColorListener(new ColorListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onColorSelected(ColorEnvelope colorEnvelope) {
                    int a = colorEnvelope.getColor();
                    s=colorEnvelope.getColorHtml();
                    //Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
                }
            });
            // colorPickerView.setFlagView(new CustomFlag(this.getContext(), R.layout.layout_flag));
            // colorPickerView.setFlagMode(FlagMode.ALWAYS);
            //   pref=getPreferenceManager().getSharedPreferences();
            background_color=colorPickerView.getSavedColorHtml(Color.WHITE);
          //  Toast.makeText(getContext(),background_color,Toast.LENGTH_SHORT).show();
            pref = getActivity().getSharedPreferences(Mypref,Context.MODE_PRIVATE);
            editor=pref.edit();
            editor.putString(color_back,background_color);
            editor.commit();
           // Toast.makeText(this.getContext(),"hiii"+pref.getString(color_back,""),Toast.LENGTH_SHORT).show();
            background_color=pref.getString(color_back,"");
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {

                    //System.out.println("hon");
                    dialog.dismiss();
                }
            });
           // System.out.println("back"+background_color);


            //Toolbar
            colorPickerPreferencetoolbar=(ColorPickerPreference)getPreferenceManager().findPreference("ToolbarColorPickerPreference");
            ColorPickerDialog.Builder builder1=colorPickerPreferencetoolbar.getColorPickerDialogBuilder();
            // builder.setFlagView(new CustomFlag(this,R.layout.la));
            colorPickerView = builder1.getColorPickerView();
            colorPickerView.setColorListener(new ColorListener() {
                @Override
                public void onColorSelected(ColorEnvelope colorEnvelope) {
                    int a = colorEnvelope.getColor();
                    f=colorEnvelope.getColorHtml();
                  // Toast.makeText(getContext(),f,Toast.LENGTH_SHORT).show();
                }
            });
            //     colorPickerView.setFlagView(new CustomFlag(this.getContext(), R.layout.layout_flag));
            //      colorPickerView.setFlagMode(FlagMode.ALWAYS);
            pref=getPreferenceManager().getSharedPreferences();
            toolbar_colorr=colorPickerView.getSavedColorHtml(Color.WHITE);
            pref = getActivity().getSharedPreferences(Mypref,Context.MODE_PRIVATE);
            editor=pref.edit();
            editor.putString(color_tool,toolbar_colorr);
            editor.commit();
           // Toast.makeText(this.getContext(),"hiii"+pref.getString(color_tool,""),Toast.LENGTH_SHORT).show();
            toolbar_color=pref.getString(color_tool,"");
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {

                    dialog.dismiss();
                }
            });
            //System.out.println("tool"+toolbar_color);



           try {
               FileOutputStream fout = getContext().openFileOutput("mot.txt", Context.MODE_PRIVATE);

                OutputStreamWriter osw = new OutputStreamWriter(fout);
                osw.write(background_color);
                osw.flush();
                osw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
           }

            try {
                FileOutputStream fout = getContext().openFileOutput("toolbar.txt", Context.MODE_PRIVATE);

                OutputStreamWriter osw = new OutputStreamWriter(fout);
                osw.write(toolbar_color);
                osw.flush();
                osw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), ControlPanel.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        SharedPreferences sharedPreferences;


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
            sharedPreferences=getPreferenceManager().getSharedPreferences();
            SharedPreferences.Editor editorr=sharedPreferences.edit();
              //  Toast.makeText(getContext(),"ooooooooooo",Toast.LENGTH_SHORT).show();
            //notification

            notifications=new HashSet<>();
            categorie=getcategories();
            for (String region : categorie)
                notifications.add(region);
          /*  for (String region : notifications)
                System.out.println(region);*/

            MultiSelectListPreference list=(MultiSelectListPreference)getPreferenceManager().findPreference("categories");
            list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    MultiSelectListPreference pref = (MultiSelectListPreference) preference;
                    HashSet<String> old=(HashSet<String>)pref.getValues();
                    HashSet<String> neww=(HashSet<String>)newValue;

                    for (String categorie:old)
                    {
                        if(!neww.contains(categorie))
                        {
                            sendToServer(categorie,"OFF");
                        }
                    }
                    for (String categorie:neww)
                    {
                        if(!old.contains(categorie))
                        {
                            sendToServer(categorie,"ON");
                        }
                    }
                    return true;
                }
                @SuppressLint("StaticFieldLeak")
                void sendToServer(final String categorie, final String option){
                    final String adress = "http://news-project.000webhostapp.com/editCategorie.php?token="
                            +FirebaseInstanceId.getInstance().getToken()
                            +"&categorie="+categorie
                            + "&option="+option;
                    //Toast.makeText(getContext(), adress, Toast.LENGTH_SHORT).show();
                    new Thread() {


                        @Override
                        public void run() {
                            super.run();
                            try {
                                URL url = new URL(adress);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");
                                connection.connect();
                                System.out.println(connection.getResponseMessage());
                               // System.out.println(adress);

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
            savecategory=  sharedPreferences.getStringSet("categories", (Set<String>) notifications);

            if(savecategory==null) {

                editorr.putStringSet("categories", notifications);
                editorr.commit();
                //System.out.println(savecategory.toString());
            }
            else{
                /*System.out.println("limaza");
                System.out.println(savecategory.toString());*/
            }
            Set<String> cat=notifications;

            CharSequence[] sequences = cat.toArray(new CharSequence[cat.size()]);
            try {
                FileOutputStream fout = getContext().openFileOutput("category.txt", Context.MODE_PRIVATE);

                OutputStreamWriter osw = new OutputStreamWriter(fout);

                osw.write(savecategory.toString());
                osw.flush();
                osw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.setEntries(sequences);
            list.setEntryValues(sequences);
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
      /*      for(String catt : savedd)
                System.out.println(catt);*/
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        public static Set<String> getNotifications()
        {
            return savecategory;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), ControlPanel.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), ControlPanel.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
