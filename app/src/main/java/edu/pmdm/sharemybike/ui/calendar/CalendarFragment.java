package edu.pmdm.sharemybike.ui.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import edu.pmdm.sharemybike.FirstFragment;
import edu.pmdm.sharemybike.R;
import edu.pmdm.sharemybike.User;
import edu.pmdm.sharemybike.databinding.FragmentCalendarBinding;


public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        showWelcome();
        binding.calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String date = String.format("%d/%d/%d", dayOfMonth, month, year);
            binding.textviewFirst.setText(String.format("Date %s",date));
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.chosen_date), date);
            editor.apply();
            NavHostFragment.findNavController(CalendarFragment.this)
                    .navigate(R.id.action_nav_calendar_to_nav_bike);
        });
        return root;
    }

    private void showWelcome() {
        User user = User.getInstance();
        String welcomeText = String.format("Welcome %s! [%s]", user.getName(), user.getEmail());
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getContext(), welcomeText, duration);
        toast.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}