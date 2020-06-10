package br.eti.softlog.softlogtmsentregas;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

public class SettingsActivityMain extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings);

            Preference interval_sync = findPreference("interval_sync");

            Preference version = findPreference("version");

            try {
                String versionName = getActivity().getPackageManager()
                        .getPackageInfo(getActivity().getPackageName(), 0).versionName;
                version.setSummary(versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Preference buttonfeedback = findPreference(getString(R.string.send_feedback));
            buttonfeedback.setOnPreferenceClickListener(preference -> {
                Intent emailIntent =
                        new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "suporte@softlog.eti.br", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name) + " Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Escreva seu feedback aqui...");
                startActivity(Intent.createChooser(emailIntent, "Enviar email..."));

                return true;
            });
        }
    }
}