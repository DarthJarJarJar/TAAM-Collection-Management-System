package com.example.b07demosummer2024;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ReportFragmentView extends Fragment {

    private ReportFragmentPresenter presenter;

    Spinner detailSpinner;
    Spinner reportTypeSpinner;
    EditText editTextReportParameter;
    TextView detailTextView;
    CheckBox descriptionAndImageOnlyCheckbox;
    Button generateButton;

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        presenter = new ReportFragmentPresenter(this, new ReportFragmentModel());

        reportTypeSpinner = view.findViewById(R.id.reportType);
        detailSpinner = view.findViewById(R.id.detailSpinner);
        editTextReportParameter = view.findViewById(R.id.editTextReportParamter);
        detailTextView = view.findViewById(R.id.detailTextView);
        descriptionAndImageOnlyCheckbox = view.findViewById(R.id.descriptionAndPictureOnlyCheckBox);
        progressBar = view.findViewById(R.id.progressBarForReport);
        generateButton = view.findViewById(R.id.generateReportButton);

        ArrayAdapter<CharSequence> reportSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.report_options, R.layout.spinner_item_right_aligned);
        reportSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportTypeSpinner.setAdapter(reportSpinnerAdapter);

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                presenter.setSpinner(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        generateButton.setOnClickListener(v -> {
            presenter.makePdf(reportTypeSpinner.getSelectedItem().toString(),
                    descriptionAndImageOnlyCheckbox.isChecked());
        });

        return view;
    }

    void updateDetailSpinnerBasedOnSelection(String selectedItem, List<String> categories, List<String> periods) {
        detailSpinner.setVisibility(View.GONE);
        editTextReportParameter.setVisibility(View.GONE);
        detailTextView.setVisibility(View.GONE);

        switch (selectedItem) {
            case "Lot Number":
                editTextReportParameter.setHint("Enter Lot Number");
                editTextReportParameter.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Name":
                editTextReportParameter.setHint("Enter Name");
                editTextReportParameter.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextReportParameter.setVisibility(View.VISIBLE);
                break;
            case "Category":
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_right_aligned, categories);
                categoryAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                detailSpinner.setAdapter(categoryAdapter);
                detailTextView.setText(R.string.select_category);
                detailTextView.setVisibility(View.VISIBLE);
                detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "Period":
                ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_right_aligned, periods);
                periodAdapter.setDropDownViewResource(R.layout.spinner_item_right_aligned);
                detailSpinner.setAdapter(periodAdapter);
                detailTextView.setText(R.string.select_period);
                detailTextView.setVisibility(View.VISIBLE);
                detailSpinner.setVisibility(View.VISIBLE);
                break;
            case "All Items":
                break;
            default:
                break;
        }
    }

    void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    String getEditTextValue() {
        return editTextReportParameter.getText().toString();
    }

    String getDetailSpinnerValue() {
        return detailSpinner.getSelectedItem().toString();
    }

    void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void resetForm() {
        progressBar.setVisibility(View.GONE);
        reportTypeSpinner.setSelection(0);
        detailSpinner.setSelection(0);
        editTextReportParameter.setText("");
        descriptionAndImageOnlyCheckbox.setChecked(false);
    }

}