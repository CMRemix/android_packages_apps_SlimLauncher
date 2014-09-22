package com.slim.slimlauncher.settings;

import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.slim.slimlauncher.DeviceProfile;
import com.slim.slimlauncher.DynamicGrid;
import com.slim.slimlauncher.LauncherAppState;
import com.slim.slimlauncher.R;
import com.slim.slimlauncher.preference.DoubleNumberPickerPreference;
import com.slim.slimlauncher.preference.NumberPickerPreference;

/**
 * Created by gmillz on 9/7/14.
 */
public class SettingsFragment extends PreferenceFragment {

    private NumberPickerPreference mDockIcons;
    private DoubleNumberPickerPreference mHomescreenGrid;
    private DoubleNumberPickerPreference mDrawerGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        DynamicGrid grid = LauncherAppState.getInstance().getDynamicGrid();

        mDockIcons = (NumberPickerPreference)
                findPreference(SettingsProvider.KEY_DOCK_ICONS);
        mHomescreenGrid = (DoubleNumberPickerPreference)
                findPreference(SettingsProvider.KEY_HOMESCREEN_GRID);
        mDrawerGrid = (DoubleNumberPickerPreference)
                findPreference(SettingsProvider.KEY_DRAWER_GRID);

        Preference gestures = findPreference("gestures");
        gestures.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getFragmentManager().beginTransaction().replace(
                        android.R.id.content, new GestureFragment()).commit();
                return true;
            }
        });


        if (grid != null) {
            DeviceProfile prof = grid.getDeviceProfile();
            prof.updateFromPreferences(getActivity());

            if (SettingsProvider.getCellCountX(getActivity(),
                    SettingsProvider.KEY_HOMESCREEN_GRID, 0) < 1) {
                SettingsProvider.putCellCountX(getActivity(),
                        SettingsProvider.KEY_HOMESCREEN_GRID, (int) prof.numColumns);
                mHomescreenGrid.setDefault2((int) prof.numColumns);
            }
            if (SettingsProvider.getCellCountY(getActivity(),
                    SettingsProvider.KEY_HOMESCREEN_GRID, 0) < 1) {
                SettingsProvider.putCellCountY(getActivity(),
                        SettingsProvider.KEY_HOMESCREEN_GRID, (int) prof.numRows);
                mHomescreenGrid.setDefault1((int) prof.numRows);
            }
            if (SettingsProvider.getCellCountX(getActivity(),
                    SettingsProvider.KEY_DRAWER_GRID, 0) < 1) {
                SettingsProvider.putCellCountX(getActivity(),
                        SettingsProvider.KEY_DRAWER_GRID, prof.allAppsNumCols);
                mDrawerGrid.setDefault2(prof.allAppsNumCols);
            }
            if (SettingsProvider.getCellCountY(getActivity(),
                    SettingsProvider.KEY_DRAWER_GRID, 0) < 1) {
                SettingsProvider.putCellCountY(getActivity(),
                        SettingsProvider.KEY_DRAWER_GRID, prof.allAppsNumRows);
                mDrawerGrid.setDefault2(prof.allAppsNumRows);
            }
            if (SettingsProvider.getInt(getActivity(),
                    SettingsProvider.KEY_DOCK_ICONS, 0) < 1) {
                SettingsProvider.putInt(getActivity(),
                        SettingsProvider.KEY_DOCK_ICONS, (int) prof.numHotseatIcons);
                mDockIcons.setDefaultValue((int) prof.numHotseatIcons);
            }

        }

    }
}
