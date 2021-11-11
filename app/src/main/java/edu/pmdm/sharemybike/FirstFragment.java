package edu.pmdm.sharemybike;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.pmdm.sharemybike.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String date = String.format("%d/%d/%d", dayOfMonth, month, year);
            binding.textviewFirst.setText(String.format("Date %s",date));
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.chosen_date), date);
            editor.apply();
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_itemFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}