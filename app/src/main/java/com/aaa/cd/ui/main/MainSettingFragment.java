package com.aaa.cd.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.aaa.cd.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainSettingFragment extends PreferenceFragmentCompat
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.preferences);

//        SwitchCompat ss = (SwitchCompat) getActivity().findViewById(R.id.switch_compat);
//        ss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Logger.d("SwitchCompat " + buttonView + " changed to " + isChecked);
//            }
//        });

/*        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.pref_key_save_mode));

        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {

            *//**
             * @param preference The changed Preference.
             * @param newValue   The new value of the Preference.
             * @return True to update the state of the Preference with the new value.
             *//*
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {

                boolean checked = Boolean.valueOf(newValue.toString());
                Toast.makeText(getActivity(), "checkboxChange", Toast.LENGTH_SHORT).show();
//                PrefUtils.setSaveNetMode(checked);
                return true;

            }
        });*/
    }
}